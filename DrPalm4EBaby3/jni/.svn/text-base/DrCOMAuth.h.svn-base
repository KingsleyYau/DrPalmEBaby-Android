/*
 * File         : DrCOMAuth.h
 * Date         : 2011-07-12
 * Author       : Keqin Su
 * Copyright    : City Hotspot Co., Ltd.
 * Description  : DrCOM auth include
 */

#ifndef _INC_DRCOMAUTH_
#define _INC_DRCOMAUTH_

#include "DrCOMSocket.h"

class DrCOMAuth
{
	public:
		DrCOMAuth();
		~DrCOMAuth();

		int httpLogin(string strGatewayAddress, string strUsername, string strPassword);
		int httpLoginCheck();
		int httpLoginAuth();
		string getUndefineError();
		string getXip();
		string getMac();

		int httpLogout();

		string getGatewayAddress();
		bool getLoginStatus();

		bool httpStatus();
		string getFluxStatus();
		string getTimeStatus();

	public:
		int httpHandle(tcpSocket* ptSocket, int& iHttpCode, int& iContentLen, string& strHttpServerName, string& strHttpReLocal, char* pHttpBody);
		char* strIstr(const char *haystack, const char *needle);
		string findStringBetween(char* pData, char* pBegin, char* pEnd, char* pTmpBuffer = NULL, int iTmpLen = 0);
		string grantMD5(string strData);
		string trim(string strData);
		int doWithLoginResult(char* pData);
		int loginStatus(string strMsg, string strMsga, string strXip, string strMac, string strTime, string strFlow, string strCode);
		string grantUpdateRequest();
		static void* requestUpdate(void* pData);

	protected:
		string m_strGatewayAddress;
		string m_strUsername;
		string m_strPassword;

		string m_strUndefineError;
		string m_strXip;
		string m_strMac;

		string m_strFlux;
		string m_strTime;

		bool m_bConnected;

		string m_strMacList;

		char m_cBuffer[DrCOM_BUFFER_64K];
};

#endif
