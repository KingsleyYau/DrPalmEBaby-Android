/*
 * File         : DrCOMAuth.cpp
 * Date         : 2011-07-12
 * Author       : Keqin Su
 * Copyright    : City Hotspot Co., Ltd.
 * Description  : DrCOM auth source
 */

#include <ctype.h>
#include "Arithmetic.h"
#include "DrCOMAuth.h"

DrCOMAuth::DrCOMAuth() {
	m_strGatewayAddress = "";
	m_strUsername = "";
	m_strPassword = "";

	m_strUndefineError = "";
	m_strXip = "";
	m_strMac = "";

	m_strFlux = "";
	m_strTime = "";

	m_bConnected = false;

	tcpSocket tS;
	m_strMacList = tS.GetMacAddressList();

	bzero(m_cBuffer, sizeof(m_cBuffer));
    srand(time(NULL));
}

DrCOMAuth::~DrCOMAuth() {

}

int DrCOMAuth::httpLogin(string strGatewayAddress, string strUsername, string strPassword) {
	m_strGatewayAddress = strGatewayAddress;
	m_strUsername = strUsername;
	m_strPassword = strPassword;

	int iRet = httpLoginCheck();
	if (iRet == DrCOM_SUCCESS) {
		iRet = httpLoginAuth();
		if (iRet == DrCOM_SUCCESS) {
			return DrCOM_SUCCESS;
		}
	}
	return iRet;
}

int DrCOMAuth::httpLoginCheck() {
	int iRet = Network_connection_interruption_check_the_network_configuration_please;
	int iHttpCode = 0, iContentLen = 0;
	string strHttpServerName = "", strHttpReLocal = "";
	char cRep[DrCOM_BUFFER_256B] = {'\0'};
	tcpSocket tSocket;

	//connect to test url
	bzero(m_cBuffer, sizeof(m_cBuffer));
	if (tSocket.Connect(DrCOM_TestUrl) == 1) {
		sprintf(m_cBuffer, DrCOM_GET, "/", DrCOM_TestUrl);
		if (tSocket.SendData(m_cBuffer, strlen(m_cBuffer)) == strlen(m_cBuffer)) {
			bzero(m_cBuffer, sizeof(m_cBuffer));
			if (httpHandle(&tSocket, iHttpCode, iContentLen, strHttpServerName, strHttpReLocal, (char*)m_cBuffer) == 1) {
				if (strHttpReLocal.length() > 0 && iHttpCode == DrCOM_HTTP_302) {
					tSocket.Close();
					//relocal to gateway
					if (tSocket.Connect(strHttpReLocal) == 1) {
						sprintf(m_cBuffer, DrCOM_GET, "/", strHttpReLocal.c_str());
						if (tSocket.SendData(m_cBuffer, strlen(m_cBuffer)) == strlen(m_cBuffer)) {
							bzero(m_cBuffer, sizeof(m_cBuffer));
							if (httpHandle(&tSocket, iHttpCode, iContentLen, strHttpServerName, strHttpReLocal, (char*)m_cBuffer) == 1) {
								if ((iHttpCode == DrCOM_HTTP_200) && ((strHttpServerName == DrCOM_Server) || (strHttpServerName == DrCOM_Server_IIS)) && strstr(m_cBuffer, DrCOM_0_html)) {
									string strAddress = findStringBetween(m_cBuffer, (char*)"v46ip='", (char*)"'", cRep, sizeof(cRep));
									if (strAddress.length() < 1) {
										strAddress = findStringBetween(m_cBuffer, (char*)"v46ip=\"", (char*)"\"", cRep, sizeof(cRep));
									}
									if ((strAddress.length() < 1) || (!tcpSocket::CompareLocalAddress(strAddress))) {
										iRet = This_account_does_not_allow_use_NAT;
									} else {
										m_strGatewayAddress = strHttpReLocal;
										iRet = DrCOM_SUCCESS;
									}
								} else {
									iRet = Can_not_find_Dr_COM_web_protocol;
								}
							}
						}
					}
				} else {
					if (m_bConnected) {
						iRet = DrCOM_CHECK;
					} else {
						if (m_strGatewayAddress.length() > 0) {
							if (httpLoginAuth() == DrCOM_SUCCESS) {
								iRet = DrCOM_SUCCESS;
							}
						} else {
							iRet = This_equipment_already_online_do_not_need_to_log_in;
						}
					}
				}
			}
		}
	}

	return iRet;
}

