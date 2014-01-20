/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_drcom_drpalm_Tool_service_DrServiceJni */

#ifndef _Included_com_drcom_drpalm_Tool_service_DrServiceJni
#define _Included_com_drcom_drpalm_Tool_service_DrServiceJni
#ifdef __cplusplus
extern "C" {
#endif
#undef com_drcom_drpalm_Tool_service_DrServiceJni_TIME_FOR_CLEAN_JNI_LOG
#define com_drcom_drpalm_Tool_service_DrServiceJni_TIME_FOR_CLEAN_JNI_LOG 864000000i64
/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    NativeInit
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_NativeInit
  (JNIEnv *, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetFirstMacAddress
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetFirstMacAddress
  (JNIEnv *, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetDeviceId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetDeviceId
  (JNIEnv *, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetNetworkGate
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetNetworkGate
  (JNIEnv *, jobject, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetTours
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetTours
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    LoginGateway
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_LoginGateway
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    Logout
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_Logout
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    PushInfo
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZLjava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_PushInfo
  (JNIEnv *, jobject, jstring, jstring, jstring, jboolean, jboolean, jboolean, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    KeepAlive
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_KeepAlive
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitProblem
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitProblem
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SetUserEmail
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SetUserEmail
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetNewsModuleInfoList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetNewsModuleInfoList
  (JNIEnv *, jobject, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetEventsModuleInfoList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetEventsModuleInfoList
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetLastUpdate
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetLastUpdate
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jboolean, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetSchoolList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetSchoolList
  (JNIEnv *, jobject, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SearchSchool
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SearchSchool
  (JNIEnv *, jobject, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetNews
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetNews
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetNewsDetail
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetNewsDetail
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SearchNews
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SearchNews
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    PutConSult
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_PutConSult
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetEventList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetEventList
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetEventDetail
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetEventDetail
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetPublishEventList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetPublishEventList
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetPublishEventDetail
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetPublishEventDetail
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitEvent4Kids
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitEvent4Kids
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jstring, jobject, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    AutoAnswer
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_AutoAnswer
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    AutoAnswerList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_AutoAnswerList
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetAnswerList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetAnswerList
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetReplyInfo
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetReplyInfo
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    ReplyPost
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_ReplyPost
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetOrganization
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetOrganization
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetContactMsgs
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetContactMsgs
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SendContactMsg
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SendContactMsg
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetSysMsgs
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetSysMsgs
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetSysMsgContent
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetSysMsgContent
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetEventReadInfo
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetEventReadInfo
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetClassFavorite
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetClassFavorite
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitClassfav
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitClassfav
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetAccountinfo
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetAccountinfo
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitAccountinfo
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitAccountinfo
  (JNIEnv *, jobject, jstring, jstring, jstring, jbyteArray, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetNavigation
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetNavigation
  (JNIEnv *, jobject, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetUseralbum
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetUseralbum
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitUseralbum
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitUseralbum
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetGrowdiary
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetGrowdiary
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitGrowdiary
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitGrowdiary
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetNavigationList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetNavigationList
  (JNIEnv *, jobject, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitDelEvent
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitDelEvent
  (JNIEnv *, jobject, jstring, jstring, jstring, jstring, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    GetContactList
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_GetContactList
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject, jobject);

/*
 * Class:     com_drcom_drpalm_Tool_service_DrServiceJni
 * Method:    SubmitClassreview
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/ArrayList;Lcom/drcom/drpalm/Tool/service/DrServiceJniCallback;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_drcom_drpalm_Tool_service_DrServiceJni_SubmitClassreview
  (JNIEnv *, jobject, jstring, jstring, jstring, jobject, jobject);

#ifdef __cplusplus
}
#endif
#endif
