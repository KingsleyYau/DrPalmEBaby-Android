package com.drcom.drpalm.objs;

import android.os.Parcel;
import android.os.Parcelable;

public class PushMessageItem implements Parcelable{
	public String alert = "";
	public int badge = 0;
	public String sound = "";	
	public int etype = 0;
	
	public PushMessageItem(){
		
	}
	public PushMessageItem(String packageName,int badge,String sound,int etype){
		this.alert = packageName ;
		this.badge = badge ;
		this.sound = sound ;
		this.etype = etype;
	}
	private void readFromParcel(Parcel in) {
		alert = in.readString();
		badge = in.readInt();
		sound = in.readString();		
		etype = in.readInt();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(alert);	
		dest.writeInt(badge);		
		dest.writeString(sound);	
		dest.writeInt(etype);
	}
	
	public static final Parcelable.Creator<PushMessageItem> CREATOR  
	= new Parcelable.Creator<PushMessageItem>(){
		@Override
		public PushMessageItem createFromParcel(Parcel source){
			PushMessageItem item = new PushMessageItem(); 
			item.readFromParcel(source);
			return item;
		}			
		public PushMessageItem[] newArray(int size){  
	        return new PushMessageItem[size];  
	    } 
	}; 
}
