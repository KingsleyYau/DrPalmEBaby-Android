package com.drcom.drpalm.Tool.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushServiceReceiver extends BroadcastReceiver{
	static final String ALARM_ACTION = "com.drcom.drpalm.pushalarm.action";
	@Override
	public void onReceive(Context context, Intent intent){
		String stringAction = intent.getAction();
		//Bundle extras = intent.getExtras();
		// 开机广播
		if(stringAction.equals(Intent.ACTION_BOOT_COMPLETED)){
    		Log.d("PushServiceReceiver:BroadcastReceiver", Intent.ACTION_BOOT_COMPLETED);
    		writeLog("推送服务广播接收器检测到开机启动");
		}
	}
	////////////////////////////////////////////////////////////////////////////////////
	/*
	 *  输出日志
	 */
	static final String PUSH_LOG_SAVE_PATH = "/sdcard/DrpalmPushLog/";
	static final String PUSH_LOG_NAME = "PushLog";
	static final long TIME_FOR_CLEAN_PUSH_LOG = 10*24*3600*1000;

	private void writeLog(String strLog) {
	    /*
	     * 清除旧日志
	     * 默认保存10天的日志
	     * day: 保存天数
	     */
		DrServiceLog.clearInvalidLog(PUSH_LOG_SAVE_PATH, TIME_FOR_CLEAN_PUSH_LOG);
		DrServiceLog.writeLog(PUSH_LOG_SAVE_PATH, PUSH_LOG_NAME, strLog);
	}
	////////////////////////////////////////////////////////////////////////////////////
}
