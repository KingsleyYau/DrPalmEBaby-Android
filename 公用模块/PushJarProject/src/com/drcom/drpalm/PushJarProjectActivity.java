package com.drcom.drpalm;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;

import com.drcom.drpalm.Tool.PushManager;
import com.drcom.drpalm.Tool.service.ConnectPushCallback;
import com.drcom.drpalm.Tool.service.DrServiceLog;
import com.push.test.R;

public class PushJarProjectActivity extends Activity {
	
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
		
        
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//解除绑定(没什么作用)
    	PushManager.unbindService();
    	super.onDestroy();
    }
}