package com.drcom.drpalm.Tool.service;
/*
 * Create by KingsleyYau 2012-10-25
 * 1.增加�?��自启�?
 * 2.推�?重连机制: (1.断线3秒后重连;2.应用向服务注册成功重�?3.网络由断�?��为连接状态重�?4.30秒检测一次网络状�?但只写日志不做�?辑操
 * 3.启动步骤:(1.应用向服务注�?添加到应用列�?并请求重�?2.重连触发遍历应用列表;)
 */

import java.util.List;

import com.drcom.drpalm.objs.DeviceUuidFactory;
import com.drcom.drpalm.objs.PushActionDefine;
import com.drcom.drpalm.objs.PushMessageItem;
import com.drcom.drpalm.objs.PushRegItem;
import com.drcom.drpalm.objs.PushUpgradeAppItem;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;


public class DrPalmPushService extends Service{
	private final String PUSHCENTER = "http://push.doctorcom.com:88/";
	
	/*
	 * 1.2v 添加服务错误返回增加重连时间
	 */
	static private String PUSH_SERVICE_VERSION = "1.2";
	static int RECONNECT_TIME = 10 * 1000; // 每次错误返回增加的重连间隔
	static int RECONNECT_MAX_TIME = 60 * 1000; // 最大的错误重连间隔


	private class MessageObject{
		PushMessageItem pushMessageItem = null;
		PushRegItem pushRegItem = null;
		public MessageObject(PushMessageItem pushMessageItem, PushRegItem pushRegItem){
			this.pushMessageItem = pushMessageItem;
			this.pushRegItem = pushRegItem;
		}
	};
	private class MessageJsonObject{
		String pushMessageStr = null;
		PushRegItem pushRegItem = null;
		public MessageJsonObject(String jsonstr, PushRegItem pushRegItem){
			this.pushMessageStr = jsonstr;
			this.pushRegItem = pushRegItem;
		}
	};
	private class UpgradeObject{
		PushUpgradeAppItem pushUpgradeAppItem = null;
		PushRegItem pushRegItem = null;
		public UpgradeObject(PushUpgradeAppItem pushUpgradeAppItem, PushRegItem pushRegItem){
			this.pushUpgradeAppItem = pushUpgradeAppItem;
			this.pushRegItem = pushRegItem;
		}
	};
	/*
	 * Message queue define
	 * RECEIVE_PUSH_MESSAGE: 接收推送消息
	 * UPGRADE_APP: 接收升级通知
	 */
	public static final int RECEIVE_PUSH_MESSAGE = 0;
	public static final int UPGRADE_APP = 1;
	public static final int RESTART_PUSH = 2;
	public static final int GET_CHALLENGE = 3;
	public static final int REG_TOKEN = 4;
	public static final int START_GET_PUSH = 5;

	private Object mSetpLock = new Object();

	// 标志握手流程
	private enum RegisterStep{
		STOPSETP,
		GETCHALLENGE,
		GETCHALLENGE_SUC,
		REGTOKEN,
		REGTOKEN_SUC,
		START_GET_PUSH,
	};
	RegisterStep mCurentStep = RegisterStep.STOPSETP;

	private boolean mIsDestoryService = false;
	private String mImeistring;

	// 支持多个应用需要改成多个
	private String mChallenge = "";
	private PushRequest mPushRequest;
	private int mReconnectTime = RECONNECT_TIME;


