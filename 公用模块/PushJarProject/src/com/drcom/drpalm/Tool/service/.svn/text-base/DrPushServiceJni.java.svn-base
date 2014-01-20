package com.drcom.drpalm.Tool.service;

public class DrPushServiceJni {
	private static final String LIBRARY_NAME = "DrPushService";
	static{
		try{
			System.loadLibrary(LIBRARY_NAME);
		}catch( Exception e){
			e.printStackTrace();
		}

	}
	/*
	 *  输出Jni日志
	 */
	static final String JNI_LOG_SAVE_PATH = "/sdcard/DrpalmPushLog/";
	static final String JNI_LOG_NAME = "JniLog";
	static final long TIME_FOR_CLEAN_JNI_LOG = 10*24*3600*1000;
	public void WriteJniLog(byte[] value) {
		DrServiceLog.clearInvalidLog(JNI_LOG_SAVE_PATH, TIME_FOR_CLEAN_JNI_LOG);
		DrServiceLog.writeLog(JNI_LOG_SAVE_PATH, JNI_LOG_NAME, new String(value));
	}
	public native boolean NativeInit();

	// PushService protocol
	public native boolean GetPushChallenge(String domain, String packageName, String indetify, DrServiceJniCallback2 jniHttpCallback);
	public native boolean RegPushToken(String domain, String challenge, String schoolid, String schoolKey, String packagename, String indetify, String model, String system, String appver, DrServiceJniCallback2 jniHttpCallback);
	public native boolean StartGetPushMessage(String domain, DrServicePushJniCallback jniHttpCallback);
	public native boolean StopUrlConnection(String url);

	//取MAC地址
	public native String GetFirstMacAddress();
	//取设备ID
	public native String GetDeviceId();
	//取tokenid
	public native String GetTokenId(String indetify, String packagename);

}
