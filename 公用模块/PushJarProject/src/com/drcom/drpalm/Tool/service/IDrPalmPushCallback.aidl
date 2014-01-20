package com.drcom.drpalm.Tool.service;
import com.drcom.drpalm.objs.PushmessageItem;
interface IDrPalmPushCallback
{    
	void onError(String err);
	//void onGetPushMessage(in PushMessageItem messageItem);
	void onSuccess();
}