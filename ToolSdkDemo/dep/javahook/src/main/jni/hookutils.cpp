#include <string.h>
#include "hookutils.h"

#define LIBPLUGIN "libplugin.so"
#define LIBSANDBOX "libsandbox.so"

#define LIBC "/system/lib/libc.so"

#define L_GET_LIBDL_INFO    "__dl__Z14get_libdl_infov"


typedef void (*wrapper_inline_hook)(void *symbol, void *replace, void **result);
typedef void (*wrapper_add_dlopen_callback)(dlopen_callback callback);
typedef void (*wrapper_remove_dlopen_callback)(dlopen_callback callback);
typedef unsigned int (*wrapper_plt_hook)(const char *soname, const char *symbol, const char *victim, unsigned int new_symbol);
typedef void *(*wrapper_find_loaded_library)(const char *name);

static wrapper_plt_hook plt_hook = NULL;
static wrapper_inline_hook inline_hook = NULL;
static wrapper_add_dlopen_callback add_dlopen_callback = NULL;
static wrapper_remove_dlopen_callback remove_dlopen_callback = NULL;
static wrapper_find_loaded_library find_loaded_library = NULL;






static inline Elf32_Shdr *elf_sheader(Elf32_Ehdr *hdr) {
    return (Elf32_Shdr *)((int)hdr + hdr->e_shoff);
}

static inline Elf32_Shdr *elf_section(Elf32_Ehdr *hdr, int idx) {
    return &elf_sheader(hdr)[idx];
}

static inline char *elf_str_table(Elf32_Ehdr *hdr) {
    if(hdr->e_shstrndx == SHN_UNDEF) return NULL;
    return (char *)hdr + elf_section(hdr, hdr->e_shstrndx)->sh_offset;
}

static inline char *elf_lookup_string(Elf32_Ehdr *hdr, int offset) {
    char *strtab = elf_str_table(hdr);
    if(strtab == NULL) return NULL;
    return strtab + offset;
}

static inline Elf32_Shdr *elf_symtab(Elf32_Ehdr *hdr) {
    Elf32_Shdr *shdr = elf_sheader(hdr);
    int i;
    for (i=0; i<hdr->e_shnum; i++) {
        if (shdr[i].sh_type == SHT_SYMTAB) {
            return &shdr[i];
        }
    }

    return NULL;
}
#define ELF_STRTAB ".strtab" /* string table */
static inline char *elf_strtab(Elf32_Ehdr *hdr) {
    Elf32_Shdr *shdr = elf_sheader(hdr);
    int i;
    for (i=0; i<hdr->e_shnum; i++) {
        if (strcmp(ELF_STRTAB, elf_lookup_string(hdr, shdr[i].sh_name)) == 0) {
            return (char *)((int)hdr + shdr[i].sh_offset);
        }
    }

    return NULL;
}

static inline Elf32_Sym *elf_syms(Elf32_Ehdr *hdr, Elf32_Shdr *shdr) {
    Elf32_Sym *syms = (Elf32_Sym *)((int)hdr + shdr->sh_offset);
    return syms;
}

static unsigned long get_linker_base() {
    char path[1024], buf[1024], *start = NULL;
    unsigned long start_addr = -1;
    FILE *f = NULL;

    snprintf(path, sizeof(path), "/proc/self/maps");

    if ((f = fopen(path, "r")) == NULL) {
        return 0;
    }

    for (;;) {
        if (!fgets(buf, sizeof(buf), f)) {
            break;
        }
        if (!strstr(buf, "/system/bin/linker")) {
            continue;
        }
        start = strtok(buf, "-");
        sscanf(start, "%p", &start_addr);
        break;
    }

    fclose(f);

    return start_addr;
}

static void *find_hidden_sym(const char* dst_sym) {
    unsigned long linker_base = get_linker_base();
    if (linker_base == -1) {
        return NULL;
    }

    int i;
    struct stat s;
    int fd = open("/system/bin/linker", O_RDONLY);
    fstat(fd, &s);

    const char *block = (const char *)mmap(NULL, s.st_size, PROT_READ, MAP_PRIVATE, fd, 0);
    if (block == NULL) {
        close(fd);
        return NULL;
    }
    Elf32_Ehdr *hdr = (Elf32_Ehdr *)block;
    Elf32_Shdr *shdr = elf_symtab(hdr);
    char *strtab = elf_strtab(hdr);
    int ptr = 0;

    if (shdr != NULL && strtab != NULL) {
        Elf32_Sym *syms = elf_syms(hdr, shdr);
        int size = shdr->sh_size / sizeof(Elf32_Sym);
        for (i=0; i<size; i++) {
            char *sym_name = strtab + syms[i].st_name;
            if (strcmp(sym_name, dst_sym) == 0) {
                ptr = syms[i].st_value;
                break;
            }
        }
    }

    munmap((void *)block, s.st_size);
    close(fd);

    if (ptr != NULL) {
        return (void *)(linker_base + ptr);
    } else {
        return NULL;
    }


}

