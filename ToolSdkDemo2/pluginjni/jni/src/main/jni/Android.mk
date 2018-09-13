LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := sdk-plugins

LOCAL_SRC_FILES := JNI.cpp

LOCAL_LDLIBS +=  -llog -ldl -lc

LOCAL_ARM_MODE := arm

LOCAL_PROGUARD_ENABLED:= disabled

include $(BUILD_SHARED_LIBRARY)
