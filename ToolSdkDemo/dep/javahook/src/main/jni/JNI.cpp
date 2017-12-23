
//#include <sys/mman.h>
//#include <sys/atomics.h>

//#include <stdatomic.h>
#include <string.h>
#include "hookutils.h"
#include "dalvik_vm.h"

#define VICTIM_LIB "libDungeonHunter4HD.so"


//TODO dalvik hook code;

#define MAX_BACKUP_SLOTS          32
#define MAX_PARAMS_ALLOWED        8

#define __LIBC_INLINE__           __attribute__((always_inline))
#define LIKELY(exp)               (__builtin_expect((exp) != 0, true))
#define UNLIKELY(exp)             (__builtin_expect((exp) != 0, false))
#define METHOD_PTR(p)             reinterpret_cast<uintptr_t>(p)
#define NATIVE_METHOD(n, s)       #n, s, reinterpret_cast<void *>(n)
#define PLoad(lib)                ::dlopen(lib, RTLD_LAZY)
#define PFree(handle)             ::dlclose(handle)
#define PInvoke(so, symbol)       __PInvoke<__typeof__(symbol)>(so, SYMBOL_##symbol, #symbol)
template <typename func> __LIBC_HIDDEN__ __LIBC_INLINE__ func *__PInvoke(void *handle, const char *mangled_symbol, const char *symbol)
{
    void *sym = ::dlsym(handle, mangled_symbol);
    if (UNLIKELY(sym == NULL)) sym = ::dlsym(handle, symbol);
    return reinterpret_cast<func *>(sym);
}



static volatile int g_slotindex = 0;
static void        *g_dvm;
static Method       g_backups[MAX_BACKUP_SLOTS];
static __typeof__(&dvmFindPrimitiveClass)  g_dvmFindPrimitiveClass;
static __typeof__(&dvmBoxPrimitive)        g_dvmBoxPrimitive;
static __typeof__(&dvmUnboxPrimitive)      g_dvmUnboxPrimitive;
static __typeof__(&dvmDecodeIndirectRef)   g_dvmDecodeIndirectRef;
static __typeof__(&dvmReleaseTrackedAlloc) g_dvmReleaseTrackedAlloc;
static __typeof__(&dvmThreadSelf)          g_dvmThreadSelf;


static int __native_hook(JNIEnv *env, Method *vm_origin, jobject target)
{
    Method *vm_target = reinterpret_cast<Method *>(env->FromReflectedMethod(target));

    if (IS_METHOD_FLAG_SET(vm_origin, ACC_PRIVATE) ||
        IS_METHOD_FLAG_SET(vm_origin, ACC_STATIC) || *vm_origin->name == '<') {
        *vm_origin = *vm_target;
        return 1;
    } //if

    if (IS_METHOD_FLAG_SET(vm_origin, ACC_ABSTRACT) ||
        vm_origin->methodIndex >= vm_origin->clazz->vtableCount) {
        return -1;
    } //if


    vm_origin->clazz->vtable[vm_origin->methodIndex] = vm_target;
    return 0;
}

static void JNICALL dvmHookNativeNoBackup(JNIEnv *env, jclass, jobject origin, jobject target)
{
    Method *vm_origin = reinterpret_cast<Method *>(env->FromReflectedMethod(origin));
    __native_hook(env, vm_origin, target);
}

//-------------------------------------------------------------------------

static jint JNICALL dvmHookNative(JNIEnv *env, jclass jc, jobject origin, jobject target)
{
    pthread_mutex_t count_lock = PTHREAD_MUTEX_INITIALIZER;
    pthread_mutex_lock(&count_lock);
    int slot=g_slotindex++;


    Method *vm_origin = reinterpret_cast<Method *>(env->FromReflectedMethod(origin));
    g_backups[slot]   = *vm_origin;
    if (__native_hook(env, vm_origin, target) == 0) {
        CLEAR_METHOD_FLAG(&g_backups[slot], ACC_PUBLIC);
        SET_METHOD_FLAG(&g_backups[slot], ACC_PRIVATE);
    } //if
    pthread_mutex_unlock(&count_lock);



    return slot;
}

//-------------------------------------------------------------------------

