package com.drcom.drpalm.objs;

import android.os.Parcel;
import android.os.Parcelable;

public class PushUpgradeAppItem implements Parcelable{
	public String version = "";
	public String url = "";
	public String des = "";

	public PushUpgradeAppItem(){

	}
	public PushUpgradeAppItem(String version,String url,String des){
		this.version = version ;
		this.url = url ;
		this.des = des ;
	}
	private void readFromParcel(Parcel in) {
		version = in.readString();
		url = in.readString();
		des = in.readString();
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(version);
		dest.writeString(url);
		dest.writeString(des);
	}

	public static final Parcelable.Creator<PushUpgradeAppItem> CREATOR
	= new Parcelable.Creator<PushUpgradeAppItem>(){
		@Override
		public PushUpgradeAppItem createFromParcel(Parcel source){
			PushUpgradeAppItem item = new PushUpgradeAppItem();
			item.readFromParcel(source);
			return item;
		}
		public PushUpgradeAppItem[] newArray(int size){
	        return new PushUpgradeAppItem[size];
	    }
	};
}
