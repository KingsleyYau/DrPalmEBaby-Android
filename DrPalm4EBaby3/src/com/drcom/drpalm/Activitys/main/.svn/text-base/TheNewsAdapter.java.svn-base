package com.drcom.drpalm.Activitys.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.drcom.drpalm.Definition.RequestCategoryID;
import com.drcom.drpalm.objs.UpdateTimeItem;
import com.drcom.drpalmebaby.R;

public class TheNewsAdapter extends BaseAdapter {
	public static int TYPE_SCHOOL = 0;
	public static int TYPE_CLASS = 1;
	
	public int mType = TYPE_SCHOOL;
	
	private Context context;
	private LayoutInflater mLayoutInflater;
	private List<UpdateTimeItem> data;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
	private Date mDateToday = new Date();
	
	public TheNewsAdapter(Context context, List<UpdateTimeItem> data) {
		super();
		this.context = context;
		this.mLayoutInflater = LayoutInflater.from(context);
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder {
		ImageView icon;
		TextView sum;
		TextView moduleName;
		TextView title;
		TextView time;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		UpdateTimeItem item = data.get(position);
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.thenews_item, null);
			convertView.setTag(vh);
			
			vh.icon = (ImageView)convertView.findViewById(R.id.thenewsRowIV);
			vh.sum = (TextView)convertView.findViewById(R.id.thenews_number_txtv);
			vh.moduleName = (TextView)convertView.findViewById(R.id.thenewsModuleName_Txtview);
			vh.title = (TextView)convertView.findViewById(R.id.thenewsTitle_Txtview);
			vh.time = (TextView)convertView.findViewById(R.id.thenewsDate_Txtview);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.title.setText(item.update_title);
		
		if(item.update_time_last.getTime() == 0){
			vh.time.setText("");
		}else{
			if(item.update_time_last.getYear() == mDateToday.getYear() &&
					item.update_time_last.getMonth() == mDateToday.getMonth() &&
					item.update_time_last.getDay() == mDateToday.getDay())
			{
				vh.time.setText(sdftime.format(item.update_time_last));
			}else{
				vh.time.setText(sdf.format(item.update_time_last));
			}
		}
		
		
		if(item.update_unreadcount.equals("") || item.update_unreadcount.equals("0")){
			vh.sum.setVisibility(View.GONE);
		}else{
			vh.sum.setText(item.update_unreadcount);
			vh.sum.setVisibility(View.VISIBLE);
		}
		
		if(mType == TYPE_SCHOOL){
			vh.icon.setImageResource(getSchoolIconResid(item.update_time_channel));
			vh.moduleName.setText(getSchoolModuleNameResid(item.update_time_channel));
		}else{
			vh.icon.setBackgroundResource(getClassIconResid(item.update_time_channel));
			vh.moduleName.setText(getClassModuleNameResid(item.update_time_channel));
		}
		
		return convertView;
	}
	
	private int getClassIconResid(int type){
		switch (type) {
		case RequestCategoryID.EVENTS_ACTIVITY_ID:
			return R.drawable.icon_class_activitys_selector;
		case RequestCategoryID.EVENTS_ALBUM_ID:
			return R.drawable.icon_class_album_selector;
		case RequestCategoryID.EVENTS_COMMENT_ID:
			return R.drawable.icon_class_comment_selector;
		case RequestCategoryID.EVENTS_COURSEWARE_ID:
			return R.drawable.icon_class_courseware_selector;
		case RequestCategoryID.EVENTS_NEWS_ID:
			return R.drawable.icon_class_news_selector;
		case RequestCategoryID.EVENTS_SEND_ID:
			return R.drawable.icon_class_sent_selector;
		case RequestCategoryID.SYSINFO_ID:
			return R.drawable.icon_class_sysinfo_selector;
		case RequestCategoryID.EVENTS_VIDEO_ID:
			return R.drawable.icon_class_video_selector;
		case RequestCategoryID.EVENTS_COMMUNION_ID:
			return R.drawable.icon_class_face2face_selector;
		default:
			return R.drawable.app_icon;
		}
	}
	
	private int getSchoolIconResid(int type){
		switch (type) {
		case RequestCategoryID.NEWS_ACTIVITY_ID:
			return R.drawable.icon_school_events_selector;
		case RequestCategoryID.NEWS_ALBUM_ID:
			return R.drawable.icon_school_album_selector;
		case RequestCategoryID.NEWS_INFANTDIET_ID:
			return R.drawable.icon_school_infant_diet_selector;
		case RequestCategoryID.NEWS_NEWS_ID:
			return R.drawable.icon_school_news_selector;
		case RequestCategoryID.NEWS_PARENTING_ID:
			return R.drawable.icon_school_parenting_selector;
		default:
			return R.drawable.app_icon;
		}
	}
	
	private int getClassModuleNameResid(int type){
		switch (type) {
		case RequestCategoryID.EVENTS_ACTIVITY_ID:
			return R.string.classactivity;
		case RequestCategoryID.EVENTS_ALBUM_ID:
			return R.string.classalbum;
		case RequestCategoryID.EVENTS_COMMENT_ID:
			return R.string.comment;
		case RequestCategoryID.EVENTS_COURSEWARE_ID:
			return R.string.courseware;
		case RequestCategoryID.EVENTS_NEWS_ID:
			return R.string.classnews;
		case RequestCategoryID.EVENTS_SEND_ID:
			return R.string.sent;
		case RequestCategoryID.SYSINFO_ID:
			return R.string.sysinfo;
		case RequestCategoryID.EVENTS_VIDEO_ID:
			return R.string.video;
		case RequestCategoryID.EVENTS_COMMUNION_ID:
			return R.string.face2face;
		default:
			return R.string.events;
		}
	}
	
	private int getSchoolModuleNameResid(int type){
		switch (type) {
		case RequestCategoryID.NEWS_ACTIVITY_ID:
			return R.string.activity;
		case RequestCategoryID.NEWS_ALBUM_ID:
			return R.string.album;
		case RequestCategoryID.NEWS_INFANTDIET_ID:
			return R.string.infant_diet;
		case RequestCategoryID.NEWS_NEWS_ID:
			return R.string.news;
		case RequestCategoryID.NEWS_PARENTING_ID:
			return R.string.parenting;
		default:
			return R.string.news;
		}
	}
}