int DrCOMAuth::httpLoginAuth() {
	int iRet = Can_not_find_Dr_COM_web_protocol;
	char cRep[DrCOM_BUFFER_256B] = {'\0'};
	int iHttpCode = 0, iContentLen = 0;
	string strHttpServerName = "", strHttpReLocal = "";
	string strTime = "", strData = "", strUip = DrCOM_UIP;
	sslSocket sSocket;

	bzero(m_cBuffer, sizeof(m_cBuffer));
	if (sSocket.Connect(m_strGatewayAddress) == 1) {
		//grant time string
		time_t stm = time(NULL);
		struct tm tTime;
		localtime_r(&stm, &tTime);
		bzero(cRep, sizeof(cRep));
		snprintf(cRep, sizeof(cRep), "%04d-%02d-%02d %02d:%02d:%02d", tTime.tm_year+1900, tTime.tm_mon+1, tTime.tm_mday, tTime.tm_hour, tTime.tm_min, tTime.tm_sec);
		strTime = cRep;
		//grant data
		snprintf(cRep, sizeof(cRep), DrCOM_Login, m_strUsername.c_str(), m_strPassword.c_str(), m_strMacList.c_str(), DrCOM_Def_Num);
		strData = cRep;
		//grant uip
		strUip += grantMD5(strData + strTime);

		sprintf(m_cBuffer, DrCOM_POST, "/", strTime.c_str(), strUip.c_str(), strData.length(), m_strGatewayAddress.c_str(), strData.c_str());
		if (sSocket.SendData(m_cBuffer, strlen(m_cBuffer)) == strlen(m_cBuffer)) {
			bzero(m_cBuffer, sizeof(m_cBuffer));
			if (httpHandle(&sSocket, iHttpCode, iContentLen, strHttpServerName, strHttpReLocal, (char*)m_cBuffer) == 1) {
				if ((iHttpCode == DrCOM_HTTP_200) && ((strHttpServerName == DrCOM_Server) || (strHttpServerName == DrCOM_Server_IIS))) {
					iRet = doWithLoginResult(m_cBuffer);
					if (iRet == 1) {
						m_bConnected = true;
					}
				}
			}
		}
	}

	return iRet;
}

string DrCOMAuth::getUndefineError() {
	return m_strUndefineError;
}

string DrCOMAuth::getXip() {
	return m_strXip;
}

string DrCOMAuth::getMac() {
	return m_strMac;
}

int DrCOMAuth::httpLogout() {
	int iRet = Network_connection_interruption_check_the_network_configuration_please;
	char cRep[DrCOM_BUFFER_256B] = {'\0'};
	int iHttpCode = 0, iContentLen = 0;
	string strHttpServerName = "", strHttpReLocal = "";
	tcpSocket tSocket;

	bzero(m_cBuffer, sizeof(m_cBuffer));
	if (tSocket.Connect(m_strGatewayAddress) == 1) {
		sprintf(m_cBuffer, DrCOM_GET, DrCOM_Logout, m_strGatewayAddress.c_str());
		if (tSocket.SendData(m_cBuffer, strlen(m_cBuffer)) == strlen(m_cBuffer)) {
			bzero(m_cBuffer, sizeof(m_cBuffer));
			if (httpHandle(&tSocket, iHttpCode, iContentLen, strHttpServerName, strHttpReLocal, (char*)m_cBuffer) == 1) {
				iRet = DrCOM_SUCCESS;
				m_bConnected = false;
			}
		}
	}

	return iRet;
}

string DrCOMAuth::getGatewayAddress() {
	return m_strGatewayAddress;
}

bool DrCOMAuth::getLoginStatus() {
	return m_bConnected;
}

