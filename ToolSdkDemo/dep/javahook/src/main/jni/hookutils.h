#ifndef __HOOK_UTILS_H__982364234523__
#define __HOOK_UTILS_H__982364234523__

#include <jni.h>
#include <android/log.h>
#include <dirent.h>
#include <stdlib.h>
#include <stdarg.h>
#include <errno.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <time.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <dlfcn.h>
#include <elf.h>
#include <pthread.h>

#include <sys/mman.h>

#define LOGD __android_log_print
#define LogD(fmt, ...)    {LOGD(ANDROID_LOG_ERROR, "my_gameassist", fmt, ##__VA_ARGS__);printf(fmt,##__VA_ARGS__);}

#define DISAMBLE_CNT 5

typedef struct _hook_address{
	int address;
	void *new_func;
	void **old_func;
	int disamble[DISAMBLE_CNT];
}HOOK_Address;

typedef struct _find_address{
	int address;
	void **func;
}FIND_ADDRESS;

typedef struct _hook_symbol{
	const char *symbol;
	void *new_func;
	void **old_func;
}HOOK_SYMBOL;

typedef struct _find_symbol{
	const char *symbol;
	void **func;
}FIND_SYMBOL;

#undef ANDROID_SH_LINKER
#define ANDROID_ARM_LINKER
#define SOINFO_NAME_LEN 128
typedef struct soinfo {
	const char name[SOINFO_NAME_LEN];
	Elf32_Phdr *phdr;
	int phnum;
	unsigned entry;
	unsigned base;
	unsigned size;

	int unused;  // DO NOT USE, maintained for compatibility.

	unsigned *dynamic;

	unsigned wrprotect_start;
	unsigned wrprotect_end;

	struct soinfo *next;
	unsigned flags;

	const char *strtab;
	Elf32_Sym *symtab;

	unsigned nbucket;
	unsigned nchain;
	unsigned *bucket;
	unsigned *chain;

	unsigned *plt_got;

	Elf32_Rel *plt_rel;
	unsigned plt_rel_count;

	Elf32_Rel *rel;
	unsigned rel_count;

#ifdef ANDROID_SH_LINKER
	Elf32_Rela *plt_rela;
    unsigned plt_rela_count;

    Elf32_Rela *rela;
    unsigned rela_count;
#endif /* ANDROID_SH_LINKER */

	unsigned *preinit_array;
	unsigned preinit_array_count;

	unsigned *init_array;
	unsigned init_array_count;
	unsigned *fini_array;
	unsigned fini_array_count;

	void (*init_func)(void);
	void (*fini_func)(void);

#ifdef ANDROID_ARM_LINKER
	/* ARM EABI section used for stack unwinding. */
	unsigned *ARM_exidx;
	unsigned ARM_exidx_count;
#endif

	unsigned refcount;
//    struct link_map linkmap;
} soinfo;

typedef const char *(*OnLoadingLibrary)(const char *filename);
typedef void (*OnLibraryLoaded)(const char *filename, void *handle);

typedef struct {
	OnLoadingLibrary onLoadingLibrary;
	OnLibraryLoaded onLibraryLoaded;
} dlopen_callback;

void inlineHookAddress(void *address, void *replace, void **result);
void inlineHookSymbol(void *sohandle, const char *symbol, void *replace, void **result);
void registDlopen(dlopen_callback callback);
void unregistDlopen(dlopen_callback callback);
void *getAddress(void *handle, const char *symbol);
void *findLoadedLib(const char *soname);
//void hookPrepare();
void hook_address(int baseAddr);
void hook_symbols(soinfo *soinfo);

//extern static void Java_lab_galaxy_yahfa_HookMain_init(JNIEnv *, jclass, jint);
#endif

