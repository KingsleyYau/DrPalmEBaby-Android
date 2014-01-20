package com.drcom.drpalm.Activitys.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drcom.drpalm.GlobalVariables;
import com.drcom.drpalm.Activitys.events.EventsListActivity;
import com.drcom.drpalm.Activitys.events.NewEventActivity;
import com.drcom.drpalm.Activitys.events.album.ClassAlbumActivity;
import com.drcom.drpalm.Activitys.events.bookmark.EventsBookmarkListActivity;
import com.drcom.drpalm.Activitys.events.draft.EventsDraftListActivity;
import com.drcom.drpalm.Activitys.events.face2face.MemberListActivity;
import com.drcom.drpalm.Activitys.events.search.EventsSearchListActivity;
import com.drcom.drpalm.Activitys.events.sent.EventsSentListActivity;
import com.drcom.drpalm.Activitys.events.video.ClassVideolistActivity;
import com.drcom.drpalm.Activitys.sysinfo.SysinfoListActivity;
import com.drcom.drpalm.Definition.ModuleNameDefine;
import com.drcom.drpalm.Definition.RequestCategoryID;
import com.drcom.drpalm.Tool.LanguageManagement;
import com.drcom.drpalm.Tool.LanguageManagement.CurrentLan;
import com.drcom.drpalm.Tool.XmlParse.UserInfo;
import com.drcom.drpalm.View.controls.MyGridView;
import com.drcom.drpalm.View.setting.SettingManager;
import com.drcom.drpalm.objs.Block;
import com.drcom.drpalm.objs.MainMenuItem;
import com.drcom.drpalm.objs.UpdateTimeItem;
import com.drcom.drpalmebaby.R;

public class ClassView extends LinearLayout {
	public static int CHANGE_SUM = 0;

	private MainAdapter mMainAdapter;
	private MainAdapter mMainAdapter2;

	private Context mContext;
	private List<MainMenuItem> mMyGVList1 = new ArrayList<MainMenuItem>();
	private List<MainMenuItem> mMyGVList2 = new ArrayList<MainMenuItem>();
//	private Handler mHandler;

	private MyGridView myGVgridview1;
	private MyGridView myGVgridview2;
	private RelativeLayout mRelativeLayoutMsg;
	private LinearLayout mLinearLayoutGird2;
	private RelativeLayout mLayout_top;
	private RelativeLayout mLayout_middle;

