/*
 * File         : com_drcom_drpalm_Tool_service_DrPushServiceJni.cpp
 * Date         : 2012-11-10
 * Author       : Kingsley Yau
 * Copyright    : City Hotspot Co., Ltd.
 * Description  : com_drcom_drpalm_Tool_service_DrPushServiceJni source
 */

#include "com_drcom_drpalm_Tool_service_DrPushServiceJni.h"
#include "DrRequestDefine.h"

// HttpClient
#include "DrUrlConnection.h"
// Json
#include "json/json/json.h"

// Common
#include "StringHandle.h"
#include "DrLog.h"
#include "DrMutex.h"

#include <map>
#include <list>

#define MAXREMAINCOUNT 100;
#define PUSHMSG_BUFFER_SIZE_4K 4096
#define TEMP_BUFFER_SIZE_64K 65535

static void checkAndClearExceptionFromCallback(JNIEnv* env, const char* methodName, bool isThrow);
static void writeLogWithJava(JNIEnv *env, unsigned char* buf, int len);

typedef map<int, jobject> JNICALLBACKOBJECTMAP;
JNICALLBACKOBJECTMAP g_ObjectMap;

/* Source for class com_drcom_drpalm_Tool_service_DrPushServiceJni */

DrMutex gMutex;
jobject g_JniCallbackObject = NULL;
JavaVM *g_jvm = NULL;

// push
char TEMP_BUFFER[TEMP_BUFFER_SIZE_64K];
char *pushMsgBuffer = NULL;
int iCurrentSize = 0;
int iCurrentCapacity = 4 * PUSHMSG_BUFFER_SIZE_4K;
typedef struct _PushMessageData{
	char *_data;
	int _len;
	_PushMessageData(){
		_data = NULL;
		_len = 0;
	}
	~_PushMessageData(){
		_data = NULL;
		_len = 0;
	}
}PUSHMESSAGEDATA;
typedef list<PUSHMESSAGEDATA> PUSHMSGLIST;

static inline void initPushMsgBuffer(){
	if(pushMsgBuffer){
		delete[] pushMsgBuffer;
		pushMsgBuffer = NULL;
		iCurrentSize = 0;
		iCurrentCapacity = 4 * PUSHMSG_BUFFER_SIZE_4K;
	}
	pushMsgBuffer = new char[iCurrentCapacity];
}
static inline void addPushMsgBuffer(const char *buf, int size){
	bool bFlag = false;
	showLog("Jni.DrPushService.addPushMsgBuffer", "iCurrentSize:%d size:%d iCurrentCapacity:%d", iCurrentSize, size, iCurrentCapacity);
	while(size + iCurrentSize > iCurrentCapacity){
		iCurrentCapacity *= 2;
		bFlag = true;
		showLog("Jni.DrPushService.addPushMsgBuffer", "new iCurrentCapacity:%d", iCurrentCapacity);
	}
	if(bFlag){
		char *newBuffer = new char[iCurrentCapacity];
		memcpy(newBuffer, pushMsgBuffer, iCurrentSize);
		delete[] pushMsgBuffer;
		pushMsgBuffer = newBuffer;
	}
	memcpy(pushMsgBuffer + iCurrentSize, buf, size);
	iCurrentSize += size;
	showLog("Jni.DrPushService.composeMessage", "iCurrentSize:%d iCurrentCapacity:%d", iCurrentSize, iCurrentCapacity);
}
static PUSHMSGLIST composeMessage(const char* buf, int len){
	PUSHMSGLIST list;
	if(0 == len || buf == NULL){
		showLog("Jni.DrPushService.composeMessage", "buf is null or len is zero break");
		return list;
	}
	showLog("Jni.DrPushService.composeMessage", "pushMsgBuffer:%s", pushMsgBuffer);
	addPushMsgBuffer(buf, len);
	PUSHMESSAGEDATA data;
	char *cBegin;
	do
	{
		cBegin = StringHandle::strIstr(pushMsgBuffer, DrHttpClient_HTTP_CONTEXT_LENGYH);
		if(cBegin){
			//showLog("Jni.DrService.composeMessage", "cBegin:%s", cBegin);
			char *cEnd = StringHandle::strIstr(pushMsgBuffer, DrHttpClient_HTTP_END);
			if(cEnd){
				//showLog("Jni.DrService.composeMessage", "cEnd:%s", cEnd);
				char *conLenStart = cBegin + strlen(DrHttpClient_HTTP_CONTEXT_LENGYH);
				string strConLen = StringHandle::findStringBetween(pushMsgBuffer, conLenStart, cEnd, TEMP_BUFFER, TEMP_BUFFER_SIZE_64K);
				showLog("Jni.DrPushService.composeMessage", "strConLen:%s", strConLen.c_str());
				int iLen = atoi(strConLen.c_str());
				// check length
				int wholeLen = strlen(DrHttpClient_HTTP_CONTEXT_LENGYH) + strConLen.length() \
						+ strlen(DrHttpClient_HTTP_END) + iLen;
				//showLog("Jni.DrService.composeMessage", "wholeLen:%d iCurrentSize:%d iLen:%d", wholeLen, iCurrentSize, iLen);
				if(wholeLen <= iCurrentSize){
					char *body = cEnd + 4;
					data._data = new char[iLen];
					memcpy(data._data, body, iLen);
					data._len = iLen;
					list.push_back(data);
					showLog("Jni.DrPushService.composeMessage", "list.size:%d", list.size());
					showLog("Jni.DrPushService.composeMessage", "data(%d):%s",data._len, data._data);
					iCurrentSize -= wholeLen;
					memcpy(pushMsgBuffer, pushMsgBuffer + wholeLen, iCurrentSize);
					memset(pushMsgBuffer + iCurrentSize, 0, wholeLen);
					showLog("Jni.DrPushService.composeMessage", "iCurrentSize:%d", iCurrentSize);
				}
				else{
					break;
				}
			}
		}
	}while(cBegin);
	return list;
}