bool DrCOMAuth::httpStatus() {
	bool bRet = false;
	char cRep[DrCOM_BUFFER_256B] = {'\0'};
	int iHttpCode = 0, iContentLen = 0;
	string strHttpServerName = "", strHttpReLocal = "";
	tcpSocket tSocket;

	bzero(m_cBuffer, sizeof(m_cBuffer));
	if (tSocket.Connect(m_strGatewayAddress) == 1) {
		sprintf(m_cBuffer, DrCOM_GET, "/", m_strGatewayAddress.c_str());
		if (tSocket.SendData(m_cBuffer, strlen(m_cBuffer)) == strlen(m_cBuffer)) {
			bzero(m_cBuffer, sizeof(m_cBuffer));
			if (httpHandle(&tSocket, iHttpCode, iContentLen, strHttpServerName, strHttpReLocal, (char*)m_cBuffer) == 1) {
				if ((iHttpCode == DrCOM_HTTP_200) && ((strHttpServerName == DrCOM_Server) || (strHttpServerName == DrCOM_Server_IIS)) && strstr(m_cBuffer, DrCOM_1_html)) {
					string strTime = findStringBetween(m_cBuffer, (char*)"time='", (char*)"';flow", cRep, sizeof(cRep));
					string strFlow = findStringBetween(m_cBuffer, (char*)"flow='", (char*)"';fsele", cRep, sizeof(cRep));
					if ((strTime.length() > 0) && (strFlow.length() > 0)) {
						m_strTime = trim(strTime);
						strFlow = trim(strFlow);
						//copy from web script
						int iflow = atoi(strFlow.c_str());
						int iflow0 = 0;
						//int iflow1 = 0;
						iflow0 = iflow % 1024;
						//iflow1 = iflow - iflow0;
						iflow0 = iflow0 * 1000;
						iflow0 = iflow0 - iflow0 % 1024;
						string flow3;
						if ((iflow0 / 1024) < 10) {
							flow3 = ".00";
						} else if ((iflow0 / 1024) < 100) {
							flow3 = ".0";
						} else {
							flow3 = ".";
						}
						sprintf(cRep, "%d%s%d", (int)(iflow/1024), flow3.c_str(), (int)(iflow0/1024));
						m_strFlux = cRep;
						bRet = true;
					}
				}
			}
		}
	}

	return bRet;
}

string DrCOMAuth::getFluxStatus() {
	return m_strFlux;
}

string DrCOMAuth::getTimeStatus() {
	return m_strTime;
}

int DrCOMAuth::httpHandle(tcpSocket* ptSocket, int& iHttpCode, int& iContentLen, string& strHttpServerName, string& strHttpReLocal, char* pHttpBody) {
	iHttpCode = 0;
	strHttpServerName = "";
	iContentLen = 0;

	int iRet = -1;
	bool bFlag = true;
	int iRLen = 0, iBufferLen = DrCOM_BUFFER_64K;
	char *pHttpEnd = NULL;
	char cRep[DrCOM_BUFFER_256B] = {'\0'};

	while (true) {
		int iLen = ptSocket->RecvData(pHttpBody + iRLen, iBufferLen - iRLen, false);
		if (iLen == -1) {
			break;
		}
		if (iLen > 0) {
			iRLen += iLen;
		}
		//find http end "\r\n\r\n"
		pHttpEnd = strIstr(pHttpBody, DrCOM_HTTP_END);
		if (pHttpEnd) {
			//get http reponse code
			bzero(cRep, sizeof(cRep));
			memcpy(cRep, pHttpBody + 9, 3);
			iHttpCode = atoi(cRep);
			//get content length begin
			iContentLen = atoi(findStringBetween(pHttpBody, (char*)DrCOM_HTTP_CON_LEN, (char*)DrCOM_HTTP_LINE_END, cRep, sizeof(cRep)).c_str());
			//get http server name
			strHttpServerName = findStringBetween(pHttpBody, (char*)DrCOM_HTTP_SERVER, (char*)DrCOM_HTTP_LINE_END, cRep, sizeof(cRep));
			//get location while response code is 302
			if (iHttpCode == DrCOM_HTTP_302) {
				strHttpReLocal = findStringBetween(pHttpBody, (char*)DrCOM_HTTP_LOCATION, (char*)DrCOM_HTTP_LINE_END, cRep, sizeof(cRep));
				int index = strHttpReLocal.find("/");
				if (index > 0) {
					strHttpReLocal = strHttpReLocal.substr(0, index - 1);
				}
			}
			iRet = 1;
			break;
		}
	}

	if (iContentLen > 0) {
		iRet = -1;
		int iHeaderLen = (pHttpEnd - pHttpBody) + strlen(DrCOM_HTTP_END);
		if ((iRLen - iHeaderLen) > 0) {
			iRLen = iRLen - iHeaderLen;
			memcpy(pHttpBody, pHttpBody + iHeaderLen, iRLen);
			bzero(pHttpBody + iRLen, iHeaderLen);
		} else {
			iRLen = 0;
		}

		while (iRLen < iContentLen) {
			int iLen = ptSocket->RecvData(pHttpBody + iRLen, iContentLen - iRLen);
			if (iLen == -1) {
				break;
			}
			if (iLen > 0) {
				iRLen += iLen;
			}
		}
		if (iRLen >= iContentLen) {
			iRet = 1;
		}
	}
	return iRet;
}