	/*
	 *  Receiver register
	 *  Intent.ACTION_PACKAGE_REMOVED: 卸载应用
	 *  ConnectivityManager.CONNECTIVITY_ACTION: 网络改变
	 */
	public GroupReceiver mGroupReceiver;
	public void initReceiver(){
    	mGroupReceiver = new GroupReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(DrPalmGuideService.RESTART_GUIDE_SERVICE_ACTION);
        //filter.addDataScheme("package");
        filter.addAction(ALARM_ACTION);
        registerReceiver(mGroupReceiver,filter);
    }
	public class GroupReceiver extends BroadcastReceiver{
		private boolean bConnectionActionFlag = false;
    	@Override
    	public void onReceive(Context context, Intent intent){
    		String stringAction = intent.getAction();
    		if(stringAction.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
    			Log.d("DrPalmPushService:BroadcastReceiver", ConnectivityManager.CONNECTIVITY_ACTION);
    			writeLog("检测到网络状态发生改变...");
    			if(isNeedToRestart()){
    				Log.d("DrPalmPushService:BroadcastReceiver", "bConnectionActionFlag:" + String.valueOf(bConnectionActionFlag));
    				if(!bConnectionActionFlag) {
    					bConnectionActionFlag = true;
        				writeLog("检测到网络被打开:" + String.valueOf(bConnectionActionFlag));
            			//restartPush();
        				Message message = Message.obtain();
        				message.arg1 = RESTART_PUSH;
        				mHandler.sendMessage(message);
    				}
        			else {
        				bConnectionActionFlag = false;
        				writeLog("检测到网络被打开:" + String.valueOf(bConnectionActionFlag));
        			}
    			}
    			else {
    				bConnectionActionFlag = false;
    				writeLog("检测到网络被关闭:" + String.valueOf(bConnectionActionFlag));
    			}
    		}
    		else if(stringAction.equals(Intent.ACTION_PACKAGE_REMOVED)){
    			Log.d("DrPalmPushService:BroadcastReceiver", Intent.ACTION_PACKAGE_REMOVED);
    			String packageName = intent.getDataString();
    			List<PushRegItem> itemList = mPushPreferenceManagement.loadPushSetting();
    			for(PushRegItem item: itemList){
    				if(item.packageName.equals(packageName)){
    					itemList.remove(item);
    					mPushPreferenceManagement.saveSetting(itemList);
    					break;
    				}
    			}
    		}
    		else if(stringAction.equals(DrPalmGuideService.RESTART_GUIDE_SERVICE_ACTION)) {
    			writeLog("收到重启守护服务广播!");
    			reStartGuideService();
    		}
			// 闹铃广播
    		else if(stringAction.equals(ALARM_ACTION)) {
				Log.d("DrPalmPushService:BroadcastReceiver", ALARM_ACTION);
				writeLog("收到闹铃广播,服务运行已经到达1小时");
				Message message = Message.obtain();
				message.arg1 = RESTART_PUSH;
				mHandler.sendMessage(message);
			}
    	}
	}

	static final String ALARM_ACTION = "com.drcom.drpalm4datang.pushalarm.action";
	// 闹铃间隔， 这里设为1小时闹一次
	static final int INTERVAL = 60 * 60 * 1000;
	private void setAlarmTime(Context context, long interval) {
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(ALARM_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, sender);
	}

	/**
     * 网络是否连接
     * @param context
     * @return
     */
    private boolean IsNetUsed(Context context)
    {
    	boolean success = false;
		//获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
		if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络
			Log.d("DrPalmPushService:IsNetUsed", "正在使用WIFI网络");
			writeLog("正在使用WIFI网络");
			success = true;
		}

		try{
			state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
			if (State.CONNECTED == state) { // 判断是否正在使用GPRS网络
				Log.d("DrPalmPushService:IsNetUsed", "正在使用GPRS网络");
				writeLog("正在使用GPRS网络");
				success = true;
			}
		}catch (Exception e) {
			//没SIM卡的机可能会报空指针错误
		}

		if(success == false){
			Log.d("DrPalmPushService:IsNetUsed", "没有网络");
			writeLog("没有网络");
		}

