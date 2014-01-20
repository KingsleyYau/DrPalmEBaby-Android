package com.drcom.drpalm.Tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.drcom.drpalm.Tool.service.ConnectPushCallback;
import com.drcom.drpalm.Tool.service.DrPalmGuideService;
import com.drcom.drpalm.Tool.service.DrPalmPushService;
import com.drcom.drpalm.Tool.service.IDrPalmPush;
import com.drcom.drpalm.Tool.service.IDrPalmPushCallback;
import com.drcom.drpalm.objs.DeviceUuidFactory;
import com.drcom.drpalm.objs.PushRegItem;

public class PushManager {
	private static Context mContext;
	// 推送设置信息
	private static ServiceConnection mPushConnection = null;
	private static IDrPalmPush mDrPalmPushService = null;
	
	/**
	 * push设置初始化
	 * @param context
	 */
	public static void init(Context context,final ConnectPushCallback callback){
		mContext = context;
		mPushConnection = new ServiceConnection(){
			public void onServiceDisconnected(ComponentName name) {
				mDrPalmPushService = null;
	        }
	        public void onServiceConnected(ComponentName name, IBinder service) {
	        	
	        	mDrPalmPushService = IDrPalmPush.Stub.asInterface(service);
	        	Log.d("RequestOperation:onServiceConnected","bind DrPalmPushService Success");
	        	if(callback != null){
	        		callback.onSuccess();
	        	}
	        }
		};
		
		Intent intentPushService = new Intent(mContext,DrPalmPushService.class);
		mContext.startService(intentPushService);
		boolean ret = mContext.bindService(intentPushService, mPushConnection, Context.BIND_AUTO_CREATE);
		Log.d("RequestOperation:bindPushService:", String.valueOf(ret));

		Intent intentGuideService = new Intent(mContext,DrPalmGuideService.class);
		mContext.startService(intentGuideService);
		Log.d("RequestOperation:startGuideService:", "");
	}
	
	/**
	 * 注册PUSH服务
	 * @param schoolid
	 * @param schoolkey
	 * @throws RemoteException
	 */
	public static void Register(String schoolid,String schoolkey) throws RemoteException{
		String packageName = mContext.getPackageName();
		PushRegItem regItem = new PushRegItem();
		regItem.packageName = packageName;
		regItem.SchoolID = schoolid;
		regItem.SchoolKey = schoolkey;
		regItem.appver = "";
		PackageManager packageManager = mContext.getPackageManager();
		try {
			PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(),0);
			regItem.appver = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mDrPalmPushService.RegAppPush(regItem, new IDrPalmPushCallback.Stub(){
			@Override
			public void onError(String err) throws RemoteException {
//				callback.onError(err);
			}
			@Override
			public void onSuccess() throws RemoteException {
//				callback.onSuccess();
			}
		});
	}
	
	public static void unbindService(){
		mContext.unbindService(mPushConnection);
	}
	
	/**
	 * 取得本机Tokenid
	 * @return
	 */
	public static String getTokenid(String deviceid,String packagename){
		DeviceUuidFactory duf = new DeviceUuidFactory(mContext);
		return duf.getTokenid(deviceid,packagename);
	}
}
