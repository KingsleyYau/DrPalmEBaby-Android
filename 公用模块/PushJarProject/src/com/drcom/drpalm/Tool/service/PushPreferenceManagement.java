package com.drcom.drpalm.Tool.service;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlSerializer;

import com.drcom.drpalm.objs.PushRegItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Xml;


public class PushPreferenceManagement {
	static final String PUSH_PREFERENCES_FILE = "PushPreferencesFile";
	static final String PUSH_REG_LIST_STRING = "PushRegListString";
	static final String PUSH_LAST_COUNT = "PushLastCount";

	private PushRegItemHandler mPushRegItemHandler;
	private SharedPreferences mPushSharedPreferences;
	static private PushPreferenceManagement mPreferenceManagement = null;
	static public PushPreferenceManagement getInstance(Context context){
		if(null == mPreferenceManagement){
			mPreferenceManagement = new PushPreferenceManagement(context);
		}
		return mPreferenceManagement;
	}
	public PushPreferenceManagement(Context context){
		mPushSharedPreferences = context.getSharedPreferences(PUSH_PREFERENCES_FILE, Context.MODE_PRIVATE);
	}

	synchronized public final List<PushRegItem> loadPushSetting(){
		mPushRegItemHandler = new PushRegItemHandler();
		String listString = mPushSharedPreferences.getString(PUSH_REG_LIST_STRING, "");
        StringReader read = new StringReader(listString);
        InputSource mInputSource = new InputSource(read);
        try {
        	SAXParserFactory msSaxParserFactory = SAXParserFactory.newInstance();
        	SAXParser msSaxParser = msSaxParserFactory.newSAXParser();
        	XMLReader msXmlReader = msSaxParser.getXMLReader();
        	msXmlReader.setContentHandler(mPushRegItemHandler);
         	msXmlReader.parse(mInputSource);
        } catch (Exception e) {

        }
        return mPushRegItemHandler.getParsedData();
	}
	synchronized public boolean saveSetting(List<PushRegItem> list)
	{
		Editor editor = mPushSharedPreferences.edit();
		String strXMLRegItem = List2String(list);
		editor.putString(PUSH_REG_LIST_STRING, strXMLRegItem);
		return editor.commit();
	}
	public String List2String(List<PushRegItem> list){
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer=new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("utf-8", true);
			serializer.startTag("", PushRegItemHandler.PUSH_REG_LIST);

			for(PushRegItem regItem : list)
			{
				serializer.startTag("", PushRegItemHandler.PUSH_REG_ITEM);

				serializer.startTag("", PushRegItemHandler.PUSH_APP_PACKAGENAME);
				serializer.text(regItem.packageName);
				serializer.endTag("", PushRegItemHandler.PUSH_APP_PACKAGENAME);

				serializer.startTag("", PushRegItemHandler.PUSH_SCHOOLID);
				serializer.text(regItem.SchoolID);
				serializer.endTag("", PushRegItemHandler.PUSH_SCHOOLID);

				serializer.startTag("", PushRegItemHandler.PUSH_SCHOOLKEY);
				serializer.text(regItem.SchoolKey);
				serializer.endTag("", PushRegItemHandler.PUSH_SCHOOLKEY);

				serializer.startTag("", PushRegItemHandler.PUSH_APPVERSION);
				serializer.text(regItem.appver);
				serializer.endTag("", PushRegItemHandler.PUSH_APPVERSION);

				serializer.endTag("", PushRegItemHandler.PUSH_REG_ITEM);
			}
			serializer.endTag("", PushRegItemHandler.PUSH_REG_LIST);
			serializer.endDocument();
		}
		catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();
	}

}
