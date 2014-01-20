package com.drcom.drpalm.Tool.service;



public interface PushMsgCallback{
	public void onError(String err);
	public void onSuccess(String jsonstr);
}