static void insertCallbackObject(JNIEnv *env, int id, jobject callback, string methodname = "") {
	jobject obj = env->NewGlobalRef(callback);
	gMutex.lock(methodname.c_str());
	JNICALLBACKOBJECTMAP::iterator it = g_ObjectMap.find(id);
	if(it != g_ObjectMap.end()){
		jobject oldObj = it->second;
		if(NULL != oldObj){
			showLog("Jni.DrPushService", "modify old %s callback object iThreadId:%d", methodname.c_str(), id);
			env->DeleteGlobalRef(oldObj);
			oldObj = NULL;
		}
		it->second = obj;
	}
	else {
		showLog("Jni.DrPushService", "insert %s callback object iThreadId:%d", methodname.c_str(), id);
		g_ObjectMap.insert(JNICALLBACKOBJECTMAP::value_type(id, obj));
	}
	char logBuff[2048];
	memset(logBuff, '\0', 2048);
	sprintf(logBuff, "%s\r\nstart new thread:%d", methodname.c_str(), id);
	writeLogWithJava(env, (unsigned char*)logBuff, strlen(logBuff));
	gMutex.unlock(methodname.c_str());
}
static void earseCallbackObject(JNIEnv *env, int id, string methodname = "") {
	gMutex.lock(methodname.c_str());
	JNICALLBACKOBJECTMAP::iterator it = g_ObjectMap.find(id);
	if(it != g_ObjectMap.end()){
		jobject obj = it->second;
		if(NULL != obj){
			env->DeleteGlobalRef(obj);
			obj = NULL;
		}
		showLog("Jni.DrPushService", "earse old %s callback object iThreadId:%d", methodname.c_str(), id);
		g_ObjectMap.erase(it);
	}
	gMutex.unlock(methodname.c_str());
}

