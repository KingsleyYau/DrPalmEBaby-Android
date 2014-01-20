package com.drcom.drpalm.Tool.service;

/*
 * Create by KingsleyYau 2012-10-29
 * 守护服务(本地服务利用startService启动), 保证推送服务(远程服务bindService启动)不被杀死
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class DrPalmGuideService extends Service{
	public static String PACKAGENAME = "";//GoodPlaceApp.getContext().getPackageName();
	static final String RESTART_GUIDE_SERVICE_ACTION = PACKAGENAME + "action.RestartGuideService";
	@Override
	public void onCreate(){
		Log.d("DrPalmGuideService.onCreate","Enter");
		writeLog("创建守护服务成功!");
		PACKAGENAME = getApplicationContext().getPackageName();
	}
	@Override
	public void onStart(Intent intent, int startId){
		Log.d("DrPalmGuideService.onStart", String.valueOf(startId));
		
		Intent intentPushService = new Intent(getApplicationContext(),DrPalmPushService.class);
		getApplicationContext().startService(intentPushService);
	}
	@Override
	public void onDestroy(){
		Log.d("DrPalmGuideService.onDestroy", "Enter");
		writeLog("守护服务被销毁,发送重启广播!");
		Intent sendIntent = new Intent(RESTART_GUIDE_SERVICE_ACTION);
		sendBroadcast(sendIntent);
	}
	@Override
	public void onLowMemory() {
		Log.d("DrPalmGuideService.onLowMemory", "Enter");
		writeLog("系统发送内存不够");
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	////////////////////////////////////////////////////////////////////////////////////
	/*
	 *  输出日志
	 */
	static final String PUSH_LOG_SAVE_PATH = "/sdcard/DrpalmPushLog/";
	static final String PUSH_LOG_NAME = "PushLog";
	static final long TIME_FOR_CLEAN_PUSH_LOG = 10*24*3600*1000;

	synchronized private void writeLog(String strLog) {
	    /*
	     * 清除旧日志
	     * 默认保存10天的日志
	     * day: 保存天数
	     */
		DrServiceLog.getInstance(getApplicationContext()).clearInvalidLog(PUSH_LOG_SAVE_PATH, TIME_FOR_CLEAN_PUSH_LOG);
		DrServiceLog.getInstance(getApplicationContext()).writeLog(PUSH_LOG_SAVE_PATH, PUSH_LOG_NAME, strLog);
	}
	////////////////////////////////////////////////////////////////////////////////////
}

