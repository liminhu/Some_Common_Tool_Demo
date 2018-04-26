//
// Created by hlm on 26/04/18.
//
#include "MyUtils.h"
static JavaVM *vm_whole;


static void JNICALL jniNativeInit(JNIEnv *env, jclass clazz, jobject jcontent) {
//    signatureStringToHashCode("308202c3308201aba00302010202041d4c0298300d06092a864886f70d01010b050030123110300e0603550403130772656462756c6c301e170d3137313031303037353532345a170d3432313030343037353532345a30123110300e0603550403130772656462756c6c30820122300d06092a864886f70d01010105000382010f003082010a02820101008e5f421437bf53f5b2819aafad22d0f7b16cd2ce810480cee2308b32789659a013ce408e389f7c4e5164126ebf2b6ab8aa435c67898ae37b837a28a65517c8b32896fda48b9ac5d066e13acd9db85d85093a8a345c0c56606713ecaf03e7d9d75da78c05b210755bc71247e1826682437f7abd2f16a724c05e2fd78c75dcc36053524bfbe605a78b915903888602f0ece4fc31f09bc981ce685be6a73582af30f3b389b10eea7991a370070ace4d8fb5948aa67a79042a4d93518a2cf875dcb8c5c2cea670a662649b86ed3cc130dcd26f002206cc7d544a370be9072f201c25c4e82c0acdf3f3a3d190eea3f1423671c053973b915f7fc02667059c766ddc830203010001a321301f301d0603551d0e041604142d97224297d389647299c04f7deff2bc3761c06c300d06092a864886f70d01010b050003820101008c131dc40287fb209cc50de290e6bc70cd570ed9c8cee0256a60c3427bb8e4938961d4843687425bd3e8162e2f7b6babe27abd69f24e69732ff46d6634567cb423f6eefbc2cfa58ab39c7bcac7136f3aa521c8393b8bcbab01c9f81362c36c3ff2eca9ba72e59caf8b2a9a3c75ce499031f23b93b06663c04bbc7d218aca95bcb1a91599593503eacb603b494f45f923381f1e535be2dc52f23f8cc5d19df1058f19f0a8993081b90d0f919b4819cee1ee6d05d65839407601a9f3c69684f0b69c9f77303278aa2ae4a470f0993a346231c76b303e84abbd6b6e0d86d1288fc7c1b537c869035baf61bcb298fa59c9042d1ce358ae15b36ce405c6ed88cbd4fb");
  //  initMiniSDK(axuls_def,env,clazz,sdk_obj,pm_obj);

}




jstring  JNICALL jniNativeE(JNIEnv *env, jclass clazz, jint index, jstring data) {
    char* ret=doJniNativeE(env, index, data);
    if(ret!=NULL){
        char * d="出错了。。";
        return  stringToJstring(env, d);
    }
    return stringToJstring(env, ret);
}




 jstring  JNICALL jninativeD(JNIEnv *env, jclass clazz, jint index, jstring data) {
    char* ret=doJniNativeD(env, index, data);
    if(ret==NULL){
        char * d="出错了。。";
        return  stringToJstring(env, d);
    }
    return stringToJstring(env, ret);
}


static const JNINativeMethod gHookMethods[] = {
    //    {"nativeInit", "(Landroid/content/Content;)I", (void *) jniNativeInit},
        {"nativeE", "(ILjava/lang/String;)Ljava/lang/String;", (void *) jniNativeE},
        {"nativeD", "(ILjava/lang/String;)Ljava/lang/String;", (void *) jninativeD}
};




extern "C" __attribute__ ((visibility("default"))) jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    LogD(" encrypt_sdk .... <%s> ---  %s %s is begin ", __FUNCTION__, __DATE__, __TIME__);
    vm_whole = vm;
    JNIEnv *env = NULL;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return -1;
    }
    jclass clazz = env->FindClass("com/mx/sdk/nativeutils/NativeUtils");
    if (clazz == NULL) {
        return -1;
    }

    if (env->RegisterNatives(clazz, gHookMethods, sizeof(gHookMethods) / sizeof(gHookMethods[0])) <
        0) {
        return -1;
    }
    return JNI_VERSION_1_4;
}



