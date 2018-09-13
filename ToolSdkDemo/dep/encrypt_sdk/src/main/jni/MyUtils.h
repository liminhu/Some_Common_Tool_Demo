#ifndef __MYUTILS_H__982364234523__
#define __MYUTILS_H__982364234523__

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
#include <string.h>

#define GETLR(store_lr)	\
	__asm__ __volatile__(	\
		"mov %0, lr\n\t"	\
		:	"=r"(store_lr)	\
	)

#define LOGD __android_log_print
#define LogD(fmt, ...)    {LOGD(ANDROID_LOG_DEBUG, "my_native_log", fmt, ##__VA_ARGS__);printf(fmt,##__VA_ARGS__);}
#define LogE(fmt, ...)    {LOGD(ANDROID_LOG_ERROR, "my_native_log", fmt, ##__VA_ARGS__);printf(fmt,##__VA_ARGS__);}

JNIEnv* JVM_GetEnv(JavaVM *vm_whole);


char* doJniNativeE(JNIEnv *env, jint index, jstring data);
char* doJniNativeD(JNIEnv *env,  jint index, jstring data);


char* jstringTostring(JNIEnv* env, jstring jstr);
jstring stringToJstring(JNIEnv* env, const char* pat);

#endif   