	public ClassView(final Context context) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.main_class_view, this);

		mRelativeLayoutMsg = (RelativeLayout) findViewById(R.id.Layout_middle);
		mLinearLayoutGird2 = (LinearLayout)findViewById(R.id.mainclass_Layout_gridview2);

		mMainAdapter = new MainAdapter(context, mMyGVList1);
		mMainAdapter2 = new MainAdapter(context, mMyGVList2);
		
		mLayout_top = (RelativeLayout)findViewById(R.id.Layout_top);
		if(LanguageManagement.getSysLanguage(context) == CurrentLan.COMPLES_CHINESE){
			mLayout_top.setBackgroundResource(R.drawable.class_msgtitle_1_hk);
		}else if(LanguageManagement.getSysLanguage(context) == CurrentLan.ENGLISH){
			mLayout_top.setBackgroundResource(R.drawable.class_msgtitle_1_en);
		}
		mLayout_middle = (RelativeLayout)findViewById(R.id.Layout_middle);
		if(LanguageManagement.getSysLanguage(context) == CurrentLan.COMPLES_CHINESE){
			mLayout_middle.setBackgroundResource(R.drawable.class_msgtitle_2_hk);
		}else if(LanguageManagement.getSysLanguage(context) == CurrentLan.ENGLISH){
			mLayout_middle.setBackgroundResource(R.drawable.class_msgtitle_2_en);
		}

		myGVgridview1 = (MyGridView) findViewById(R.id.mainclass_gridview1);
		// 添加元素给gridview
		myGVgridview1.setAdapter(mMainAdapter);
		myGVgridview1.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myGVgridview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MainMenuItem item = (MainMenuItem) arg0.getItemAtPosition(arg2);
				if (item.getId() == RequestCategoryID.EVENTS_NEWS_ID) {
					Intent intent = new Intent();
					intent.setClass(context, EventsListActivity.class);
					intent.putExtra(EventsListActivity.KEY_CATEGORYID, RequestCategoryID.EVENTS_NEWS_ID);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.EVENTS_ACTIVITY_ID) {
					Intent intent = new Intent();
					intent.setClass(context, EventsListActivity.class);
					intent.putExtra(EventsListActivity.KEY_CATEGORYID, RequestCategoryID.EVENTS_ACTIVITY_ID);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.EVENTS_COMMENT_ID) {
					Intent intent = new Intent();
					intent.setClass(context, EventsListActivity.class);
					intent.putExtra(EventsListActivity.KEY_CATEGORYID, RequestCategoryID.EVENTS_COMMENT_ID);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.EVENTS_ALBUM_ID) {
					Intent intent = new Intent();
					intent.setClass(context, ClassAlbumActivity.class);
					intent.putExtra(ClassAlbumActivity.CATEGORYID_KEY, RequestCategoryID.EVENTS_ALBUM_ID);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.EVENTS_COURSEWARE_ID) {
					Intent intent = new Intent();
					intent.setClass(context, EventsListActivity.class);
					intent.putExtra(EventsListActivity.KEY_CATEGORYID, RequestCategoryID.EVENTS_COURSEWARE_ID);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.EVENTS_COMMUNION_ID) {
					Intent intent = new Intent();
					intent.setClass(context, MemberListActivity.class);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.EVENTS_VIDEO_ID) {
					Intent intent = new Intent();
					intent.setClass(context, ClassVideolistActivity.class);
					intent.putExtra(ClassVideolistActivity.CATEGORYID_KEY, RequestCategoryID.EVENTS_VIDEO_ID);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.EVENTS_INOUT_ID) {
					showExitMessage(mContext.getResources().getString(R.string.nofunction));
				}
				// 发送未读数到主Activity中
				// Message message = Message.obtain();
				// message.arg1 = CHANGE_SUM;
				// message.obj = Integer.valueOf(item.getCount());
				// mHandler.sendMessage(message) ;
				//
				// item.setCount("0");
				//
				// mMainAdapter.notifyDataSetChanged();
			}
		});

		myGVgridview2 = (MyGridView) findViewById(R.id.mainclass_gridview2);
		// 添加元素给gridview
		myGVgridview2.setAdapter(mMainAdapter2);
		myGVgridview2.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myGVgridview2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MainMenuItem item = (MainMenuItem) arg0.getItemAtPosition(arg2);
				if (item.getId() == RequestCategoryID.EVENTS_SEND_ID) {
					Intent intent = new Intent();
					intent.setClass(context, EventsSentListActivity.class);
					context.startActivity(intent);
				} else if (item.getId() == 7) {
					Intent intent = new Intent();
					intent.setClass(context, EventsDraftListActivity.class);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.SYSINFO_ID) {
					Intent intent = new Intent();
					intent.setClass(context, SysinfoListActivity.class);
					context.startActivity(intent);
				}
			}
		});

		//新通告
		Button button_newevent = (Button) findViewById(R.id.button_newevent);
		button_newevent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, NewEventActivity.class);
				context.startActivity(i);
			}
		});

		//查看收藏
		Button button_bookmark = (Button) findViewById(R.id.buttonBookmark);
		button_bookmark.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, EventsBookmarkListActivity.class);
				context.startActivity(i);
			}
		});

		//搜索通告
		Button button_search = (Button) findViewById(R.id.buttonSearch);
		button_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, EventsSearchListActivity.class);
				context.startActivity(i);
			}
		});

		initData();

		ReflashUI();
	}

	/**
	 * 初始化 按钮数据
	 */
	private void initData() {
		MainMenuItem mi1 = new MainMenuItem();
		mi1.setId(RequestCategoryID.EVENTS_NEWS_ID);
		mi1.setCount("0");
		mi1.setName(ModuleNameDefine.getEventsModuleNamebyId(mi1.getId()));
		mi1.setPicResId(R.drawable.icon_class_news_selector);

		MainMenuItem mi2 = new MainMenuItem();
		mi2.setId(RequestCategoryID.EVENTS_ACTIVITY_ID);
		mi2.setCount("0");
		mi2.setName(ModuleNameDefine.getEventsModuleNamebyId(mi2.getId()));
		mi2.setPicResId(R.drawable.icon_class_activitys_selector);

		MainMenuItem mi3 = new MainMenuItem();
		mi3.setId(RequestCategoryID.EVENTS_COMMENT_ID);
		mi3.setCount("0");
		mi3.setName(ModuleNameDefine.getEventsModuleNamebyId(mi3.getId()));
		mi3.setPicResId(R.drawable.icon_class_comment_selector);

		MainMenuItem mi4 = new MainMenuItem();
		mi4.setId(RequestCategoryID.EVENTS_ALBUM_ID);
		mi4.setCount("0");
		mi4.setName(ModuleNameDefine.getEventsModuleNamebyId(mi4.getId()));
		mi4.setPicResId(R.drawable.icon_class_album_selector);

		MainMenuItem mi5 = new MainMenuItem();
		mi5.setId(RequestCategoryID.EVENTS_COURSEWARE_ID);
		mi5.setCount("0");
		mi5.setName(ModuleNameDefine.getEventsModuleNamebyId(mi5.getId()));
		mi5.setPicResId(R.drawable.icon_class_courseware_selector);

		MainMenuItem mi6 = new MainMenuItem();
		mi6.setId(RequestCategoryID.EVENTS_COMMUNION_ID);
		mi6.setCount("0");
		mi6.setName(ModuleNameDefine.getEventsModuleNamebyId(mi6.getId()));
		mi6.setPicResId(R.drawable.icon_class_face2face_selector);

		MainMenuItem mi7 = new MainMenuItem();
		mi7.setId(RequestCategoryID.EVENTS_SEND_ID);
		mi7.setCount("0");
		mi7.setName(ModuleNameDefine.getEventsModuleNamebyId(mi7.getId()));
		mi7.setPicResId(R.drawable.icon_class_sent_selector);

		MainMenuItem mi8 = new MainMenuItem();
		mi8.setId(7);
		mi8.setCount("0");
		mi8.setName(getResources().getString(R.string.draft));
		mi8.setPicResId(R.drawable.icon_class_draft_selector);

		MainMenuItem mi9 = new MainMenuItem();
		mi9.setId(RequestCategoryID.SYSINFO_ID);
		mi9.setCount("0");
		mi9.setName(ModuleNameDefine.getEventsModuleNamebyId(mi9.getId()));
		mi9.setPicResId(R.drawable.icon_class_sysinfo_selector);
		
		MainMenuItem mi10 = new MainMenuItem();
		mi10.setId(RequestCategoryID.EVENTS_VIDEO_ID);
		mi10.setCount("0");
		mi10.setName(ModuleNameDefine.getEventsModuleNamebyId(mi10.getId()));
		mi10.setPicResId(R.drawable.icon_class_video_selector);
		
		MainMenuItem mi11 = new MainMenuItem();
		mi11.setId(RequestCategoryID.EVENTS_INOUT_ID);
		mi11.setCount("0");
		mi11.setName(ModuleNameDefine.getEventsModuleNamebyId(mi11.getId()));
		mi11.setPicResId(R.drawable.icon_class_entryexit_selector);
		
		// 按钮顺序
		for (Block iterable_element : GlobalVariables.blocks) {
			if ("class".equals(iterable_element.getType())) {

				if (RequestCategoryID.EVENTS_NEWS_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi1.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_ACTIVITY_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi2.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_COMMENT_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi3.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_ALBUM_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi4.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_COURSEWARE_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi5.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_COMMUNION_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi6.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_VIDEO_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi10.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_SEND_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi7.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.SYSINFO_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi9.setShow(true);
						continue;
					}
				}else if (RequestCategoryID.EVENTS_INOUT_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi11.setShow(true);
						continue;
					}
				}
			}
		}
		// 按钮顺序
		//上边的图标
		if (mi1.isShow())
			mMyGVList1.add(mi1);
		if (mi3.isShow())
			mMyGVList1.add(mi3);
		if (mi5.isShow())
			mMyGVList1.add(mi5);
		if (mi2.isShow())
			mMyGVList1.add(mi2);
		if (mi4.isShow())
			mMyGVList1.add(mi4);
		if (mi10.isShow())
			mMyGVList1.add(mi10);
		if (mi6.isShow())
			mMyGVList1.add(mi6);
		if (mi11.isShow())
			mMyGVList1.add(mi11);
		
		//下边的图标
		if (mi7.isShow())
			mMyGVList2.add(mi7);
		mMyGVList2.add(mi8);
		if (mi9.isShow())
			mMyGVList2.add(mi9);
	}

	public void ReflashUI() {
		SettingManager settingInstance = SettingManager.getSettingManager(mContext);
		if (!settingInstance.getCurrentUserInfo().strUsrType.equals(UserInfo.USERTYPE_TEACHER)) {
			mRelativeLayoutMsg.setVisibility(View.GONE);
			mLinearLayoutGird2.setVisibility(View.GONE);
		} else {
			mRelativeLayoutMsg.setVisibility(View.VISIBLE);
			mLinearLayoutGird2.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 刷新各按钮的数字状态
	 *
	 * @param ids
	 */
	public void ReflashItemstatus(List<UpdateTimeItem> ids) {
		for (int i = 0; i < mMyGVList1.size(); i++) {
			for (int j = 0; j < ids.size(); j++) {
				if (mMyGVList1.get(i).getId() == ids.get(j).update_time_channel) {
					mMyGVList1.get(i).setCount(ids.get(j).update_unreadcount + "");
				}
			}
		}

		for (int i = 0; i < mMyGVList2.size(); i++) {
			for (int j = 0; j < ids.size(); j++) {
				if (mMyGVList2.get(i).getId() == ids.get(j).update_time_channel) {
					mMyGVList2.get(i).setCount(ids.get(j).update_unreadcount + "");
				}
			}
		}

		mMainAdapter.notifyDataSetChanged();
		mMainAdapter2.notifyDataSetChanged();
	}

//	/**
//	 * 消息句柄
//	 *
//	 * @param h
//	 */
//	public void setHandler(Handler h) {
//		this.mHandler = h;
//	}

	// /**
	// * 模块未读数减1
	// */
	// private Handler mHandlerModuleSumDesc = new Handler(){
	// @Override
	// public void handleMessage(Message msg) {
	// int type = msg.arg1;
	// for(int i = 0 ; i < mMyGVList1.size(); i ++){
	// if(mMyGVList1.get(i).getId() == type){
	// int sum = Integer.valueOf(mMyGVList1.get(i).getCount()) - 1;
	// mMyGVList1.get(i).setCount(sum + "");
	// }
	// }
	//
	// mMainAdapter.notifyDataSetChanged();
	// }
	// };
	
	/**
	 * 入园离园提示框(系统)
	 * 
	 * @param pMsg
	 */
	private void showExitMessage(String pMsg) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage(pMsg);
		builder.setPositiveButton(R.string.OK,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

//		builder.setNegativeButton(R.string.Cancel,
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});

		builder.create().show();
	}

}