inline char* DrCOMAuth::strIstr(const char *haystack, const char *needle) {
	if (!*needle) {
		return (char*)haystack;
	}
	for (; *haystack; ++haystack) {
		if (toupper(*haystack) == toupper(*needle)) {
			const char *h, *n;
			for (h = haystack, n = needle; *h && *n; ++h, ++n) {
				if (toupper(*h) != toupper(*n)) {
					break;
				}
			}
			if (!*n) {
				return (char*)haystack;
			}
		}
	}
	return 0;
}

inline string DrCOMAuth::findStringBetween(char* pData, char* pBegin, char* pEnd, char* pTmpBuffer, int iTmpLen) {
	string strRet = "";
	char *pC_Begin = NULL, *pC_End = NULL, *pRep = NULL;
	int iLen = DrCOM_BUFFER_256B;

	if (pTmpBuffer && iTmpLen > 0) {
		pRep = pTmpBuffer;
		iLen = iTmpLen;
	} else {
		pRep = new char[DrCOM_BUFFER_256B];
	}
	bzero(pRep, iLen);

	if ((pC_Begin = strIstr(pData, pBegin)) > 0) {
		pC_Begin = pC_Begin + strlen(pBegin);
		if ((pC_End = strIstr(pC_Begin, pEnd)) > 0) {
			memcpy(pRep, pC_Begin, pC_End - pC_Begin);
			strRet = pRep;
		}
	}

	if (!pTmpBuffer || iTmpLen <= 0) {
		delete pRep;
	}

	return strRet;
}