static void *sohandler = (void *)-1;

static void *find_library(const char *name) {
    soinfo *si;
    const char *bname;
    bname = strrchr(name, '/');
    bname = bname ? bname + 1 : name;

//    LogD("<%s> %s", __FUNCTION__,name)
    //TODO API >= 24 android 7.0 专用
    void *get_libdl_info_ = find_hidden_sym(L_GET_LIBDL_INFO);
    if (get_libdl_info_) {
//        LogD("<%s> find_hidden_sym(L_GET_LIBDL_INFO) = %x", __FUNCTION__,get_libdl_info_)
        soinfo *so = ((soinfo *(*)()) get_libdl_info_)();
        while (so != NULL) {
            if (so->name && strlen(so->name) > 3 && !strcmp(bname, so->name)) {
                return so;
            }
            so = so->next;
        }
    }else {
        //TODO API < 24 android 7.0 以下用
        soinfo *somain = (soinfo *) dlopen("libdl.so", RTLD_NOW);
//        LogD("<%s> dlopen(libdl.so) = %x", __FUNCTION__,somain)
        for (si = somain; si != NULL; si = si->next) {
//            LogD("<%s> %s, %s", name, si->name)
            if (!strcmp(bname, si->name)) {
                return si;
            }
        }
    }
    return NULL;
}

static void *getFunctionPtr(const char *function){
//    LogD("<%s> ... 0x%x, %s ", __FUNCTION__, sohandler, function);
    void *func = dlsym(sohandler, function);
    if (func == NULL){

        if (sohandler == (void *)-1 || sohandler == NULL)
            sohandler = find_library(LIBPLUGIN);
        if (sohandler == (void *)-1 || sohandler == NULL)
            sohandler = find_library(LIBSANDBOX);
//        LogD("<%s> ... sohandler = %x ", __FUNCTION__, sohandler);
        func = dlsym(sohandler, function);
//        LogD("<%s> ... func = %x ", __FUNCTION__, func);

    }
    return func;
}

void* findLoadedLib(const char *soname){
    if(find_loaded_library == NULL){
        find_loaded_library = (wrapper_find_loaded_library)getFunctionPtr("find_loaded_library_so");
//        LogD("<%s> find_loaded_library = 0x%x", __FUNCTION__, find_loaded_library);
    }
    return find_loaded_library(soname);
}


void inlineHookAddress(void *address, void *replace, void **result){
    if(inline_hook == NULL){
        inline_hook = (wrapper_inline_hook)getFunctionPtr("inline_hook");
    }
    inline_hook(address, replace, result);
}

void *getAddress(void *handle, const char *symbol){
    return dlsym(handle, symbol);
}

void inlineHookSymbol(void *sohandle, const char *symbol, void *replace, void **result){
    void *address = dlsym(sohandle, symbol);
    if(address){
        if(inline_hook == NULL){
            inline_hook = (wrapper_inline_hook)getFunctionPtr("inline_hook");
//            LogD("<%s> inline_hook = 0x%x", __FUNCTION__, inline_hook);
        }
        inline_hook(address, replace, result);
    }
}

void registDlopen(dlopen_callback callback){
    if(add_dlopen_callback == NULL){
        add_dlopen_callback = (wrapper_add_dlopen_callback)getFunctionPtr("add_dlopen_callback");
//        LogD("<%s> add_dlopen_callback = 0x%x", __FUNCTION__, add_dlopen_callback);
    }
    add_dlopen_callback(callback);
}

void unregistDlopen(dlopen_callback callback){
    if(remove_dlopen_callback == NULL){
        remove_dlopen_callback = (wrapper_remove_dlopen_callback)getFunctionPtr("remove_dlopen_callback");
    }
    remove_dlopen_callback(callback);
}



