package com.drplam.test;

import com.drcom.drpalm.Tool.PushManager;
import com.drcom.drpalm.Tool.service.ConnectPushCallback;
import com.drcom.drpalm.Tool.service.DrServiceLog;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/**
 * 程序主窗体
 * 注册PUSH服务
 * @author zhaojunjie
 *
 */
public class PushJarDemoActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //初始化PUSH服务Log
		DrServiceLog.getInstance(this);
		//初始化PUSH服务并注册
		PushManager.init(getApplicationContext(),new ConnectPushCallback() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				try {
					PushManager.Register("9532", "a8Ca7rzh7g9532jQiunuFHK6mRAKMhKh");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(String err) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//当需要PUSH服务器要绑定本手机的时候,把Tokenid发送到对应接口即可
        Log.i("pushdemo","本机Tokenid:" + PushManager.getTokenid());
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//解除绑定(没什么作用,push服务是不会终止的)
//    	PushManager.unbindService();
    	super.onDestroy();
    }
}