inline string DrCOMAuth::grantMD5(string strData) {
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

inline string DrCOMAuth::trim(string strData) {
	return strData.erase(0, strData.find_first_not_of(" \t\r\n")).erase(strData.find_last_not_of(" \t\r\n") + 1);
}

int DrCOMAuth::doWithLoginResult(char* pData) {
	m_strUndefineError = "";
	m_strXip = "";
	m_strMac = "";

	int iRet = -1;
	char cRep[DrCOM_BUFFER_256B] = {'\0'};

	if (strstr(m_cBuffer, DrCOM_2_html)) {
		string strMsg = findStringBetween(pData, (char*)"Msg=", (char*)";time", cRep, sizeof(cRep));
		string strMsga = findStringBetween(pData, (char*)"msga='", (char*)"';", cRep, sizeof(cRep));
		string strXip = findStringBetween(pData, (char*)"xip=", (char*)";mac", cRep, sizeof(cRep));
		string strMac = findStringBetween(pData, (char*)"mac=", (char*)";va", cRep, sizeof(cRep));
		string strTime = findStringBetween(pData, (char*)"time='", (char*)"';flow", cRep, sizeof(cRep));
		string strFlow = findStringBetween(pData, (char*)"flow='", (char*)"';fsele", cRep, sizeof(cRep));
		string strCode = findStringBetween(pData, (char*)"mcode = ", (char*)";", cRep, sizeof(cRep));
		iRet = loginStatus(strMsg, strMsga, strXip, strMac, strTime, strFlow, strCode);
	} else if (strstr(m_cBuffer, DrCOM_3_html)) {
		iRet = DrCOM_SUCCESS;
	} else {
		iRet = Can_not_find_Dr_COM_web_protocol;
	}

	if (iRet == DrCOM_SUCCESS) {
		//connect to update server
		pthread_t th;
		pthread_create(&th, NULL, &requestUpdate, (void*)this);
	}

	return iRet;
}

int DrCOMAuth::loginStatus(string strMsg, string strMsga, string strXip, string strMac, string strTime, string strFlow, string strCode) {
	int iRet = DrCOM_SUCCESS;

	if (strMsg.length() > 1) {
		if (strMsg == "00" || strMsg == "01") {
			if (strMsga.length() > 0) {
				if (strMsga == "error0") {
					iRet = The_IP_does_not_allow_login_base_Dr_COM_web_technology;
				} else if (strMsga == "error1") {
					iRet = The_account_does_not_allow_login_base_Dr_COM_web_technology;
				} else if (strMsga == "error2") {
					iRet = The_account_does_not_allow_change_password;
				} else {
					m_strUndefineError = strMsga;
					iRet = Undefine_error;
				}
			} else {
				iRet = Invalid_account_or_password_please_login_again;
			}
		} else if (strMsg == "02") {
			m_strXip = strXip;
			m_strMac = strMac;
			iRet = This_account_is_tie_up_please_contact_network_administration_IP_MAC;
		} else if (strMsg == "03") {
			m_strXip = strXip;
			iRet = This_account_use_on_appointed_address_only_IP;
		} else if (strMsg == "04") {
			iRet = This_account_charge_be_overspend_or_flux_over;
		} else if (strMsg == "05") {
			iRet = This_account_be_break_off;
		} else if (strMsg == "06") {
			iRet = System_buffer_full;
		} else if (strMsg == "08") {
			iRet = This_account_is_tie_up_can_not_amend;
		} else if (strMsg == "09") {
			iRet = The_new_and_the_confirm_password_are_differ_can_not_amend;
		} else if (strMsg == "10") {
			iRet = The_password_amend_successed;
		} else if (strMsg == "11") {
			m_strMac = strMac;
			iRet = This_account_use_on_appointed_address_only_MAC;
		} else if (strMsg == "15") {
			iRet = DrCOM_SUCCESS;
		}
	} else {
		if (strCode == "0000" || strCode == "FFFF") {
			iRet = DrCOM_SUCCESS;
		}
	}

	return iRet;
}

string DrCOMAuth::grantUpdateRequest() {
	char cBuffer[DrCOM_BUFFER_512B] = {'\0'};
	//grant time
	string strTime = "";
	time_t stm = time(NULL);
	struct tm tTime;
	localtime_r(&stm,&tTime);
	sprintf(cBuffer, "%04d%02d%02d%02d%02d%02d", tTime.tm_year+1900, tTime.tm_mon+1, tTime.tm_mday, tTime.tm_hour, tTime.tm_min, tTime.tm_sec);
	strTime = cBuffer;

	//grant time
	string strType = "";
	int iType = (int)time(NULL) % 3;
	switch (iType) {
		case 0: strType = DrCOM_HTTP_TYPE_0000; break;
		case 1: strType = DrCOM_HTTP_TYPE_0003; break;
		case 2: strType = DrCOM_HTTP_TYPE_0006; break;
	}

	//grant key
	string strKey = "";

	//grant hash & version
	string strHash = DrCOM_Hash;
	string strVer = DrCOM_Version;

	//grant rand
	int iRnd = rand() % 1000000;
	sprintf(cBuffer, "%06d", iRnd);
	string strRnd = cBuffer;

	//grant chk
	string strChk = "";
	string strTmp = "";

	if (strType == DrCOM_HTTP_TYPE_0000) {
		strTmp = strTime+  strHash + strVer + strKey;
		strChk = grantMD5(strTmp);
		strChk = strChk.substr(strChk.length() - 8, 8);
	} else {
		strTmp = strTime + strHash + strVer + strKey + strRnd;
		if (strType == DrCOM_HTTP_TYPE_0003) {
			Arithmetic ath;
			unsigned long ulChk = ath.MakeCRC32((char*)strTmp.c_str(), strTmp.length());
			sprintf(cBuffer, "%u", ulChk);
			strChk = cBuffer;
		} else if (strType == DrCOM_HTTP_TYPE_0006) {
			strChk = grantMD5(strTmp);
			strChk = strChk.substr(strChk.length() - 8, 8);
		}
	}

	//get mac hash
	string strMac = grantMD5(tcpSocket::GetFirstMacAddress());
	sprintf(cBuffer, DrCOM_Update_Parm, strTime.c_str(), strType.c_str(), strKey.c_str(), strHash.c_str(), strVer.c_str(), strRnd.c_str(), strChk.c_str(), strMac.c_str());
	string strRet = cBuffer;
	return strRet;
}

void* DrCOMAuth::requestUpdate(void* pData) {
	if (!pData) {
		return NULL;
	}
	DrCOMAuth* pDrCOMAuth = (DrCOMAuth*)pData;
	tcpSocket tSocket;

	char cBuffer[DrCOM_BUFFER_1K] = {'\0'};
	bzero(cBuffer, sizeof(cBuffer));
	if (tSocket.Connect(DrCOM_Update_Svr) == 1) {
		sprintf(cBuffer, DrCOM_GET, pDrCOMAuth->grantUpdateRequest().c_str(), DrCOM_Update_Svr);
		if (tSocket.SendData(cBuffer, strlen(cBuffer)) == strlen(cBuffer)) {
		}
	}
	tSocket.Close();
	return NULL;
}
