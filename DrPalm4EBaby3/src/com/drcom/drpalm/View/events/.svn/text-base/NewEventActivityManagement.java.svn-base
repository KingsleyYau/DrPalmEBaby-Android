package com.drcom.drpalm.View.events;

import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.drcom.drpalm.GlobalVariables;
import com.drcom.drpalm.DB.EventsDB;
import com.drcom.drpalm.Tool.CommonTranslate.DateFormatter;
import com.drcom.drpalm.Tool.request.RequestOperation;
import com.drcom.drpalm.Tool.request.RequestOperationReloginCallback;
import com.drcom.drpalm.View.setting.SettingManager;
import com.drcom.drpalm.objs.EventDetailsItem;
import com.drcom.drpalm.objs.EventDraftItem;
import com.drcom.drpalm.objs.EventDraftItem.Attachment;

public class NewEventActivityManagement {
	private int SEND_SUCCEED = 1;
	private int SEND_FAILD = 0;
	
	private Context mContext;
	
	private EventsDB mEventsDB;
	private SettingManager setInstance;
	private String mUsername = "";
	private boolean isRequestRelogin = true; // 登录SECCION超时要重登录?
	
	public NewEventActivityManagement(Context c){
		mContext = c;
		setInstance = SettingManager.getSettingManager(c);
		mEventsDB = EventsDB.getInstance(c, GlobalVariables.gSchoolKey);
		mUsername = setInstance.getCurrentUserInfo().strUsrName;
	}
	
	/**
	 * 初始化编辑通告
	 * @param detailitem
	 * @return
	 */
	public EventDraftItem initEditDraf(EventDetailsItem detailitem ){
		EventDraftItem item = new EventDraftItem();
		item.old_eventid = detailitem.eventid;
		item.owner = detailitem.owner;
		item.ownerid = detailitem.ownerid;

		for (int i = 0; i < detailitem.imgs.size(); i++) {
			Attachment a = new Attachment();
			a.data = detailitem.imgs.get(i).imgData;
			a.description = detailitem.imgs.get(i).imgDescription;
			a.fileId = detailitem.imgs.get(i).fileId;
			a.fileType = detailitem.imgs.get(i).fileType;
			a.item = String.valueOf(detailitem.imgs.get(i).attid);
			a.type = "id";

			GlobalVariables.mListAttachmentData.add(a);
		}
		
		return item;
	}
	
	/**
	 * 保存草稿
	 * @param id
	 * @param title
	 * @param shortloc
	 * @param body
	 * @param type
	 * @param oristatus
	 * @param old_eventid
	 * @param bifeshow
	 * @param start
	 * @param end
	 * @param owner
	 * @param ownerid
	 */
	public void SaveDraf(Integer id,String title,String shortloc,String body,int type,String oristatus,Integer old_eventid,
			boolean bifeshow,Date start,Date end,String owner,String ownerid){
		EventDraftItem ditem = new EventDraftItem();
		ditem.pk_id = id;
		ditem.title = title;
		ditem.shortloc = shortloc;
		ditem.body = body;
		ditem.type = type;
		ditem.oristatus = oristatus;
		ditem.eventDraftAttachment = GlobalVariables.mListAttachmentData;
		
		if(old_eventid != null){	//如果是转发的通告保存为附件，则把在服务器的附件ID保存,下次从草稿中发送时同时发送
			ditem.old_eventid = old_eventid;
			
			if (GlobalVariables.mListAttachmentData.size() > 0) {
				ditem.isAttc = true;

				// 附件是已发通告的话,要将内容赋为空
				for (int i = 0; i < ditem.eventDraftAttachment.size(); i++) {
					if (ditem.eventDraftAttachment.get(i).type.equals("id")) {
						//如果附件是在服务器端，就不保存到草稿附件库
						ditem.eventDraftAttachment.get(i).data = null;
						GlobalVariables.mListAttachmentData.get(i).data = null;
					}
				}
			}
		}else{
			if (GlobalVariables.mListAttachmentData.size() > 0) {
				ditem.isAttc = false;

				// 附件是已发过的话,要将内容赋为空
				for (int i = 0; i < ditem.eventDraftAttachment.size(); i++) {
					if (ditem.eventDraftAttachment.get(i).type.equals("id")) {
						//如果附件是在服务器端，就不保存到草稿附件库
						ditem.eventDraftAttachment.get(i).data = null;
						GlobalVariables.mListAttachmentData.get(i).data = null;
					}else{
						//否则，保存到草稿附件库
						ditem.isAttc = true;
					}
				}
			}
		}


		ditem.bifeshow = bifeshow;
		ditem.user = mUsername;
		ditem.save = DateFormatter.getDateFromMilliSeconds(System.currentTimeMillis());
		ditem.start = start;
		ditem.end = end;
		ditem.owner = owner;
		ditem.ownerid = ownerid;

		mEventsDB.saveEventsDraftItem(ditem);
	}
	
	/**
	 * 
	 * @param h
	 * @param item
	 */
	public void SendEventRequest(final Handler h,final EventDraftItem item){
		RequestOperation mRequestOperation = RequestOperation.getInstance();
		RequestOperationReloginCallback callback = new RequestOperationReloginCallback() {
			@Override
			public void onSuccess() { // 请求数据成功
				Message message = Message.obtain();
				message.arg1 = SEND_SUCCEED;
				h.sendMessage(message);
				Log.i("zjj", "发送通告成功");
			}

			@Override
			public void onCallbackError(String str) {
				Message message = Message.obtain();
				message.arg1 = SEND_FAILD;
				message.obj = str;
				h.sendMessage(message);
				Log.i("zjj", "发送通告失败" + str);
			}

			@Override
			public void onReloginError() {
				// TODO Auto-generated method stub
				Log.i("zjj", "发送通告:自动重登录失败");
			}

			@Override
			public void onReloginSuccess() {
				// TODO Auto-generated method stub
				Log.i("zjj", "发送通告:自动重登录成功");
				if (isRequestRelogin) {
					SendEventRequest(h,item); // 自动登录成功后，再次请求数据
					isRequestRelogin = false;
				}
			}
		};
		
		System.gc();
		mRequestOperation.sendGetNeededInfo("SubmitEvent", new Object[] { item, callback }, callback.getClass().getName());
	}
}
