package com.drcom.drpalm.objs;

import com.drcom.drpalm.Tool.service.PushRequest;

import android.os.Parcel;
import android.os.Parcelable;


public class PushRegItem implements Parcelable{
	public String packageName = "";
	public String SchoolID = "";
	public String SchoolKey = "";
	public String appver = "";
	
	public String mChallenge = "";
	public PushRequest mPushRequest;
	public PushRegItem(){
		
	}
	public PushRegItem(String packageName,String SchoolID,String SchoolKey,String appver){
		this.packageName = packageName ;
		this.SchoolID = SchoolID ;
		this.SchoolKey = SchoolKey ;
		this.appver = appver;
	}
	private void readFromParcel(Parcel in) {
		packageName = in.readString();
		SchoolID = in.readString();
		SchoolKey = in.readString();		
		appver = in.readString();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(packageName);	
		dest.writeString(SchoolID);		
		dest.writeString(SchoolKey);	
		dest.writeString(appver);
	}
	
	public static final Parcelable.Creator<PushRegItem> CREATOR  
	= new Parcelable.Creator<PushRegItem>(){
		@Override
		public PushRegItem createFromParcel(Parcel source){
			PushRegItem item = new PushRegItem(); 
			item.readFromParcel(source);
			return item;
		}			
		public PushRegItem[] newArray(int size){  
	        return new PushRegItem[size];  
	    } 
	}; 
}
