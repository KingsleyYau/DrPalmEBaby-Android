/*
 * File         : DrMutex.h
 * Date         : 2012-07-02
 * Author       : Kingsley Yau
 * Copyright    : City Hotspot Co., Ltd.
 * Description  : DrPalm DrMutex include
 */

#ifndef _INC_DRMUTEX_
#define _INC_DRMUTEX_
#include <pthread.h>
#include "DrLog.h"
using namespace std;
class DrMutex
{
	public:
	DrMutex(){
		initlock();
	}
	~DrMutex(){
		desrtoylock();
	}
	int trylock(){
		return pthread_mutex_trylock(&m_Mutex);
	}
	int lock(const char* ptag = "", int iThreadId = -1){
		showLog("Jni.DrMutex.lock", "%d, tag=%s, thread=%d", &m_Mutex, (ptag == NULL) ? "" : ptag, iThreadId);
		return pthread_mutex_lock(&m_Mutex);
	}
	int unlock(const char* ptag = "", int iThreadId = -1){
		showLog("Jni.DrMutex.unlock", "%d, tag=%s, thread=%d", &m_Mutex, (ptag == NULL) ? "" : ptag, iThreadId);
		return pthread_mutex_unlock(&m_Mutex);
	}
	protected:
	int initlock(){
		//m_Mutex = PTHREAD_MUTEX_INITIALIZER;
		showLog("Jni.DrMutex.initlock", "pthread_mutex_init");
		return pthread_mutex_init(&m_Mutex, NULL);
	}
	int desrtoylock(){
		return pthread_mutex_destroy(&m_Mutex);
	}
	private:
	pthread_mutex_t m_Mutex;
	pthread_mutexattr_t m_Mutexattr;
};
#endif