DrUrlConnection gDrUrlConnection;
static void checkAndClearExceptionFromCallback(JNIEnv* env, const char* methodName, bool isThrow){
	if(env->ExceptionCheck()){
		showLog("Jni.DrPushService.checkAndClearExceptionFromCallback", "An exception was thrown by callback:'%s'", methodName);
		env->ExceptionClear();
	}
}
static void writeLogWithJava(JNIEnv *env, unsigned char* buf, int len) {
	showLog("Jni.DrPushService.writeLogWithJava", "buf(%d):%s", len, buf);
	jclass cls_DrServiceJniCallback = env->GetObjectClass(g_JniCallbackObject);
	jmethodID mth_WriteJniLog = env->GetMethodID(cls_DrServiceJniCallback, "WriteJniLog","([B)V");
	checkAndClearExceptionFromCallback(env, "Jni.DrService.WriteJniLog", false);
	if(0 != mth_WriteJniLog){
		jbyteArray data = env->NewByteArray(len);
		env->SetByteArrayRegion(data, 0, len, (jbyte*)buf);
		env->CallVoidMethod(g_JniCallbackObject, mth_WriteJniLog, data);
	}
}
static void onWriteLog(unsigned char* buf, int len, int iThreadId) {
	showLog("Jni.DrPushService.onWriteLog", "buf:%s len:%d, iThreadId:%d", buf, len, iThreadId);
	JNIEnv *env;
	if(g_jvm){
		jint iRet;
		iRet = g_jvm->AttachCurrentThread((JNIEnv **)&env, NULL);
		writeLogWithJava(env, buf, len);
		g_jvm->DetachCurrentThread();
	}
}
static void onSuccess(unsigned char* buf, int len, int iThreadId, bool bBackToView){
	showLog("Jni.DrPushService.onSuccess", "buf:%s len:%d, iThreadId:%d", buf, len, iThreadId);
	JNIEnv *env;//AndroidRuntime::getJNIEnv();
	if(g_jvm){
		jint iRet;
		iRet = g_jvm->AttachCurrentThread((JNIEnv **)&env, NULL);
		//showLog("Jni.DrPushService.onSuccess", "AttachCurrentThread iRet:%d", iRet);
		jbyteArray data = env->NewByteArray(len);
		env->SetByteArrayRegion(data, 0, len, (jbyte*)buf);
		showLog("Jni.DrPushService.onSuccess", "len:%d", len);

		jobject obj = NULL;
		gMutex.lock("onSuccess");
		JNICALLBACKOBJECTMAP::iterator it = g_ObjectMap.find(iThreadId);
		if(it != g_ObjectMap.end()){
			obj = it->second;
		}
		gMutex.unlock("onSuccess");

		if(NULL != obj && bBackToView) {
			showLog("Jni.DrPushService.onSuccess", "find callback object iThreadId:%d", iThreadId);
			jclass cls_DrServiceJniCallback = env->GetObjectClass(obj);
			jmethodID mth_onSuccess = env->GetMethodID(cls_DrServiceJniCallback,"onSuccess","([B)V");
			checkAndClearExceptionFromCallback(env, "Jni.DrPushService.onSuccess", false);
			if(0 != mth_onSuccess){
				env->CallVoidMethod(obj, mth_onSuccess, data);
			}
			showLog("Jni.DrPushService.onSuccess", "finished");
		}

		earseCallbackObject(env, iThreadId, "onSuccess");

		g_jvm->DetachCurrentThread();
	}

}
static void onError(unsigned char* buf, int len, int iThreadId, bool bBackToView){
	showLog("Jni.DrPushService.onError", "buf:%s len:%d, iThreadId:%d", buf, len, iThreadId);
	JNIEnv *env;//AndroidRuntime::getJNIEnv();
	if(g_jvm){

		g_jvm->AttachCurrentThread((JNIEnv **)&env, NULL);
		jbyteArray data = env->NewByteArray(len);
		env->SetByteArrayRegion(data, 0, len, (jbyte*)buf);

		jobject obj = NULL;
		gMutex.lock("onError");
		JNICALLBACKOBJECTMAP::iterator it = g_ObjectMap.find(iThreadId);
		if(it != g_ObjectMap.end()){
			obj = it->second;
		}
		gMutex.unlock("onError");

		if(NULL != obj && bBackToView) {
			jclass cls_DrServiceJniCallback = env->GetObjectClass(obj);
			jmethodID mth_onError = env->GetMethodID(cls_DrServiceJniCallback,"onError","([B)V");
			checkAndClearExceptionFromCallback(env, "Jni.DrService.onError", false);
			if(0 != mth_onError){
				env->CallVoidMethod(obj, mth_onError, data);
			}
			showLog("Jni.DrPushService.onError", "finished");
		}
		earseCallbackObject(env, iThreadId, "onError");
		g_jvm->DetachCurrentThread();
	}
}
static void onReceiveData(unsigned char* buf, int len, int iThreadId, bool bBackToView){
	showLog("Jni.DrPushService.onReceiveData", "buf:%s len:%d, iThreadId:%d", buf, len, iThreadId);

	JNIEnv *env;
	bool isOnReceivePushData = false;
	if(g_jvm){
		g_jvm->AttachCurrentThread((JNIEnv **)&env, NULL);

		jobject obj = NULL;
		gMutex.lock("onReceiveData");
		JNICALLBACKOBJECTMAP::iterator it = g_ObjectMap.find(iThreadId);
		if(it != g_ObjectMap.end()){
			obj = it->second;
		}
		gMutex.unlock("onReceiveData");
		if(NULL != obj && bBackToView){

			jclass cls_DrServiceJniCallback = env->GetObjectClass(obj);
			if(cls_DrServiceJniCallback){
//				jmethodID mth_onReceiveData = env->GetMethodID(cls_DrServiceJniCallback,"onReceiveData","([B)V");
//				checkAndClearExceptionFromCallback(env, "onReceiveData", false);
//				if(0 != mth_onReceiveData){
//					showLog("Jni.DrService.onReceiveData", "onReceiveData");
//					jbyteArray data = env->NewByteArray(len);
//					env->SetByteArrayRegion(data, 0, len, (jbyte*)buf);
//					env->CallVoidMethod(obj, mth_onReceiveData, data);
//				}

				jmethodID mth_onReceivePushData = env->GetMethodID(cls_DrServiceJniCallback,"onReceivePushData","([B)V");
				checkAndClearExceptionFromCallback(env, "onReceivePushData", false);
				if(0 != mth_onReceivePushData)
				{
					showLog("Jni.DrService.onReceivePushData", "onReceivePushData");
					PUSHMSGLIST list = composeMessage((const char*)buf, len);
					PUSHMSGLIST::iterator itData;
					for(itData = list.begin(); itData != list.end(); itData++){
						int msgLen = itData->_len;
						jbyteArray data = env->NewByteArray(msgLen);
						env->SetByteArrayRegion(data, 0, msgLen, (jbyte*)itData->_data);
						showLog("Jni.DrService.onReceivePushData", "get a new push message(%d):%s", msgLen, itData->_data);
						env->CallVoidMethod(obj, mth_onReceivePushData, data);
						delete[] itData->_data;
						showLog("Jni.DrService.onReceivePushData", "finshed");
					}
				}
			}
			showLog("Jni.DrPushService.onReceiveData", "finished");
		}
		g_jvm->DetachCurrentThread();
	}
}

