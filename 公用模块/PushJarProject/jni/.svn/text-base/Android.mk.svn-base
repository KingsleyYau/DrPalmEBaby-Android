MAIN_LOCAL_PATH := $(call my-dir)
include $(call all-subdir-makefiles)

#include $(LOCAL_PATH)/json/Android.mk

include $(CLEAR_VARS)
#$(call import-module,DrJson)
LOCAL_PATH := $(MAIN_LOCAL_PATH)
LOCAL_MODULE    := DrPushService
LOCAL_SRC_FILES := com_drcom_drpalm_Tool_service_DrPushServiceJni.cpp Arithmetic.cpp \
 DrCOMSocket.cpp DrThread.cpp DrHttpClient.cpp DrUrlConnection.cpp
LOCAL_LDLIBS 	:= -lssl -lcrypto -L$(SYSROOT)/usr/lib -llog
#LOCAL_CPPFLAGS += -std=c++0x
LOCAL_CFLAGS := -g
LOCAL_STRIP_MODULE := false
LOCAL_STATIC_LIBRARIES := DrJson
include $(BUILD_SHARED_LIBRARY) 
