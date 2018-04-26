//
// Created by hlm on 26/04/18.
//
#include "MyUtils.h"

JNIEnv* JVM_GetEnv(JavaVM *vm_whole) {
    //JNIEnv是跟线程相关的
    if(vm_whole==NULL){
        return  NULL;
    }
    JNIEnv* env;
    vm_whole->GetEnv((void **) &env, JNI_VERSION_1_4);
    return env;
}

char* doJniNativeE(JNIEnv *env, jint index, jstring data) {
    char *ret=jstringTostring(env, data);
    LogD("ret -- <%s> ,  data:%s", __FUNCTION__, ret);
    return  ret;
}


char* doJniNativeD(JNIEnv *env, jint index, jstring data) {
    char *ret=jstringTostring(env, data);
    LogD("ret -- <%s> ,  data:%s", __FUNCTION__, ret);
    return  ret;
}





char* jstringTostring(JNIEnv* env, jstring jstr){
    char* rtn = NULL;
    jclass clsstring = env->FindClass("java/lang/String");  //注意这个地方，网上的有错
    jstring strencode = env->NewStringUTF("utf-8");
    jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
    jsize alen = env->GetArrayLength(barr);
    jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
    if (alen > 0){
        rtn = (char*)malloc(alen + 1);
        memcpy(rtn, ba, alen);
        rtn[alen] = 0;
    }
    env->ReleaseByteArrayElements(barr, ba, 0);
    return rtn;
}



//char* to jstring
jstring stringToJstring(JNIEnv* env, const char* pat){
    LogD("test -- <%s> 000 ,  data:%s", __FUNCTION__, pat);
    jclass strClass = env->FindClass("java/lang/String");
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(pat));
    env->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
    jstring encoding = env->NewStringUTF("utf-8");
    LogD("test -- <%s> 111 ,  data:%s", __FUNCTION__, pat);
    return (jstring)env->NewObject(strClass, ctorID, bytes, encoding);
}



