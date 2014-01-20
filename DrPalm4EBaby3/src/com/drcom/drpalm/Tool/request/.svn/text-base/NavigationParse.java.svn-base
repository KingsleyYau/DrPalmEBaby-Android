package com.drcom.drpalm.Tool.request;

import java.util.ArrayList;
import java.util.HashMap;

import com.drcom.drpalm.Tool.service.ItemDataTranslate;
import com.drcom.drpalm.objs.NavigationItem;
import com.drcom.drpalm.objs.NavigationListItem;

public class NavigationParse extends BaseParse{
	
	private static final String NAVIGATION_RESULTS = "results";
	private static final String NAVIGATION_LOCALID = "localid";
	private static final String NAVIGATION_PLOCID = "plocid";
	private static final String NAVIGATION_NAME = "name";
	private static final String NAVIGATION_TYPE = "type";
	private static final String NAVIGATION_KEY = "key";
	private static final String NAVIGATION_TITLEURL = "titleurl";
	
	public NavigationParse(HashMap<String, Object> map) {
		super(map);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<NavigationItem> parseNavigation() {
		ArrayList<HashMap<String,Object>> objectList = null;
		ArrayList<NavigationItem> navigationList = new ArrayList<NavigationItem>();
		if(mHashMap.containsKey(NAVIGATION_RESULTS)) {	
			try{
				objectList = (ArrayList<HashMap<String,Object>>)mHashMap.get(NAVIGATION_RESULTS);
				for(int i =0;i<objectList.size();i++){
					HashMap<String,Object> objMap = objectList.get(i);
					NavigationItem navigationItem = new NavigationItem();
					if(objMap.containsKey(NAVIGATION_LOCALID)){
						String value = (String)objMap.get(NAVIGATION_LOCALID);
						navigationItem.point_id = ItemDataTranslate.String2Intger(value);
					}
					if(objMap.containsKey(NAVIGATION_PLOCID)){
						String value = (String)objMap.get(NAVIGATION_PLOCID);
						navigationItem.parent_id = ItemDataTranslate.String2Intger(value);
					}
					if(objMap.containsKey(NAVIGATION_NAME)){
						navigationItem.name = (String)objMap.get(NAVIGATION_NAME);
					}						
					if(objMap.containsKey(NAVIGATION_TYPE)){
						navigationItem.type = (String)objMap.get(NAVIGATION_TYPE);
					}
					if(objMap.containsKey(NAVIGATION_KEY)){
						navigationItem.key = (String)objMap.get(NAVIGATION_KEY);
					}
					if(objMap.containsKey(NAVIGATION_TITLEURL)){
						navigationItem.titleurl = (String)objMap.get(NAVIGATION_TITLEURL);
					}
					navigationList.add(navigationItem);
				}
			}catch(Exception e){

			}	
		}
		return navigationList;
	}
	
	public ArrayList<NavigationListItem> parseNavigationList() {
		ArrayList<HashMap<String,Object>> objectList = null;
		ArrayList<NavigationListItem> navigationList = new ArrayList<NavigationListItem>();
		if(mHashMap.containsKey(NAVIGATION_RESULTS)) {	
			try{
				objectList = (ArrayList<HashMap<String,Object>>)mHashMap.get(NAVIGATION_RESULTS);
				for(int i =0;i<objectList.size();i++){
					HashMap<String,Object> objMap = objectList.get(i);
					NavigationListItem navigationItem = new NavigationListItem();
					if(objMap.containsKey(NAVIGATION_LOCALID)){
						String value = (String)objMap.get(NAVIGATION_LOCALID);
						navigationItem.point_id = ItemDataTranslate.String2Intger(value);
					}						
					if(objMap.containsKey(NAVIGATION_TYPE)){
						navigationItem.type = (String)objMap.get(NAVIGATION_TYPE);
					}
					navigationList.add(navigationItem);
				}
			}catch(Exception e){

			}	
		}
		return navigationList;
	}
}
