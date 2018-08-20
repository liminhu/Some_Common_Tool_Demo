LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_ARM_MODE := arm
LOCAL_MODULE    := IHook
LOCAL_SRC_FILES := IHook.c ihookstub.s ihookstubthumb.s fixPCOpcode.c
LOCAL_LDLIBS +=  -llog -ldl -lc  -llog

include $(BUILD_STATIC_LIBRARY)