static JNICALLBACK m_JniCallback = {
		onSuccess,
		onError,
		onReceiveData,
		onWriteLog
};

inline string grantMD5(string strData) {
	unsigned char md[DrCOM_BUFFER_16B] = {'\0'};
	char cTmp[3]={'\0'};
	string strRet = "";

	MD5((unsigned char*)strData.c_str(), strData.length(), md);
	for (int i = 0; i < 16; i++){
		sprintf(cTmp,"%02x", md[i]);
		strRet += cTmp;
	}
	return strRet;
}
inline string getTokenid(string indetify, string packageName){
	string strIndetify = indetify + packageName;
	showLog("Jni.DrPushService.getTokenid", "before md5 tokenid:%s", strIndetify.c_str());
	string tokenid = "";
	if("" != strIndetify)
		tokenid = grantMD5(strIndetify);
	return tokenid;
}
inline string getAppid(string schoolid){
	return grantMD5(schoolid);
}
inline string getCheckCode(string tokenid, string challenge, string schoolKey){
	string md5SchoolKey = grantMD5(schoolKey);
	string value = tokenid + challenge + md5SchoolKey;
	showLog("Jni.DrPushService.getCheckCode", "before md5 tokenid:%s", tokenid.c_str());
	showLog("Jni.DrPushService.getCheckCode", "before md5 challenge:%s", challenge.c_str());
	showLog("Jni.DrPushService.getCheckCode", "before md5 grantMD5(schoolKey):%s", md5SchoolKey.c_str());
	showLog("Jni.DrPushService.getCheckCode", "before md5 value:%s", value.c_str());
	return grantMD5(value);
}

JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrPushServiceJni_NativeInit
  (JNIEnv *env, jobject obj){
	bool bFlag = false;
	if(env){
		env->GetJavaVM(&g_jvm);
		if(obj){
			if(g_JniCallbackObject) {
				env->DeleteGlobalRef(g_JniCallbackObject);
				g_JniCallbackObject = NULL;
			}
			g_JniCallbackObject = env->NewGlobalRef(obj);
			gDrUrlConnection.setCallback(m_JniCallback);
			showLog("Jni.DrPushService.Native_Init", "succeed");
			return bFlag;
		}
	}
	return bFlag;

}
/*
 * Class:     com_drcom_drpalm_Tool_service_DrPushServiceJni
 * Method:    GetPushChallenge
 * Signature: (Ljava/lang/String)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrPushServiceJni_GetPushChallenge
  (JNIEnv *env, jobject, jstring domain, jstring packagename, jstring indetify, jobject callback){
	bool bFlag = false;
	const char *pDomain = env->GetStringUTFChars(domain, 0);
	const char *pPackagename = env->GetStringUTFChars(packagename, 0);
	const char *pIndetify = env->GetStringUTFChars(indetify, 0);

	Json::Value root;
	root[PUSH_CMD] = PUSH_GETCHANLLENGE;
	string tokenid = getTokenid(pIndetify, pPackagename);
	root[PUSH_TOKENID] = tokenid;
	Json::FastWriter writer;
	string postData = writer.write(root);
	showLog("Jni.DrPushService.GetPushChallenge", "postData:%s", postData.c_str());

	gDrUrlConnection.setPost(true);
	gDrUrlConnection.setKeepAlive(true);
	gDrUrlConnection.setDomain(pDomain);
	gDrUrlConnection.setData((char*)postData.c_str(), strlen(postData.c_str()));

	int id = gDrUrlConnection.startRequest();
	if(0 < id){
		insertCallbackObject(env, id, callback, "GetPushChallenge");
		bFlag = true;
	}

	env->ReleaseStringUTFChars(domain, pDomain);
	env->ReleaseStringUTFChars(packagename, pPackagename);
	env->ReleaseStringUTFChars(indetify, pIndetify);
	return bFlag;
}

/*
 * Class:     com_drcom_drpalm_Tool_service_DrPushServiceJni
 * Method:    RegPushToken
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrPushServiceJni_RegPushToken
  (JNIEnv *env, jobject, jstring domain, jstring challenge, jstring schoolid, jstring schoolkey,
		 jstring packagename, jstring indetify, jstring model, jstring system, jstring appver, jobject callback){
	bool bFlag = false;
	const char *pDomain = env->GetStringUTFChars(domain, 0);
	const char *pChallenge = env->GetStringUTFChars(challenge, 0);
	const char *pSchoolId = env->GetStringUTFChars(schoolid, 0);
	const char *pSchoolKey = env->GetStringUTFChars(schoolkey, 0);
	const char *pPackagename = env->GetStringUTFChars(packagename, 0);
	const char *pIndetify = env->GetStringUTFChars(indetify, 0);
	const char *pModel = env->GetStringUTFChars(model, 0);
	const char *pSystem = env->GetStringUTFChars(system, 0);
	const char *pAppver = env->GetStringUTFChars(appver, 0);


	Json::Value root;
	Json::Value body;

	root[PUSH_CMD] = PUSH_REGISTER;
	string tokenid = getTokenid(pIndetify, pPackagename);
	root[PUSH_TOKENID] = tokenid;

	body[PUSH_APPID] = getAppid(pSchoolId);
	string checkcode = getCheckCode(tokenid, pChallenge, pSchoolKey);
	body[PUSH_CHECKCODE] = checkcode;
	body[PUSH_MODEL] = pModel;
	body[PUSH_SYSTEM] = pSystem;
	body[PUSH_APPVER] = pAppver;

	root[PUSH_BODY] = body;

	showLog("Jni.DrPushService.RegPushToken", "pIndetify:%s", pIndetify);
	showLog("Jni.DrPushService.RegPushToken", "pSchoolKey:%s", pSchoolKey);
	showLog("Jni.DrPushService.RegPushToken", "pSchoolId:%s", pSchoolId);
	showLog("Jni.DrPushService.RegPushToken", "pPackagename:%s", pPackagename);
	showLog("Jni.DrPushService.RegPushToken", "tokenid:%s", tokenid.c_str());
	showLog("Jni.DrPushService.RegPushToken", "checkcode:%s", checkcode.c_str());


	Json::FastWriter writer;
	string postData = writer.write(root);
	//showLog("Jni.DrService.RegPushToken", "RegPushToken:%s", postData.c_str());

	gDrUrlConnection.setPost(true);
	gDrUrlConnection.setKeepAlive(true);
	gDrUrlConnection.setDomain(pDomain);
	gDrUrlConnection.setData((char*)postData.c_str(), strlen(postData.c_str()));

	int id = gDrUrlConnection.startRequest();
	if(0 < id){
		insertCallbackObject(env, id, callback, "RegPushToken");
		bFlag = true;
	}

	string logString = "Appid:" + getAppid(pSchoolId);
	logString += "\r\n";
	logString += "pIndetify:";
	logString +=  pIndetify;
	logString += "\r\n";
	logString += "pPackagename:";
	logString += pPackagename;
	logString += "\r\n";
	logString += "tokenid:";
	logString +=  tokenid;
	logString += "\r\n";
	logString += "pChallenge:";
	logString +=  pChallenge;
	logString += "\r\n";
	logString += "pSchoolKey:";
	logString +=  pSchoolKey;
	logString += "\r\n";
	logString += "checkcode:";
	logString +=  checkcode;
	logString += "\r\n";
	logString += "/****************************************************/";

	writeLogWithJava(env, (unsigned char*)logString.c_str(), strlen(logString.c_str()));

	env->ReleaseStringUTFChars(domain, pDomain);
	env->ReleaseStringUTFChars(challenge, pChallenge);
	env->ReleaseStringUTFChars(schoolid, pSchoolId);
	env->ReleaseStringUTFChars(schoolkey, pSchoolKey);
	env->ReleaseStringUTFChars(packagename, pPackagename);
	env->ReleaseStringUTFChars(indetify, pIndetify);
	env->ReleaseStringUTFChars(model, pModel);
	env->ReleaseStringUTFChars(system, pSystem);
	env->ReleaseStringUTFChars(appver, pAppver);

	return bFlag;
}

