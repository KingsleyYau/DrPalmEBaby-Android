/*
 * File         : DrCOMSocket.h
 * Date         : 2011-07-11
 * Author       : Keqin Su
 * Copyright    : City Hotspot Co., Ltd.
 * Description  : DrCOM socket include
 */

#ifndef _INC_DRCOMSOCKET_
#define _INC_DRCOMSOCKET_

#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string>
#include <string.h>
#include <openssl/ssl.h>
#include <openssl/err.h>
#include <openssl/md5.h>
#include "DrCOMDefine.h"

using namespace std;

class tcpSocket
{
	public:
		tcpSocket();
		~tcpSocket();
		tcpSocket & operator=(const tcpSocket &ojb);

		virtual int Connect(string strAddress, unsigned int uiPort = HTTP_PROT);
		virtual int SendData(char* pBuffer, unsigned int uiSendLen, unsigned int uiTimeout = HTTP_SEND_TIMEOUT);
		virtual int RecvData(char* pBuffer, unsigned int uiRecvLen, bool bRecvAll = true, unsigned int uiTimeout = HTTP_RECV_TIMEOUT);
		virtual int Close();

		int getSocketId();
	public:
		static bool CompareLocalAddress(string strAddress);
		static string GetFirstMacAddress();
		static string GetFirstIpAddress();
		static string GetMacAddressList();

	protected:
		int BaseConnect(string strAddress, unsigned int uiPort, bool bBlock);
		unsigned int GetTick();
		bool isTimeout(unsigned int uiStart, unsigned int uiTimeout);

	protected:
		int m_iSocket;
		string m_strAddress;
		unsigned int m_uiPort;
};

class sslSocket : public tcpSocket
{
	public:
		sslSocket();
		~sslSocket();

		int Connect(string strAddress, unsigned int uiPort = HTTPS_PROT);
		int SendData(char* pBuffer, unsigned int uiSendLen, unsigned int uiTimeout = HTTP_SEND_TIMEOUT);
		int RecvData(char* pBuffer, unsigned int uiRecvLen, bool bRecvAll = true, unsigned int uiTimeout = HTTP_RECV_TIMEOUT);
		int Close();

	protected:
		SSL_CTX* m_pCTX;
		SSL* m_pSSL;
};

#endif
