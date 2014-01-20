/*
 * File         : DrUrlConnection.h
 * Date         : 2012-07-02
 * Author       : Kingsley Yau
 * Copyright    : City Hotspot Co., Ltd.
 * Description  : DrPalm DrUrlConnection include
 */

#ifndef _INC_DRURLCONNECTION_
#define _INC_DRURLCONNECTION_

#include "DrHttpClient.h"
#include "DrJniCallback.h"
#include <map>
typedef struct _fileStruct{
	char* _data;
	int _size;
	string _fileext;
	_fileStruct(){
		_data = NULL;
		_size = 0;
		_fileext = "";
	}
	_fileStruct(char *data, int size,string ext){
		_data = data;
		//memcpy(_data, data, size);
		_size = size;
		_fileext = ext;
	}
	~_fileStruct(){
		_data = NULL;
		_size = 0;
		_fileext = "";
	}
}FILESTRUCT;
typedef map<string,string> URLPARAMMAP;
typedef map<string,FILESTRUCT> URLFILEMAP;
typedef map<int, int> JNIOBJECTMAP;
using namespace std;
class DrUrlConnection : public IDrHttpClientCallback
{
	public:
		DrUrlConnection();
		virtual ~DrUrlConnection();
		void setCallback(JNICALLBACK callback);
		void setDomain(string domain);
		void setBasePath(string path);
		void setPath(string path);
		void setPost(bool isPost);
		void setKeepAlive(bool isKeepAlive);
		void addParam(string key, string value);
		void addFile(string key,string fileext, char* data, int len);
		void setData(char *buf, int len);
		void clearParam();
		void clearFile();
		int startRequest();
		bool stopConnection(string strUrl);

		void composeMessage(char* buf,int nLen,char* msgbuf,int& nPushMsgLen);
	protected:
		void onReceiveData(unsigned char* buf, int len, int iThreadId, bool bBackToView = true);
		void onSuccess(unsigned char* buf, int len, int iThreadId, bool bBackToView = true);
		void onError(unsigned char* buf, int len, int iThreadId, bool bBackToView = true);
		void onWriteLog(unsigned char* buf, int len, int iThreadId);
	private:
		int httpGet(string strUrl);
		int httpPost(string strUrl, char* data, int len, bool isKeepAlive = false);
		void translate(string &str);

		DrHttpClient m_DrHttpClient;
		JNICALLBACK m_JniCallback;

		int m_id;
		bool m_isPost;
		bool m_isKeepAlive;
		string m_domain;
		string m_basepath;
		string m_path;
		string m_wholeUrl;
		char* m_data;
		int m_dataLen;
		URLPARAMMAP m_mapParam;
		URLFILEMAP m_mapFile;
		JNIOBJECTMAP m_mapJniObject;
};
#endif
