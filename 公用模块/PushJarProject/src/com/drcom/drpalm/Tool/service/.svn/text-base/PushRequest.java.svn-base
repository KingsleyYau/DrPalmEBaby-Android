package com.drcom.drpalm.Tool.service;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import org.json.JSONException;

import android.util.Log;

import com.drcom.drpalm.Tool.service.DrPushServiceJni;
import com.drcom.drpalm.Tool.service.DrServiceJniCallback2;


public class PushRequest{
	private final String ERRORSTRING = "Connect error";
	
	protected String mDomain = "";

	protected DrPushServiceJni mDrPushServiceJni = new DrPushServiceJni();
	public PushRequest(){
		mDrPushServiceJni.NativeInit();
	}
	public PushRequest(String url){
		mDomain = url;
		mDrPushServiceJni.NativeInit();
	}
	public boolean GetPushChallenge(String packageName, String indetify, final PushCallback callback){
		DrServiceJniCallback2 jnicallback = new DrServiceJniCallback2(){
			@Override
			public void onError(byte[] data) {
				// TODO Auto-generated method stub
				callback.onError(ERRORSTRING);
			}
			@Override
			public void onSuccess(byte[] data) {
				try{
					RequestParse parse = new RequestParse(new String(data));
					HashMap<String, Object> map = parse.getHashMap();
					PushParse pushParse = new PushParse(map);
					if(pushParse.parseOperate()){
						callback.onSuccess(pushParse);
					}
					else{
						String err = pushParse.parseErrorCode();
						callback.onError(err);
					}
				}catch(JSONException e){
					callback.onError("JSONException");
				}
			}
			@Override
			public void onReceiveData(byte[] value) {
				// TODO Auto-generated method stub

			}
		};
		return mDrPushServiceJni.GetPushChallenge(mDomain, packageName, indetify, jnicallback);
	}
	public boolean RegPushToken(String challenge, String schoolid, String schoolKey,
			String packagename, String indetify, String model, String system,
			String appver, final PushCallback callback){
		Log.d("zjj", "indetify:" + indetify + ",packagename:" + packagename);
		
		DrServiceJniCallback2 jnicallback = new DrServiceJniCallback2(){
			@Override
			public void onError(byte[] data) {
				// TODO Auto-generated method stub
				callback.onError(ERRORSTRING);
			}
			@Override
			public void onSuccess(byte[] data) {
				try{
					RequestParse parse = new RequestParse(new String(data));
					HashMap<String, Object> map = parse.getHashMap();
					PushParse pushParse = new PushParse(map);
					if(pushParse.parseOperate()){
						callback.onSuccess(pushParse);
					}
					else{
						String err = pushParse.parseErrorCode();
						callback.onError(err);
					}
				}catch(JSONException e){
					callback.onError("JSONException");
				}
			}
			@Override
			public void onReceiveData(byte[] value) {
				// TODO Auto-generated method stub

			}
		};
		return mDrPushServiceJni.RegPushToken(mDomain, challenge, schoolid, schoolKey, packagename, indetify, model, system, appver, jnicallback);
	}
	public boolean StopGetMessage(){
		return mDrPushServiceJni.StopUrlConnection(mDomain);
	}
	public boolean StartGetPushMessage(final PushCallback callback){
		DrServicePushJniCallback jnipushcallback = new DrServicePushJniCallback() {

			@Override
			public void onSuccess(byte[] value) {
				// TODO Auto-generated method stub
				//callback.onError("Collection has been closed.");
			}

			@Override
			public void onReceivePushData(byte[] value) {
				try {
					String strJason;
					strJason = new String(value, "UTF-8");
					if("" != strJason){
						RequestParse parse = new RequestParse(strJason);
						HashMap<String, Object> map = parse.getHashMap();
						PushParse pushParse = new PushParse(map);
						callback.onSuccess(pushParse);
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onReceiveData(byte[] value) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(byte[] value) {
				// TODO Auto-generated method stub
//				callback.onError(new String(value));
				callback.onError(ERRORSTRING);
			}
		};
		return mDrPushServiceJni.StartGetPushMessage(mDomain, jnipushcallback);
	}
	
	public boolean StartGetPushMessage(final PushMsgCallback callback){
		DrServicePushJniCallback jnipushcallback = new DrServicePushJniCallback() {

			@Override
			public void onSuccess(byte[] value) {
				// TODO Auto-generated method stub
				//callback.onError("Collection has been closed.");
			}

			@Override
			public void onReceivePushData(byte[] value) {
				try {
					String strJason;
					strJason = new String(value, "UTF-8");
					if("" != strJason){
//						RequestParse parse = new RequestParse(strJason);
//						HashMap<String, Object> map = parse.getHashMap();
//						PushParse pushParse = new PushParse(map);
						callback.onSuccess(strJason);
					}
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
				}catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onReceiveData(byte[] value) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onError(byte[] value) {
				// TODO Auto-generated method stub
//				callback.onError(new String(value));
				callback.onError(ERRORSTRING);
			}
		};
		return mDrPushServiceJni.StartGetPushMessage(mDomain, jnipushcallback);
	}

}
