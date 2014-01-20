package com.drcom.drpalm.Tool.service;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManagement {
	public static final int    CURRESOURCEVERSION_DEFAULT = 0;
	public static final int    CURRESOURCEVERSIN_DEMO = 1;
	public static final int    CURRESOURCEVERSION_CUSTOMIZE = 2;
	
	static final String NEWS_PREFERENCES_FILE = "NewsPreferencesFile";
	static final String EVENTS_PREFERENCES_FILE = "EventsPreferencesFile";	
	static final String LAST_CATEGORY_CLEARED_KEY_PREFIX = "LastCategoryClearedDate";
	static final String LAST_UPDATE_COUNT_KEY_PREFIX = "LastUpdateCount";
	
	// resource version file
	static final String COMMON_PREFERENCES_FILE = "CommonPreferencesFile";
	static final String APPLICATION_VERSION = "ApplicationVersion";
	static final String RESOURCE_VERSION = "ResourceVersion";
	static final String LASTGET_SCHOOLEKY = "LastGetSchoolKey";
	static final String LASTUPDATE_RESOURCE_DATE = "LastupdateResourceDate";
	static final String CUSTOMIZE_LASTUPDATE_RESOURCE_DATE = "CustomizeLastUpdateResourceDate";
	
	static final String CURRES_ISCUSTOMIZE  = "CurrentResourceIsCustomize";
	static final String CURRES_VERSION = "CurrentResourceVersion";
	static final String RESOURCE_VERSION_DEMO =  "DemoResourceVersion";
	static final String RESOURCE_VERSION_CUSTOMIZE=  "CustomizeResourceVersion";
	static final String RESOURCE_FILENAME_DEMO =  "DemoResourceFileName";
	static final String RESOURCE_FILENAME_CUSTOMIZE=  "CustomizeResourceFileName";
	static final String PROFILEKEY   = "ProfileKey";
	static final String CUSTOMIZE_SCHOOLKEY = "CustomizeSchoolKey";
	static final String DEMOVERSION_SCHOOLKEY = "DemoVersionSchoolKey";
	static final String DEMO_RESZIP_ISUPDATE = "DemoResZipIsUpdate";
	
	// push 
	static final String PUSH_PREFERENCES_FILE = "PushPreferencesFile";
	static final String LAST_PUSH_COUNT = "LastPushCount";
	static final String IS_VIRGIN = "IsVirgin";
	
	private SharedPreferences mNewsSharedPreferences;
	private SharedPreferences mEventsSharedPreferences;
	private SharedPreferences mCommonSharedPreferences;
	private SharedPreferences mPushSharedPreferences;
	
	static private PreferenceManagement mPreferenceManagement = null;
	static public PreferenceManagement getInstance(Context context){
		if(null == mPreferenceManagement){
			mPreferenceManagement = new PreferenceManagement(context);			
		}
		return mPreferenceManagement;
	}
	public PreferenceManagement(Context context){
		mNewsSharedPreferences = context.getSharedPreferences(NEWS_PREFERENCES_FILE, Context.MODE_PRIVATE);
		mEventsSharedPreferences = 	context.getSharedPreferences(EVENTS_PREFERENCES_FILE, Context.MODE_PRIVATE);
		mCommonSharedPreferences = context.getSharedPreferences(COMMON_PREFERENCES_FILE, Context.MODE_PRIVATE);
		mPushSharedPreferences = context.getSharedPreferences(COMMON_PREFERENCES_FILE, Context.MODE_PRIVATE);
	}
	public int getLastPushCount(){
		int lastCount = mPushSharedPreferences.getInt(LAST_PUSH_COUNT, 0);
		return lastCount;
	}
	synchronized public void markNotVirgin() {
		Editor editor = mPushSharedPreferences.edit();		
		editor.putBoolean(IS_VIRGIN, false);
		editor.commit();		
	}	
	public boolean getIsVirgin(){
		boolean IsVirgin = mPushSharedPreferences.getBoolean(IS_VIRGIN, true);
		return IsVirgin;
	}
	synchronized public void markLastCountOfPush(int count) {
		Editor editor = mPushSharedPreferences.edit();		
		editor.putInt(LAST_PUSH_COUNT, count);
		editor.commit();		
	}
	public int getCountLastNewsLoaded(){
		int lastCount = mNewsSharedPreferences.getInt(LAST_UPDATE_COUNT_KEY_PREFIX, 0);
		return lastCount;
	}	
	public boolean isNewsRefresh() {
		long lastClearTime = mNewsSharedPreferences.getLong(LAST_CATEGORY_CLEARED_KEY_PREFIX, -1);
		if(lastClearTime < 0) {
			return false;
		}		
		return true;		
	}
	public Date getNewsLastLoaded() {
		long lastClearTime = mNewsSharedPreferences.getLong(LAST_CATEGORY_CLEARED_KEY_PREFIX, -1);
		if(lastClearTime > 0) {
			return new Date(lastClearTime);
		} else {
			return null;
		}	
	}
	synchronized public void clearAllNews() {		
		Editor editor = mNewsSharedPreferences.edit();
		editor.putLong(LAST_CATEGORY_CLEARED_KEY_PREFIX, -1);
		editor.commit();
	}
	synchronized public void markNewsCategoryAsFresh(){
		markCategoryAsFresh(mNewsSharedPreferences);
	}	
	synchronized public void markNewsCountOfFresh(int count) {
		markCountOfFresh(mNewsSharedPreferences,count);
	}
	
	public int getCountLastEventsLoaded(){
		int lastCount = mEventsSharedPreferences.getInt(LAST_UPDATE_COUNT_KEY_PREFIX, 0);
		return lastCount;
	}	
	public Date getEventsLastLoaded() {
		long lastClearTime = mEventsSharedPreferences.getLong(LAST_CATEGORY_CLEARED_KEY_PREFIX, -1);
		if(lastClearTime > 0) {
			return new Date(lastClearTime);
		} else {
			return null;
		}	
	}
	public boolean isEventsRefresh() {
		long lastClearTime = mEventsSharedPreferences.getLong(LAST_CATEGORY_CLEARED_KEY_PREFIX, -1);
		if(lastClearTime < 0) {
			return false;
		}		
		return true;		
	}	
	synchronized public void markEventsCategoryAsFresh(){
		markCategoryAsFresh(mEventsSharedPreferences);
	}
	synchronized public void  markEventsCountOfFresh(int count) {
		markCountOfFresh(mEventsSharedPreferences,count);
	}
	synchronized private void markCategoryAsFresh(SharedPreferences sharePre) {
		long currentTime = System.currentTimeMillis();
		Editor editor = sharePre.edit();
		editor.putLong(LAST_CATEGORY_CLEARED_KEY_PREFIX, currentTime);
		editor.commit();
	}
	synchronized private void markCountOfFresh(SharedPreferences sharePre,int count) {
		Editor editor = sharePre.edit();		
		editor.putInt(LAST_UPDATE_COUNT_KEY_PREFIX, count);
		editor.commit();
	}
	
	
	
	
	//add by JiangBo 2012-06-26 
	//保存当前使用资源包版本
	synchronized public boolean markCurResVersion(int version){
		Editor editor = mCommonSharedPreferences.edit();	
		editor.putInt(CURRES_VERSION, version);
		return editor.commit();
	}	
	
	synchronized public int getCurResVersion(){
		return mCommonSharedPreferences.getInt(CURRES_VERSION, CURRESOURCEVERSION_DEFAULT);
	}	
	//获取当前使用资源包是否为Demo版
	synchronized public boolean getCurResIsCustomizeVersion(){
		int version = getCurResVersion();
		if(getCurResVersion() == CURRESOURCEVERSION_CUSTOMIZE)
			return true;
		else 
			return false;
	}	
	
	//保存当前使用资源包版本号
	synchronized public boolean markResourceVersion(String version){
		Editor editor = mCommonSharedPreferences.edit();	
		if(!getCurResIsCustomizeVersion())
			editor.putString(RESOURCE_VERSION_DEMO, version);
		else
			editor.putString(RESOURCE_VERSION_CUSTOMIZE, version);
		return editor.commit();
	}	
	//获取当前使用资源包版本号
	synchronized public String getResourceVersion(){
		if(!getCurResIsCustomizeVersion())
		{
			return mCommonSharedPreferences.getString(RESOURCE_VERSION_DEMO, "");
		}
		else
		{
			return mCommonSharedPreferences.getString(RESOURCE_VERSION_CUSTOMIZE, "");
		}
	}	
	
	//保存当前使用资源包文件名称
	synchronized public boolean markResourceFileName(String filename){
		Editor editor = mCommonSharedPreferences.edit();	
		if(!getCurResIsCustomizeVersion())
			editor.putString(RESOURCE_FILENAME_DEMO, filename);
		else
			editor.putString(RESOURCE_FILENAME_CUSTOMIZE, filename);
		return editor.commit();
	}	
	//获取当前使用资源包文件名称
	synchronized public String getResourceFileName(){
		if(!getCurResIsCustomizeVersion())
		{
			return mCommonSharedPreferences.getString(RESOURCE_FILENAME_DEMO, "");
		}
		else
		{
			return mCommonSharedPreferences.getString(RESOURCE_FILENAME_CUSTOMIZE, "");
		}
	}	
	
	//保存ProfileKey(输入的8位Key)
	synchronized public boolean markProfileKey(String key){
		//获取上次key,并将改key对应的上次资源包时间清除 
		String lastkey = mCommonSharedPreferences.getString(PROFILEKEY, "");
		if(!lastkey.contentEquals(key) &&
				lastkey.length()>0)
		{
			Date date = new Date(0);
			markCustomizeLastUpdate(lastkey, date);
			//markLastUpdate(lastkey, date);
		}
		Editor editor = mCommonSharedPreferences.edit();	
		editor.putString(PROFILEKEY, key);
		return editor.commit();
	}	
	//获取ProfileKey
	synchronized public String getProfileKey(){
		return mCommonSharedPreferences.getString(PROFILEKEY, "");
	}	
	
	//保存定制版对应的SchoolKey
	synchronized public boolean markCustomizeSchoolKey(String key){
		Editor editor = mCommonSharedPreferences.edit();	
		editor.putString(CUSTOMIZE_SCHOOLKEY, key);
		return editor.commit();
	}	
	//获取定制版SchoolKey
	synchronized public String getCustomizeSchoolKey(){
		return mCommonSharedPreferences.getString(CUSTOMIZE_SCHOOLKEY, "");
	}
	
	//保存演示版对应的SchoolKey
	synchronized public boolean markDemoVersionSchoolKey(String key){
		Editor editor = mCommonSharedPreferences.edit();	
		editor.putString(DEMOVERSION_SCHOOLKEY, key);
		return editor.commit();
	}	
	//获取演示版SchoolKey
	synchronized public String getDemoVersionSchoolKey(){
		return mCommonSharedPreferences.getString(DEMOVERSION_SCHOOLKEY, "");
	}
	
	//保存演示版资源是否有更新过
	synchronized public boolean markDemoResZipIsUpdate(boolean isupdate){
		Editor editor = mCommonSharedPreferences.edit();	
		editor.putBoolean(DEMO_RESZIP_ISUPDATE, isupdate);
		return editor.commit();
	}	
	//获取演示版资源是否有更新过
	synchronized public boolean getDemoResZipIsUpdate(){
		return mCommonSharedPreferences.getBoolean(DEMO_RESZIP_ISUPDATE, false);
	}
	
	//获取定制版资源包最后更新时间
	public Date getCustomizeLastUpdate(String profilekey) {		
		long lastClearTime = mCommonSharedPreferences.getLong(CUSTOMIZE_LASTUPDATE_RESOURCE_DATE + "_" + profilekey, 0);
		if(lastClearTime > 0) {
			return new Date(lastClearTime);
		} else {
			return new Date(0);
		}
	}
	//保存定制版资源包最后更新时间
	synchronized public void markCustomizeLastUpdate(String profilekey, Date date) {
		long currentTime = date.getTime();
		Editor editor = mCommonSharedPreferences.edit();		
		editor.putLong(CUSTOMIZE_LASTUPDATE_RESOURCE_DATE + "_" +profilekey, currentTime);
		editor.commit();
	}
	////获取演示版资源包最后更新时间
	public Date getLastUpdate(String schoolKey) {		
		long lastClearTime = mCommonSharedPreferences.getLong(LASTUPDATE_RESOURCE_DATE + "_" + schoolKey, 0);
		if(lastClearTime > 0) {
			return new Date(lastClearTime);
		} else {
			return new Date(0);
		}
	}
	//保存演示版资源包最后更新时间
	synchronized public void markLastUpdate(String schoolKey, Date date) {
		long currentTime = date.getTime();
		Editor editor = mCommonSharedPreferences.edit();		
		editor.putLong(LASTUPDATE_RESOURCE_DATE + "_" +schoolKey, currentTime);
		editor.commit();
	}
	//add end 
	/*
	 * Application and resource version file 
	 */
	synchronized public boolean markCurrentResourceVersion(String version){
		Editor editor = mCommonSharedPreferences.edit();	
		editor.putString(RESOURCE_VERSION, version);
		return editor.commit();
	}	
	 public String getCuurentResourceVersion(){
		return mCommonSharedPreferences.getString(RESOURCE_VERSION, "");
	}
	// seqidkey
	public Integer getLastSchoolKey(){
		return mCommonSharedPreferences.getInt(LASTGET_SCHOOLEKY, -1);
	}
	synchronized public boolean markLastSchoolKey(String schoolKey){
		Editor editor = mCommonSharedPreferences.edit();	
		editor.putString(LASTGET_SCHOOLEKY, schoolKey);
		return editor.commit();
	}
}
