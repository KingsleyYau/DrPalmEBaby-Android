package com.drcom.drpalm.Tool.service;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.drcom.drpalm.objs.PushRegItem;


//import android.R.integer;

public class PushRegItemHandler extends DefaultHandler{

	private String mCurrentData = "";
	private PushRegItem  mPushRegItem;
	private List<PushRegItem>  mPushRegList;	
	public final List<PushRegItem> getParsedData() { 
        return mPushRegList; 
    }
	static final String PUSH_REG_LIST = "PushRegList";
	static final String PUSH_REG_ITEM = "PushRegItem";
	static final String PUSH_APP_PACKAGENAME = "AppPackageName";
	static final String PUSH_SCHOOLID = "SchoolID";	
	static final String PUSH_SCHOOLKEY = "SchoolKey";
	static final String PUSH_APPVERSION = "AppVersion";
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {	
		String data = new String(ch,start,length);
		mCurrentData = data;
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		//super.endElement(uri, localName, qName);
		// TODO Auto-generated method stub
		if(localName.endsWith(PUSH_APP_PACKAGENAME)){
			mPushRegItem.packageName = mCurrentData;	
		}
		else if(localName.endsWith(PUSH_SCHOOLID))
		{
			mPushRegItem.SchoolID = mCurrentData;			
		}
		else if(localName.endsWith(PUSH_SCHOOLKEY)){
			mPushRegItem.SchoolKey = mCurrentData;
		}
		else if(localName.endsWith(PUSH_APPVERSION)){
			mPushRegItem.appver = mCurrentData;
		}
		else if(localName.endsWith(PUSH_SCHOOLKEY)){
			mPushRegItem.SchoolKey = mCurrentData;
		}
		else if(localName.endsWith(PUSH_REG_ITEM)){
			mPushRegList.add(mPushRegItem);
			mPushRegItem = new PushRegItem();
		}
		mCurrentData = "";
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		mPushRegItem = new PushRegItem();
		mPushRegList = new ArrayList<PushRegItem>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		//this.localName = localName;
	}
	

}
