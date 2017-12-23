//
// Created by hxm on 2017/7/18.
//

#include <jni.h>

extern void Hookinit(JNIEnv *env, jclass clazz, jint sdkVersion);
extern void findAndBackupAndHook(JNIEnv *env, jclass clazz,
 jclass targetClass, jstring methodName, jstring methodSig, jboolean isStatic,jobject hook, jobject backup);