static jvalue JNICALL __invoke_backup(JNIEnv *env, jint slot, jobject receiver, jobjectArray params)
{
    jvalue ret; ret.j = 0L;

    if (UNLIKELY(slot < 0 || slot >= NELEM(g_backups))) {
        return ret;
    } //if
    jvalue ps[MAX_PARAMS_ALLOWED];
    int np = env->GetArrayLength(params);
    if (UNLIKELY(np >= NELEM(ps))) {
        return ret;
    } //if
    auto const char * shorty = g_backups[slot].shorty;
    Thread *self   = g_dvmThreadSelf();
    while (--np >= 0) {
        ps[np].l = env->GetObjectArrayElement(params, np);
        if (shorty[np + 1] == 'L') continue; // 'shorty' descr uses L for all refs, incl array

        ClassObject* typeCls = g_dvmFindPrimitiveClass(shorty[np + 1]);
        if (typeCls != NULL) {
            Object *obj = g_dvmDecodeIndirectRef(self, ps[np].l);
            if (UNLIKELY(obj == NULL)) {
                return ret;
            } //if
            if (UNLIKELY(!g_dvmUnboxPrimitive(obj, typeCls, reinterpret_cast<JValue *>(&ps[np])))) {
                return ret;
            } //if
        } //if
    }

    jmethodID originalMethod  = reinterpret_cast<jmethodID>(&g_backups[slot]);
    bool isStaticMethod  = IS_METHOD_FLAG_SET(&g_backups[slot], ACC_STATIC);
    switch (shorty[0]) {
        case 'V':
            isStaticMethod ?
            env->CallStaticVoidMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
            env->CallVoidMethodA(receiver, originalMethod, ps);
            return ret;
        case 'Z':
            ret.z = isStaticMethod ?
                    env->CallStaticBooleanMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallBooleanMethodA(receiver, originalMethod, ps);
            break;
        case 'B':
            ret.b = isStaticMethod ?
                    env->CallStaticByteMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallByteMethodA(receiver, originalMethod, ps);
            break;
        case 'S':
            ret.s = isStaticMethod ?
                    env->CallStaticShortMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallShortMethodA(receiver, originalMethod, ps);
            break;
        case 'C':
            ret.c = isStaticMethod ?
                    env->CallStaticCharMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallCharMethodA(receiver, originalMethod, ps);
            break;
        case 'I':
            ret.i = isStaticMethod ?
                    env->CallStaticIntMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallIntMethodA(receiver, originalMethod, ps);
            break;
        case 'J':
            ret.j = isStaticMethod ?
                    env->CallStaticLongMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallLongMethodA(receiver, originalMethod, ps);
            break;
        case 'F':
            ret.f = isStaticMethod ?
                    env->CallStaticFloatMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallFloatMethodA(receiver, originalMethod, ps);
            break;
        case 'D':
            ret.d = isStaticMethod ?
                    env->CallStaticDoubleMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallDoubleMethodA(receiver, originalMethod, ps);
            break;
        default: // objects case 'L' and '['
            ret.l = isStaticMethod ?
                    env->CallStaticObjectMethodA(reinterpret_cast<jclass>(receiver), originalMethod, ps) :
                    env->CallObjectMethodA(receiver, originalMethod, ps);
    }

    return ret;
}

//-------------------------------------------------------------------------

JNIEXPORT void JNICALL dvmInvokeVoidMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    __invoke_backup(env, slot, receiver, params);
}

//-------------------------------------------------------------------------

JNIEXPORT jboolean JNICALL dvmInvokeBooleanMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).z;
}

//-------------------------------------------------------------------------

JNIEXPORT jbyte JNICALL dvmInvokeByteMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).b;
}

//-------------------------------------------------------------------------

JNIEXPORT jshort JNICALL dvmInvokeShortMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).s;
}

//-------------------------------------------------------------------------

JNIEXPORT jchar JNICALL dvmInvokeCharMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).c;
}

//-------------------------------------------------------------------------

JNIEXPORT int JNICALL dvmInvokeIntMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).i;
}

//-------------------------------------------------------------------------

JNIEXPORT jlong JNICALL dvmInvokeLongMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).j;
}

//-------------------------------------------------------------------------

JNIEXPORT jfloat JNICALL dvmInvokeFloatMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).f;
}

//-------------------------------------------------------------------------

JNIEXPORT jdouble JNICALL dvmInvokeDoubleMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).d;
}

//-------------------------------------------------------------------------

JNIEXPORT jobject JNICALL dvmInvokeObjectMethod(JNIEnv *env, jclass, jint slot, jobject receiver, jobjectArray params)
{
    return __invoke_backup(env, slot, receiver, params).l;
}



//TODO art hook code



extern "C"{
#include "cHeader.h"
}
extern void doProcessCheat(int flag, int arg1, int arg2);
JNIEXPORT void JNICALL jniDoProcessCheat(JNIEnv *env, jclass clazz, jint flag, jint arg1, jint arg2) {
    doProcessCheat(flag, arg1, arg2);
}
JNIEXPORT void JNICALL jniDoinitHook(JNIEnv *env, jclass clazz, jint version) {
    Hookinit(env, clazz, version);
}
JNIEXPORT void JNICALL jniDoFindAndBackupAndHook(JNIEnv *env, jclass clazz, jclass targetClass,
   jstring methodName, jstring methodSig, jboolean isStatic,jobject hook, jobject backup){
    findAndBackupAndHook(env,clazz,targetClass,methodName,methodSig,isStatic,hook,backup);
}
dlopen_callback gCallback;

static const char *LoadingLibrary(const char *filename){
    return filename;
}


