package com.drcom.drpalm.Tool.service;



public interface PushCallback{
	public void onError(String err);
	public void onSuccess(PushParse parse);
}