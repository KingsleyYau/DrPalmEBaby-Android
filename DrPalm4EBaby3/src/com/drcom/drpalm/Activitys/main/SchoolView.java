package com.drcom.drpalm.Activitys.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drcom.drpalm.GlobalVariables;
import com.drcom.drpalm.Activitys.consultation.ConsultationActivity;
import com.drcom.drpalm.Activitys.news.NewsActivity;
import com.drcom.drpalm.Activitys.news.album.AlbumActivity;
import com.drcom.drpalm.Activitys.news.bookmark.NewsBookmarkListActivity;
import com.drcom.drpalm.Activitys.news.search.NewsSearchListActivity;
import com.drcom.drpalm.Activitys.tours.ToursAcitivity;
import com.drcom.drpalm.Definition.ModuleNameDefine;
import com.drcom.drpalm.Definition.RequestCategoryID;
import com.drcom.drpalm.Tool.LanguageManagement;
import com.drcom.drpalm.Tool.LanguageManagement.CurrentLan;
import com.drcom.drpalm.objs.Block;
import com.drcom.drpalm.objs.MainMenuItem;
import com.drcom.drpalmebaby.R;

public class SchoolView extends LinearLayout {
	private MainAdapter mMainAdapter;

	private TextView masterEmail;
	private RelativeLayout mLayout_top;
	
	private boolean showMasterEmail = false;
	private Button consultation;
	private List<MainMenuItem> mMyGVList = new ArrayList<MainMenuItem>();

	public SchoolView(final Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.main_school_view, this);

		masterEmail = (TextView) findViewById(R.id.masterEmail);
		mLayout_top = (RelativeLayout)findViewById(R.id.Layout_top);
		if(LanguageManagement.getSysLanguage(context) == CurrentLan.COMPLES_CHINESE){
			mLayout_top.setBackgroundResource(R.drawable.school_msgtitle_1_hk);
		}else if(LanguageManagement.getSysLanguage(context) == CurrentLan.ENGLISH){
			mLayout_top.setBackgroundResource(R.drawable.school_msgtitle_1_en);
		}
		
