#include <jni.h>
#include <string>
#include "MyUtils.h"

extern "C" JNIEXPORT jstring

JNICALL Java_com_hlm_tes_demo_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {

    std::string hello = "Hello from C++";
    LogE("测试c++ --- jni  demo : %s", hello.c_str());
    return env->NewStringUTF(hello.c_str());
}