		return success;
    }

	private Handler mHandler;
	private PushPreferenceManagement mPushPreferenceManagement;
	private DrPalmPush mBinder = new DrPalmPush();
	public class DrPalmPush extends IDrPalmPush.Stub{
		@Override
		public boolean RegAppPush(PushRegItem regItem,
				IDrPalmPushCallback listen) throws RemoteException{
			String appid = regItem.packageName;//getAppid(regItem.packageName);
			Log.d("DrPalmPushService.RegAppPush:", "packageName:" + appid);
			writeLog("[" + regItem.packageName + "]应用向推送服务(AIDL)注册");

			List<PushRegItem> itemList = mPushPreferenceManagement.loadPushSetting();
			boolean isExist = false;
			for(PushRegItem item: itemList){
				if(item.packageName.equals(regItem.packageName)){
					isExist = true;
					item.appver = regItem.appver;
					item.SchoolID = regItem.SchoolID;
					item.SchoolKey = regItem.SchoolKey;
					if(regItem.SchoolKey.length() > 0)
						item.SchoolKey = regItem.SchoolKey;
					writeLog("[" + item.packageName + "]RegAppPush:" + item.SchoolKey);
					break;
				}
			}
			if(!isExist){
				itemList.add(regItem);
			}
			mPushPreferenceManagement.saveSetting(itemList);
			Message message = Message.obtain();
			message.arg1 = RESTART_PUSH;
			mHandler.sendMessage(message);

			listen.onSuccess();
			return false;
		}

	}

	////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 电源锁
	 */
//	private WakeLock mWakeLock;
//	private void acquireWakeLock() {
//		if (null == mWakeLock) {
//			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
//					| PowerManager.ON_AFTER_RELEASE, "DrPalmPushService");
//			if (null != mWakeLock) {
//				mWakeLock.acquire();
//			}
//		}
//	}
//	private void releaseWakeLock() {
//		if (null != mWakeLock) {
//		mWakeLock.release();
//		mWakeLock = null;
//		}
//	}
	private void reStartGuideService() {
		Log.d("DrPalmPushService.reStartGuideService","Enter");
		Intent intentGuideService = new Intent(this, DrPalmGuideService.class);
		getApplicationContext().startService(intentGuideService);
	}
	////////////////////////////////////////////////////////////////////////////////////
	public Context mContext = this;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("DrPalmPushService.onBind","Enter");
		return mBinder;		//不能返回为空,否则绑定不了服务