static void LibraryLoaded(const char *filename, void *handle){
    if(strstr(filename, VICTIM_LIB) && handle){
//        LogD("<%s> ----------------filename=%s handle=%x ", __FUNCTION__, filename,handle);
        unregistDlopen(gCallback);
        soinfo *info = (soinfo*)handle;
        hook_address(info->base);
        hook_symbols(info);
//	    LogD("<%s> ----------------filename=%s handle=%d ", __FUNCTION__, filename,handle);
    }
    return;
}

void hookPrepare(){
    void *handle = findLoadedLib(VICTIM_LIB);
    if(handle){
        soinfo *info = (soinfo*)handle;
        hook_address(info->base);
        hook_symbols(info);
    }else{
        gCallback.onLoadingLibrary = LoadingLibrary;
        gCallback.onLibraryLoaded = LibraryLoaded;
        registDlopen(gCallback);
    }
}
//Class targetClass, String methodName, String methodSig,
//Method hook, Method backup
static const JNINativeMethod gHookMethods[] = {
        { "nativeDoCheat", "(III)V", (void*)jniDoProcessCheat },
        { "nativeinitHook", "(I)V", (void*)jniDoinitHook },
        { "nativeFindAndBackupAndHook", "(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/String;"
                                                "ZLjava/lang/reflect/Method;Ljava/lang/reflect/Method;)V",
                (void*)jniDoFindAndBackupAndHook },
        //TODO dalvik native function
        { "invokeVoidMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)V", (void*)dvmInvokeVoidMethod },
        { "invokeBooleanMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)Z", (void*)dvmInvokeBooleanMethod},
        { "invokeByteMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)B", (void*)dvmInvokeByteMethod },
        { "invokeShortMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)S", (void*)dvmInvokeShortMethod },
        { "invokeCharMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)C", (void*)dvmInvokeCharMethod },
        { "invokeIntMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)I", (void*)dvmInvokeIntMethod },
        { "invokeLongMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)J", (void*)dvmInvokeLongMethod },
        { "invokeFloatMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)F", (void*)dvmInvokeFloatMethod },
        { "invokeDoubleMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)D", (void*)dvmInvokeDoubleMethod },
        { "invokeObjectMethod", "(ILjava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", (void*)dvmInvokeObjectMethod },
//
        { "dalvikHook", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)I", (void*)dvmHookNative },
        { "dalvikHookNoBackup", "(Ljava/lang/reflect/Method;Ljava/lang/reflect/Method;)V", (void*)dvmHookNativeNoBackup },
};

extern "C" __attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM* vm, void* reserved){
#ifdef NDK_DEBUG
    LogD("<%s> libA8 %s %s", __FUNCTION__, __DATE__, __TIME__);
#endif
   //TODO dvm
 //   LogD("<%s> libA8 %s %s  0000", __FUNCTION__, __DATE__, __TIME__);
    g_dvm = PLoad("libdvm.so");
    if (UNLIKELY(g_dvm != NULL)) {
        g_dvmFindPrimitiveClass  = PInvoke(g_dvm, dvmFindPrimitiveClass);
        g_dvmBoxPrimitive        = PInvoke(g_dvm, dvmBoxPrimitive);
        g_dvmUnboxPrimitive      = PInvoke(g_dvm, dvmUnboxPrimitive);
        g_dvmDecodeIndirectRef   = PInvoke(g_dvm, dvmDecodeIndirectRef);
        g_dvmReleaseTrackedAlloc = PInvoke(g_dvm, dvmReleaseTrackedAlloc);
        g_dvmThreadSelf          = PInvoke(g_dvm, dvmThreadSelf);
        if (UNLIKELY(!(METHOD_PTR(g_dvmFindPrimitiveClass) & METHOD_PTR(g_dvmBoxPrimitive) &
                       METHOD_PTR(g_dvmUnboxPrimitive) & METHOD_PTR(g_dvmDecodeIndirectRef) &
                       METHOD_PTR(g_dvmReleaseTrackedAlloc) & METHOD_PTR(g_dvmThreadSelf)))) { // bugs?
            return JNI_ERR;
        } //if
    } //if
    LogD("<%s> libA8 %s %s  111", __FUNCTION__, __DATE__, __TIME__);

    JNIEnv* env = NULL;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }

    jclass clazz;

    clazz = env->FindClass("com/gameassist/plugin/nativeutils/NativeUtils");
    if (clazz == NULL) {
    //    LogD("<%s> libA8 %s %s  -1 退出。。。", __FUNCTION__, __DATE__, __TIME__);
        return -1;
    }
    LogD("<%s> libA8 %s %s  2222", __FUNCTION__, __DATE__, __TIME__);
    if (env->RegisterNatives(clazz, gHookMethods, sizeof(gHookMethods) / sizeof(gHookMethods[0])) < 0) {
        return -1;
    }
  //  hookPrepare();
    return JNI_VERSION_1_4;
}