/*
 * Class:     com_drcom_drpalm_Tool_service_DrPushServiceJni
 * Method:    StartGetPushMessage
 * Signature: (Ljava/lang/String;Lcom/drcom/drpalm/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrPushServiceJni_StartGetPushMessage
  (JNIEnv *env, jobject, jstring domain, jobject callback){
	bool bFlag = false;
	initPushMsgBuffer();
	const char *pDomain = env->GetStringUTFChars(domain, 0);

	Json::Value root;
	root[PUSH_CMD] = PUSH_GETMESSAGE;
	Json::FastWriter writer;
	string postData = writer.write(root);
	showLog("Jni.DrPushService.StartGetPushMessage", "postData:%s", postData.c_str());

	gDrUrlConnection.setPost(true);
	gDrUrlConnection.setKeepAlive(true);
	gDrUrlConnection.setDomain(pDomain);
	gDrUrlConnection.setData((char*)postData.c_str(), strlen(postData.c_str()));

	int id = gDrUrlConnection.startRequest();
	if(0 < id){
		insertCallbackObject(env, id, callback, "StartGetPushMessage");
		bFlag = true;
	}

	env->ReleaseStringUTFChars(domain, pDomain);
	return bFlag;
}
/*
 * Class:     com_drcom_drpalm_Tool_service_DrPushServiceJni
 * Method:    StopUrlConnection
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrPushServiceJni_StopUrlConnection
  (JNIEnv *env, jobject, jstring domain){
	bool bFlag = true;
	const char *pDomain = env->GetStringUTFChars(domain, 0);
	gDrUrlConnection.stopConnection(pDomain);
	env->ReleaseStringUTFChars(domain, pDomain);
	return bFlag;
}
/*
 * Class:     com_drcom_drpalm_Tool_service_DrPushServiceJni
 * Method:    GetFirstMacAddress
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_drcom_drpalm_Tool_service_DrPushServiceJni_GetFirstMacAddress
  (JNIEnv *env, jobject) {
	string macAddress = tcpSocket::GetFirstMacAddress();
	return env->NewStringUTF(macAddress.c_str());
}

/*
 * Class:     com_drcom_drpalm_Tool_service_DrPushServiceJni
 * Method:    GetDeviceId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_drcom_drpalm_Tool_service_DrPushServiceJni_GetDeviceId
  (JNIEnv *env, jobject) {
	string macAddress = tcpSocket::GetFirstMacAddress();
	string deviceId = grantMD5(macAddress);
	return env->NewStringUTF(deviceId.c_str());
}