//		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
	   Log.d("DrPalmPushService.onStartCommand", "flags:" + String.valueOf(flags));
	   return START_STICKY;
	}
	@Override
	public void onDestroy(){
		Log.d("DrPalmPushService.onDestroy", "Enter");
		//释放设备电源锁
//		releaseWakeLock();
		unregisterReceiver(mGroupReceiver);
		writeLog("推送服务被销毁");
	}
	@Override
	public void onCreate(){
		Log.d("DrPalmPushService.onCreate","Enter");
		// 清除日志
		clearInvalidLog();
		writeLog("创建推送服务");
		// 设置闹铃
		setAlarmTime(this, INTERVAL);
		// 申请设备电源锁
//		acquireWakeLock();
		// 获取设备串号，没有串号则用网卡mac地址
		DeviceUuidFactory duf = new DeviceUuidFactory(mContext);
//		mImeistring = duf.getDeviceUuid().toString().replace("-", "");//GlobalVariables.getDeviceId();
//		mImeistring = DeviceUuidFactory.getDeviceId2222(mContext);
		mImeistring = duf.getDeviceMac();
		// 初始化读写文件
		mPushPreferenceManagement = PushPreferenceManagement.getInstance(this);
		// 获取协议接口(暂时只针对一个应用,要支持多个应用需要修改)
		mPushRequest = new PushRequest(PUSHCENTER);
		// 初始化广播接收器
		initReceiver();
		// 消息处理队列
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.arg1 == RECEIVE_PUSH_MESSAGE){
//					MessageObject obj = (MessageObject)msg.obj;
//					String strAction = obj.pushRegItem.packageName + PushActionDefine.PUSH_GETMESSAGE_ACTION;
//					Intent intent = new Intent(strAction);
//					intent.putExtra(PushActionDefine.PUSH_MESSAGE, obj.pushMessageItem);
//					sendBroadcast(intent);
					
					MessageJsonObject obj = (MessageJsonObject)msg.obj;
					String strAction = obj.pushRegItem.packageName + PushActionDefine.PUSH_GETMESSAGE_ACTION;
					Intent intent = new Intent(strAction);
					intent.putExtra(PushActionDefine.PUSH_MESSAGE, obj.pushMessageStr);
					sendBroadcast(intent);
				}
				else if (msg.arg1 == UPGRADE_APP) {
					UpgradeObject obj = (UpgradeObject)msg.obj;
					writeLog("收到升级版本:" + obj.pushUpgradeAppItem.version + " 连接:" + obj.pushUpgradeAppItem.url);
					String strAction = obj.pushRegItem.packageName + PushActionDefine.PUSH_UPGRADEAPP_ACTION;
					Intent intent = new Intent(strAction);
					intent.putExtra(PushActionDefine.PUSH_UPGRADE, obj.pushUpgradeAppItem);
					sendBroadcast(intent);
				}
				else if (msg.arg1 == RESTART_PUSH) {
					synchronized(mSetpLock) {
						mCurentStep = RegisterStep.STOPSETP;
					}
					if(StopGetMessage()) {
						// 手动断开连接不需要回调
						writeLog("主动断开旧推送连接");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// 能检测到网络状态是断网,不需要重连
					if(isNeedToRestart())
						restartPush();
				}
				else if (msg.arg1 == REG_TOKEN) {
					PushRegItem item = (PushRegItem)msg.obj;
					if(!RegPushToken(item)) {
						writeLog("[" + item.packageName +"]发送注册Token请求失败");
					}
				}
				else if (msg.arg1 == START_GET_PUSH) {
					PushRegItem item = (PushRegItem)msg.obj;
					if(!StartGetPushMessage(item)) {
						writeLog("[" + item.packageName +"]发送开始接收推送请求失败");
					}
				}
				super.handleMessage(msg);
			}
		};

		/*
		 *  状态检测线程
		 *  每30秒检测一次需不需要重新连接推送服务器
		 *  需要重连情况:网络打开,但与推送服务器连接已经断开
		 */
		Thread checkOnlineThread = new Thread(){
			@Override
			public void run() {
				while(!mIsDestoryService){
					Log.d("DrPalmPushService.CheckOnlineThread", "");
					if(isNeedToRestart()){
						// 只是负责显示当前网络状态，不做逻辑处理
						// restartPush();
					}
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Log.d("DrPalmPushService:CheckOnlineThread", "stop");
			}
		};
		checkOnlineThread.start();
	}
	@Override
	public void onStart(Intent intent, int startId){
		Log.d("DrPalmPushService:onStart : id = ", String.valueOf(startId));
	}
	private boolean isNeedToRestart(){
		return IsNetUsed(this.getApplicationContext());
	}
	/*
	 * 重新读取服务的配置文件, 更新需要连接推送服务器的应用队列
	 */
	//private HashMap<String, PushRegItem> appMap = new HashMap<String, PushRegItem>();
	private void restartPush(){
		Log.d("DrPalmPushService:restartPush", "enter");
		writeLog("正在尝试重新启动所有应用推送连接,服务版本(" + PUSH_SERVICE_VERSION + ")");
		List<PushRegItem> itemList = mPushPreferenceManagement.loadPushSetting();
		for(PushRegItem item : itemList){
			writeLog("重新启动[" + item.packageName +"]推送连接...");
			if(item.SchoolKey.length() > 0 ){
				// 重新获取挑战码
				if(!GetPushChallenge(item)){
					writeLog("[" + item.packageName +"]GetPushChallenge线程启动失败");
				}
			}
			else {
				writeLog("[" + item.packageName +"]" + "SchoolKey为空");
			}
		}
	}
	/*
	 * 获取挑战码
	 * PushRegItem: 注册信息
	 * tokenid: 应用token
	 */
	private boolean GetPushChallenge(final PushRegItem item){
		Log.d("DrPalmPushService:GetPushChallenge", "packageName:[" + item.packageName + "]");
		writeLog("[" + item.packageName +"]" + "向服务器获取挑战值...");
		synchronized(mSetpLock) {
			mCurentStep = RegisterStep.GETCHALLENGE;
		}
		return mPushRequest.GetPushChallenge(item.packageName, getmIdentifly(), new PushCallback(){
			@Override
			public void onError(String err) {
				Log.d("DrPalmPushService.GetPushChallenge", "Error:" + err);
				writeLog("[" + item.packageName +"]" + "推送连接已断开:" + err);
				try {
					Thread.sleep(mReconnectTime);
					mReconnectTime += RECONNECT_TIME;
					mReconnectTime = Math.min(mReconnectTime, RECONNECT_MAX_TIME);
					Log.d("DrPalmPushService.GetPushChallenge.OnError", "mReconnectTime:" + String.valueOf(mReconnectTime));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized(mSetpLock) {
					if(mCurentStep == RegisterStep.GETCHALLENGE) {
						Message message = Message.obtain();
						message.arg1 = RESTART_PUSH;
						mHandler.sendMessage(message);
					}
				}
			}
			@Override
			public void onSuccess(PushParse parse) {
				synchronized(mSetpLock) {
					mCurentStep = RegisterStep.GETCHALLENGE_SUC;
				}
				mChallenge = parse.parseChallenge();
				item.mChallenge = mChallenge;
				Message message = Message.obtain();
				message.arg1 = REG_TOKEN;
				message.obj = item;
				mHandler.sendMessage(message);
				//RegPushToken(item);
			}
		});
		//return false;
	}
	/*
	 * 向服务器注册应用
	 * PushRegItem: 注册信息
	 * tokenid: 应用token
	 */
	private boolean RegPushToken(final PushRegItem item){
		Log.d("DrPalmPushService.RegPushToken", "packageName:[" + item.packageName +"]");

		synchronized(mSetpLock) {
			if(mCurentStep == RegisterStep.GETCHALLENGE_SUC) {
				mCurentStep = RegisterStep.REGTOKEN;
				writeLog("[" + item.packageName +"]" + "向服务器注册TOKEN...");
			}
			else {
				//writeLog("[" + item.packageName +"]" + "注册步骤中断(RegPushToken)");
				return false;
			}
		}
		String model = android.os.Build.MODEL;
		String system = "Android " + android.os.Build.VERSION.RELEASE;//android.os.Build.VERSION.SDK;
		String appver = item.appver;
		return mPushRequest.RegPushToken(mChallenge, item.SchoolID, item.SchoolKey, item.packageName, getmIdentifly(), model, system, appver, new PushCallback(){
			@Override
			public void onError(String err) {
				Log.d("DrPalmPushService.RegPushToken", "Error:" + err);
				writeLog("[" + item.packageName +"]" + "推送连接已断开:" + err);
				//mIsNeedToRestart = true;
				try {
					Thread.sleep(mReconnectTime);
					mReconnectTime += RECONNECT_TIME;
					mReconnectTime = Math.min(mReconnectTime, RECONNECT_MAX_TIME);
					Log.d("DrPalmPushService.RegPushToken.OnError", "mReconnectTime:" + String.valueOf(mReconnectTime));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized(mSetpLock) {
					if(mCurentStep == RegisterStep.REGTOKEN) {
						Message message = Message.obtain();
						message.arg1 = RESTART_PUSH;
						mHandler.sendMessage(message);
					}
				}
			}
			@Override
			public void onSuccess(PushParse parse) {
				// TODO Auto-generated method stub
				writeLog("[" + item.packageName +"]" + "注册成功");
				PushUpgradeAppItem upgradeItem = new PushUpgradeAppItem(parse.parseUpgradeVer(),
						parse.parseUpgradeUrl(),
						parse.parseUpgradeDes());
				if(upgradeItem.url.length() > 0) {
					Message message = Message.obtain();
					message.arg1 = UPGRADE_APP;
					message.obj = new UpgradeObject(upgradeItem, item);
					mHandler.sendMessage(message);
				}
				synchronized(mSetpLock) {
					mCurentStep = RegisterStep.REGTOKEN_SUC;
				}
				Message message = Message.obtain();
				message.arg1 = START_GET_PUSH;
				message.obj = item;
				mHandler.sendMessage(message);
				//StartGetPushMessage(item);
			}
		});
		//return false;
	}
	/*
	 * 向服务器申请接收推送
	 * PushRegItem: 注册信息
	 */
	private boolean StartGetPushMessage(final PushRegItem item){
		Log.d("DrPalmPushService:StartGetPushMessage", "packageName:[" + item.packageName + "]");
		synchronized(mSetpLock) {
			if(mCurentStep == RegisterStep.REGTOKEN_SUC) {
				mCurentStep = RegisterStep.START_GET_PUSH;
				writeLog("[" + item.packageName +"]" + "开始接受服务器推送...");
			}
			else {
				//writeLog("[" + item.packageName +"]" + "注册步骤中断(StartGetPushMessage)");
				return false;
			}
		}
		return mPushRequest.StartGetPushMessage(new PushMsgCallback(){
			@Override
			public void onError(String err) {
				Log.d("DrPalmPushService.StartGetPushMessage", "Error:" + err);
				writeLog("[" + item.packageName +"]" + "推送连接已断开:" + err);
				//mIsNeedToRestart = true;
				try {
					Thread.sleep(mReconnectTime);
					mReconnectTime += RECONNECT_TIME;
					mReconnectTime = Math.min(mReconnectTime, RECONNECT_MAX_TIME);
					Log.d("DrPalmPushService.StartGetPushMessage.OnError", "mReconnectTime:" + String.valueOf(mReconnectTime));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized(mSetpLock) {
					if(mCurentStep == RegisterStep.START_GET_PUSH) {
						Message message = Message.obtain();
						message.arg1 = RESTART_PUSH;
						mHandler.sendMessage(message);
					}
				}

			}
			@Override
			public void onSuccess(String jsonstr) {
				// TODO Auto-generated method stub
//				PushMessageItem pushMessage = parse.parseMessageBody();
//				Message message = Message.obtain();
//				message.arg1 = RECEIVE_PUSH_MESSAGE;
//				message.obj = new MessageObject(pushMessage, item);
//				mHandler.sendMessage(message);
//				String msgLog = "收到推送消息:" + item.packageName + "[" + pushMessage.alert + "]";
//				writeLog(msgLog);
				
				Message message = Message.obtain();
				message.arg1 = RECEIVE_PUSH_MESSAGE;
				message.obj = new MessageJsonObject(jsonstr, item);
				mHandler.sendMessage(message);
				String msgLog = "收到推送消息:" + item.packageName + "[" + jsonstr + "]";
				writeLog(msgLog);
			}
		});
		//return false;
	}
	/*
	 * 断开推送连接
	 */
	private boolean StopGetMessage(){
		boolean bFlag = mPushRequest.StopGetMessage();
		Log.d("DrPalmPushService.StopGetMessage", String.valueOf(bFlag));
		return bFlag;
	}
	/*
	 * 断开推送连接
	 */
	private boolean StopGetMessage(PushRegItem item){
		boolean bFlag = item.mPushRequest.StopGetMessage();
		Log.d("DrPalmPushService.StopGetMessage", "packName:[" +item.packageName + "]flag:" + String.valueOf(bFlag));
		return bFlag;
	}
	/*
	 * 获取应用唯一标识
	 */
	private String getAppid(String packageName){
		//String strIndetify = mImeistring + packageName;
		//String appid = Encryption.toMd5(packageName);
		return packageName;
	}
	/*
	 * 获取手机唯一标识
	 */
	private String getmIdentifly(){
		return mImeistring;
	}

	////////////////////////////////////////////////////////////////////////////////////
	/*
	 *  输出日志
	 */
	static final String PUSH_LOG_SAVE_PATH = "/sdcard/DrpalmPushLog/";
	static final String PUSH_LOG_NAME = "PushLog";
	static final long TIME_FOR_CLEAN_PUSH_LOG = 10*24*3600*1000;

	synchronized private void writeLog(String strLog) {
		//DrServiceLog.clearInvalidLog(PUSH_LOG_SAVE_PATH, TIME_FOR_CLEAN_PUSH_LOG);
		DrServiceLog.writeLog(PUSH_LOG_SAVE_PATH, PUSH_LOG_NAME, strLog);
	}
	/*
	 * 清除旧日志
	 * 默认保存10天的日志
	 * day: 保存天数
	 */
	synchronized private void clearInvalidLog() {
		DrServiceLog.clearInvalidLog(PUSH_LOG_SAVE_PATH, TIME_FOR_CLEAN_PUSH_LOG);
	}
	////////////////////////////////////////////////////////////////////////////////////
}
