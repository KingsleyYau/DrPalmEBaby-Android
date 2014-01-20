package com.drcom.drpalm.Tool.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestParse extends DealErrorRequestParse {	
	//String mString = null;
	InputStream mStream = null;
	HashMap<String,Object> mHashMap = new HashMap<String,Object>();
	ArrayList<HashMap<String,Object>> mArrayList = new ArrayList<HashMap<String,Object>>();
	
	public RequestParse(byte[] data)
	{
		super(data);
	}
	
	public RequestParse(String string)
	{
		super(string);
	}
//	
//	public RequestParse(String string){
//		mString = string;
//	}
//	public RequestParse(InputStream stream){
//		mStream = stream;
//	}
	public InputStream getStream(){		
		return new ByteArrayInputStream(mString.getBytes());
	}
	
	public ArrayList<HashMap<String,Object>> getArrayList(){
		mArrayList.clear();
		JSONArray array = null;
		try{
			if(null != mString){
				array = convertStringToJSONArray(mString);
			}
			else if(null != mStream) {
				array = convertStreamToJSONArray(mStream);		
			}
			if(null != array){
				TransformJSONArray2List(mArrayList,array);
			}
		}	
		catch(JSONException e){
		}		
		return mArrayList;
	}	
	public HashMap<String,Object> getHashMap() throws JSONException{
		mHashMap.clear();
		JSONObject object = null;
		try{
			if(null != mString){
				object = convertStringToJSONObejct(mString);
			}
			else if(null != mStream) {
				object = convertStreamToJSONObejct(mStream);			
			}		
			if(null != object){
				TransformJSONObject2Map(mHashMap,object);
			}		
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return mHashMap;
	}
	
	private JSONArray convertStringToJSONArray(String string) throws JSONException{
		return new JSONArray(string);
	}
	private JSONArray convertStreamToJSONArray(InputStream stream) throws JSONException{
		return convertStringToJSONArray(RequestDefine.convertStreamToString(stream));
	}
	private JSONObject convertStringToJSONObejct(String string) throws JSONException{
		return new JSONObject(string);
	}	
	private JSONObject convertStreamToJSONObejct(InputStream stream) throws JSONException{
		return convertStringToJSONObejct(RequestDefine.convertStreamToString(stream));
	}	

	private void TransformJSONObject2Map(HashMap<String,Object> map, JSONObject object) throws JSONException{
		JSONArray array = object.names();
		for(int i = 0; i< array.length();i++){
	
				String strKey = array.getString(i);
				// JSONObject
				if(null != object.optJSONObject(strKey)){					
					JSONObject obj = object.optJSONObject(strKey);	
					HashMap<String,Object> subMap = new HashMap<String,Object>();
					TransformJSONObject2Map(subMap,obj);	
					map.put(strKey,subMap);
				}
				else if (null != object.optJSONArray(strKey)){
					JSONArray ary = object.optJSONArray(strKey);
					ArrayList<HashMap<String,Object>> subArray= new ArrayList<HashMap<String,Object>>();
					TransformJSONArray2List(subArray,ary);
					map.put(strKey,subArray);
				}
				else{
					map.put(strKey,object.getString(array.getString(i)));
				}
			

			 
		}
	}
	private void TransformJSONArray2List(ArrayList<HashMap<String,Object>> list,JSONArray array){
		for(int i = 0; i< array.length();i++){
			try{		
				// JSONObject
				if(null != array.optJSONObject(i)){
					JSONObject obj = array.optJSONObject(i);	
					HashMap<String,Object> subMap = new HashMap<String,Object>();
					TransformJSONObject2Map(subMap,obj);
					list.add(subMap);
				}
				// JSONArray
				else if (null != array.optJSONArray(i)){
					JSONArray ary = array.getJSONArray(i);
					ArrayList<HashMap<String,Object>> subArray= new ArrayList<HashMap<String,Object>>();
					TransformJSONArray2List(subArray,ary);
				}				
			}
			catch(JSONException e){
				
			}
		}
	}
}
