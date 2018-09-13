LOCAL_PATH := $(call my-dir)
MAIN_LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/InlineHook
LOCAL_MODULE    := InlineArmHook
LOCAL_SRC_FILES := InlineHook.cpp

LOCAL_LDLIBS +=  -llog -ldl -lc  -llog
LOCAL_ARM_MODE := arm
include $(BUILD_SHARED_LIBRARY)

##生成第二个so
include $(MAIN_LOCAL_PATH)/InlineHook/Android.mk