		consultation = (Button) findViewById(R.id.button3);
		consultation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(context, ConsultationActivity.class);
				context.startActivity(intent);
			}
		});

		mMainAdapter = new MainAdapter(context, mMyGVList);

		GridView myGVgridview = (GridView) findViewById(R.id.mainschool_gridview);
		// 添加元素给gridview
		myGVgridview.setAdapter(mMainAdapter);
		myGVgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		myGVgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent();
				MainMenuItem item = (MainMenuItem) arg0.getItemAtPosition(arg2);
				if (item.getId() == 0) {
					// Tours
					intent.setClass(context, ToursAcitivity.class);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.NEWS_NEWS_ID) {
					// 新闻列表3001
					intent.putExtra(NewsActivity.KEY_CATEGORY, RequestCategoryID.NEWS_NEWS_ID);
					intent.setClass(context, NewsActivity.class);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.NEWS_ACTIVITY_ID) {
					// 通告列表1001
					intent.putExtra(NewsActivity.KEY_CATEGORY, RequestCategoryID.NEWS_ACTIVITY_ID);
					intent.setClass(context, NewsActivity.class);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.NEWS_ALBUM_ID) {
					// 相册列表
					intent.setClass(context, AlbumActivity.class);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.NEWS_INFANTDIET_ID) {
					// 食谱列表2001
					intent.putExtra(NewsActivity.KEY_CATEGORY, RequestCategoryID.NEWS_INFANTDIET_ID);
					intent.setClass(context, NewsActivity.class);
					context.startActivity(intent);
				} else if (item.getId() == RequestCategoryID.NEWS_PARENTING_ID) {
					// 育儿列表4001
					intent.putExtra(NewsActivity.KEY_CATEGORY, RequestCategoryID.NEWS_PARENTING_ID);
					intent.setClass(context, NewsActivity.class);
					context.startActivity(intent);
				}
				// item.setCount("0");
				// mMainAdapter.notifyDataSetChanged();
			}
		});

		Button button_bookmark = (Button) findViewById(R.id.buttonBookmark);
		button_bookmark.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, NewsBookmarkListActivity.class);
				context.startActivity(i);
			}
		});

		Button button_search = (Button) findViewById(R.id.buttonSearch);
		button_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, NewsSearchListActivity.class);
				context.startActivity(i);
			}
		});

		initData();
	}

	/**
	 * 初始化 按钮数据
	 */
	private void initData() {
		MainMenuItem mi1 = new MainMenuItem();
		mi1.setId(0);
		mi1.setCount("0");
		mi1.setName(getResources().getString(R.string.tours));
		mi1.setPicResId(R.drawable.icon_school_tours_selector);

		MainMenuItem mi2 = new MainMenuItem();
		mi2.setId(RequestCategoryID.NEWS_NEWS_ID);
		mi2.setCount("0");
		mi2.setName(ModuleNameDefine.getNewsModuleNamebyId(mi2.getId()));
		mi2.setPicResId(R.drawable.icon_school_news_selector);

		MainMenuItem mi3 = new MainMenuItem();
		mi3.setId(RequestCategoryID.NEWS_ACTIVITY_ID);
		mi3.setCount("0");
		mi3.setName(ModuleNameDefine.getNewsModuleNamebyId(mi3.getId()));
		mi3.setPicResId(R.drawable.icon_school_events_selector);

		MainMenuItem mi4 = new MainMenuItem();
		mi4.setId(RequestCategoryID.NEWS_ALBUM_ID);
		mi4.setCount("0");
		mi4.setName(ModuleNameDefine.getNewsModuleNamebyId(mi4.getId()));
		mi4.setPicResId(R.drawable.icon_school_album_selector);

		MainMenuItem mi5 = new MainMenuItem();
		mi5.setId(RequestCategoryID.NEWS_INFANTDIET_ID);
		mi5.setCount("0");
		mi5.setName(ModuleNameDefine.getNewsModuleNamebyId(mi5.getId()));
		mi5.setPicResId(R.drawable.icon_school_infant_diet_selector);

		MainMenuItem mi6 = new MainMenuItem();
		mi6.setId(RequestCategoryID.NEWS_PARENTING_ID);
		mi6.setCount("0");
		mi6.setName(ModuleNameDefine.getNewsModuleNamebyId(mi6.getId()));
		mi6.setPicResId(R.drawable.icon_school_parenting_selector);

		// 按钮顺序
		for (Block iterable_element : GlobalVariables.blocks) {
			if ("school".equals(iterable_element.getType())) {

				if (RequestCategoryID.NEWS_NEWS_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi2.setShow(true);
						continue;
					}
				}
				if (RequestCategoryID.NEWS_ACTIVITY_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi3.setShow(true);
						continue;
					}
				}
				if (RequestCategoryID.NEWS_ALBUM_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi4.setShow(true);
						continue;
					}
				}
				if (RequestCategoryID.NEWS_INFANTDIET_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi5.setShow(true);
						continue;
					}
				}
				if (RequestCategoryID.NEWS_PARENTING_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						mi6.setShow(true);
						continue;
					}
				}	if (RequestCategoryID.MASTEREMAIL_ID == iterable_element.getId()) {
					if (iterable_element.isVisible()) {
						showMasterEmail = true;
						continue;
					}
				}
			}
		}
		mMyGVList.add(mi1);
		if (mi3.isShow())
			mMyGVList.add(mi3);
		if (mi5.isShow())
			mMyGVList.add(mi5);
		if (mi2.isShow())
			mMyGVList.add(mi2);
		if (mi6.isShow())
			mMyGVList.add(mi6);
		if (mi4.isShow())
			mMyGVList.add(mi4);
		if (!showMasterEmail) {
			consultation.setVisibility(View.GONE);
			masterEmail.setVisibility(View.GONE);
		}

	}

	/**
	 * 刷新各按钮是否有NEW的状态
	 *
	 * @param ids
	 */
	public void ReflashItemstatus(List<Integer> ids) {
		for (int i = 0; i < mMyGVList.size(); i++) {
			for (int j = 0; j < ids.size(); j++) {
				if (mMyGVList.get(i).getId() == (int) ids.get(j)) {
					mMyGVList.get(i).setCount("New");
				}
			}
		}

		mMainAdapter.notifyDataSetChanged();
	}

	/**
	 * 刷新一个按钮是否有NEW的状态
	 *
	 * @param ids
	 */
	public void ReflashItemstatus(int channleid, boolean isnew) {
		for (int i = 0; i < mMyGVList.size(); i++) {
			if (mMyGVList.get(i).getId() == channleid) {
				if (isnew)
					mMyGVList.get(i).setCount("New");
				else
					mMyGVList.get(i).setCount("0");
			}
		}

		mMainAdapter.notifyDataSetChanged();
	}
}
