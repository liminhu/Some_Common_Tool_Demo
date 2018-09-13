#ifndef __MY_UTILS_H__982364234523__
#define __MY_UTILS_H__982364234523__


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

#define LOG_MY_TAG "my_sdk_ndk"



#ifdef LOG_MY_TAG
    #define LOGD __android_log_print
    #define LogD(fmt, ...)    {LOGD(ANDROID_LOG_DEBUG, LOG_MY_TAG, fmt, ##__VA_ARGS__);printf(fmt,##__VA_ARGS__);}
    #define LogE(fmt, ...)    {LOGD(ANDROID_LOG_ERROR, LOG_MY_TAG, fmt, ##__VA_ARGS__);printf(fmt,##__VA_ARGS__);}
#else
   #define LogD(...) ((void)0)
   #define LogE(...) ((void)0)
#endif


#endif   

