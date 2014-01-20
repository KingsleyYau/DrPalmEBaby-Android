/*
 * File         : DrUrlConnection.h
 * Date         : 2012-07-02
 * Author       : Kingsley Yau
 * Copyright    : City Hotspot Co., Ltd.
 * Description  : DrPalm DrUrlConnection src
 */

#include "DrUrlConnection.h"
#include "DrHttpPostBody.h"
#include <stdio.h>
#include <stdlib.h>
using namespace std;
DrUrlConnection::DrUrlConnection() {
	m_id = 0;
	m_wholeUrl = "";
	m_path = "";
	m_basepath = "";
	m_isKeepAlive = false;
	m_isPost = false;
	m_data = NULL;
	m_dataLen = 0;
	m_DrHttpClient.setCallback(this);
}

DrUrlConnection::~DrUrlConnection() {
	if(m_data){
		delete m_data;
		m_dataLen = 0;
	}
}
void DrUrlConnection::setCallback(JNICALLBACK callback){
	m_JniCallback = callback;

}

void DrUrlConnection::setKeepAlive(bool isKeepAlive){
	m_isKeepAlive = isKeepAlive;
}
void DrUrlConnection::setPost(bool isPost){
	m_isPost = isPost;
}
void DrUrlConnection::setBasePath(string basepath){
	m_basepath = basepath;
}
void DrUrlConnection::setPath(string path){
	m_path = path;
}
void DrUrlConnection::setDomain(string domain){
	m_domain = domain;
}

void DrUrlConnection::addParam(string key, string value){
	URLPARAMMAP::iterator it = m_mapParam.find(key);
	if(m_mapParam.end() != it){
		it->second = value;
	}
	else{
		m_mapParam.insert(URLPARAMMAP::value_type(key, value));
		showLog("Jni.DrUrlConnection.addParam", "key:%s value:%s", key.c_str(), value.c_str());
	}
}
void DrUrlConnection::addFile(string key,string fileext, char* data, int len){
	URLFILEMAP::iterator it = m_mapFile.find(key);
	if(m_mapFile.end() != it){

	}
	else{
		FILESTRUCT file(data, len,fileext);
		m_mapFile.insert(URLFILEMAP::value_type(key, file));
		showLog("Jni.DrUrlConnection.addFile", "key:%s data:%d len:%d", key.c_str(), data, len);
	}
}
void DrUrlConnection::setData(char *buf, int len){
	m_data = buf;
	m_dataLen = len;
	showLog("Jni.DrUrlConnection.setData", "m_dataLen:%d len:%d", m_dataLen, len);
}
void DrUrlConnection::clearFile(){

}
void DrUrlConnection::clearParam(){
	m_wholeUrl = "";
	m_path = "";
	m_basepath = "";
	m_isKeepAlive = false;
	m_isPost = false;
	m_mapParam.clear();
	m_mapFile.clear();
	m_data = NULL;
	m_dataLen = 0;
}
int DrUrlConnection::startRequest(){
	showLog("Jni.DrUrlConnection.startRequest", "startRequest");
	int id = -1;
	int filecount = 0;
	char cFileCount[10];


	basic_string<char>::size_type index = m_domain.find(HTTP_URL_START, 0);
	if( string::npos == index ){
		m_wholeUrl = HTTP_URL_START;
	}
	m_wholeUrl += m_domain;
	m_wholeUrl += "/";
	if("" != m_basepath){
		m_wholeUrl += m_basepath;
		m_wholeUrl += "/";
	}
	m_wholeUrl += m_path;
	URLPARAMMAP::iterator it;
	URLFILEMAP::iterator itfile;

	if(m_isPost){
		DrHttpPostBody postbody;
		if(m_data){
			showLog("Jni.DrUrlConnection.addData", "postbody.addData:%s", m_data);
			postbody.addData(m_data, m_dataLen);
		}
		else{
			for(it = m_mapParam.begin(); it != m_mapParam.end(); it++){
				postbody.addString(it->first, it->second);
			}
			for(itfile = m_mapFile.begin(); itfile != m_mapFile.end(); itfile++){
//				string fileKey = DrHttpClient_POST_FILE_PRE;
//				sprintf(cFileCount, "%d", filecount++);
//				fileKey += cFileCount;
				string fileKey = itfile->first;
				FILESTRUCT file(itfile->second._data, itfile->second._size,itfile->second._fileext);
				string fileName = fileKey;
				fileName += ".";
				fileName += itfile->second._fileext;
				showLog("Jni.DrUrlConnection.addFile", "postbody.addFile fileKey:%s, fileName:%s data:%d len:%d", fileKey.c_str(), fileName.c_str(), file._data, file._size);
				postbody.addFile(fileKey, fileName, file._data, file._size);

			}
		}
		//string body = postbody.getData();
		showLog("Jni.DrUrlConnection.startRequest", "postbody.getData(%d)", postbody.getSize());
		id = httpPost(m_wholeUrl, (char*)postbody.getData(), postbody.getSize(), m_isKeepAlive);
	}
	else{
		for(it = m_mapParam.begin(); it != m_mapParam.end(); it++){
			if(it == m_mapParam.begin())
				m_wholeUrl += "?";
			else
				m_wholeUrl += "&";
			m_wholeUrl += it->first;
			m_wholeUrl += "=";
			m_wholeUrl += it->second;
		}
		translate(m_wholeUrl);
		id = httpGet(m_wholeUrl);
		showLog("Jni.DrUrlConnection.startRequest", "m_wholeUrl:%s threadid:%d", m_wholeUrl.c_str(), id);
	}
	clearParam();
	return id;
}
int DrUrlConnection::httpGet(string strUrl){
	return m_DrHttpClient.httpGet(strUrl);
}
int DrUrlConnection::httpPost(string strUrl, char* data, int len, bool isKeepAlive){
	return m_DrHttpClient.httpPost(strUrl, data, len, isKeepAlive);
}
void DrUrlConnection::onSuccess(unsigned char* buf, int len, int iThreadId, bool bBackToView){
	m_JniCallback.onSuccess(buf, len, iThreadId, bBackToView);
}
void DrUrlConnection::onError(unsigned char* buf, int len, int iThreadId, bool bBackToView){
	m_JniCallback.onError(buf, len, iThreadId, bBackToView);
}
void DrUrlConnection::onReceiveData(unsigned char* buf, int len, int iThreadId, bool bBackToView){
	m_JniCallback.onReceiveData(buf, len, iThreadId, bBackToView);
}
void DrUrlConnection::onWriteLog(unsigned char* buf, int len, int iThreadId){
	m_JniCallback.onWriteLog(buf, len, iThreadId);
}
bool DrUrlConnection::stopConnection(string strUrl){
	string wholeUrl = "";
	basic_string<char>::size_type index = strUrl.find(HTTP_URL_START, 0);
		if( string::npos == index ){
			wholeUrl = HTTP_URL_START;
	}
	wholeUrl += strUrl;
	wholeUrl += "/";
	return m_DrHttpClient.stopConnection(wholeUrl);
}
void DrUrlConnection::translate(string &str){
	string ret = "";
	const char* ptr = str.c_str();
	for(int i =0; i< str.length(); i++){
		if(ptr[i] == ' '){
			ret += "+";
		}
		else{
			ret += ptr[i];
		}
	}
	str = ret;
}
