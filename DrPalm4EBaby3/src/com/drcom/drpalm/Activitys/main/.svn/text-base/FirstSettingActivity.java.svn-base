package com.drcom.drpalm.Activitys.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.drcom.drpalm.GlobalVariables;
import com.drcom.drpalm.Activitys.Navigation.NavigationMainActivity;
import com.drcom.drpalm.Activitys.Navigation.SchoolNavigation;
import com.drcom.drpalm.View.controls.MyViewGroup;
import com.drcom.drpalm.View.controls.PageControlView;
import com.drcom.drpalm.View.controls.myinterface.onFlingListener;
import com.drcom.drpalmebaby.R;

public class FirstSettingActivity extends Activity{
	
	//控件
	private MyViewGroup mViewGroup;
	private PageControlView mPageControl;
	private Context mContext;
	private int[] mImageList = {
		R.drawable.page1,
		R.drawable.page2,
		R.drawable.page3,
		R.drawable.page4,
		R.drawable.page5,
		R.drawable.page6
	};

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);	//一定要放在第一行
		super.onCreate(savedInstanceState);
        setContentView(R.layout.weclome_view);
		
		super.onCreate(savedInstanceState);
		mContext = this;
//		View myview = LayoutInflater.from(this).inflate(R.layout.read_attachment_view, null);
//        myview.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT));
        mViewGroup = (MyViewGroup) findViewById(R.id.read_attm_myViewGroup);
        mViewGroup.SetonFlingListener(new onFlingListener(){
        	@Override
        	public boolean onFling(){
        		return true;
        	}
        });
        for (int i = 0; i < mImageList.length; i++) {
        	PreviewChildView viewchild = new PreviewChildView(this);
        	viewchild.setBgImage(mImageList[i]);
        	if(i == (mImageList.length-1)){
        		viewchild.setButtonAreaVisibility(View.VISIBLE);
        	}else{
        		viewchild.setButtonAreaVisibility(View.GONE);
        	}
        	mViewGroup.addView(viewchild);
		}
        mPageControl=(PageControlView) findViewById(R.id.read_attm_pageControl);
        mPageControl.setCount(mViewGroup.getChildCount());
        mPageControl.generatePageControl(0);
        mViewGroup.setScrollToScreenCallback(mPageControl);

//        mModuleLayout.addView(myview);
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mViewGroup.removeAllViews();
		mViewGroup = null;
		
		super.onDestroy();
	}


	public class PreviewChildView extends LinearLayout{
		private ImageView bgImageView = null;
		private Button shareBtn = null;
		private Button startBtn = null;
		private LinearLayout buttonArea = null;
		public PreviewChildView(Context context) {
			super(context);
			LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	        inflater.inflate(R.layout.first_preview, this); 
	        bgImageView = (ImageView)findViewById(R.id.bg_image);
	        shareBtn = (Button)findViewById(R.id.share_button);
	        startBtn = (Button)findViewById(R.id.start_button);
	        buttonArea = (LinearLayout)findViewById(R.id.attach_area);
	        shareBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String subject = FirstSettingActivity.this.getString(R.string.app_name) ;
					String title = FirstSettingActivity.this.getString(R.string.sharetitle);
					String url  = title + FirstSettingActivity.this.getString(R.string.shareurl);
					shareContent(FirstSettingActivity.this, subject, url);
				}
			});
	        startBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(GlobalVariables.getAppDefaultSchoolKey()){
						//保存为默认的SCHOOL
						SharedPreferences  preferences = getSharedPreferences("default_school", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("school_key", GlobalVariables.gSchoolKey);
						editor.commit();
						
						Intent sendIntent = new Intent();
						sendIntent.setClass(mContext,MainActivity.class);
						startActivity(sendIntent);
					}else{
						Intent sendIntent = new Intent();
						sendIntent.setClass(mContext,NavigationMainActivity.class);
//						sendIntent.putExtra(SchoolNavigation.NAVIGATION_PARENT_ID, GlobalVariables.getAgentID());	//根目录ID(代理商ID)
						startActivity(sendIntent);	
					}
					
					//释放资源
					mViewGroup.removeAllViews();
					FirstSettingActivity.this.finish();	
				}
			});
		}
		
		public void setBgImage(int res_id){
			bgImageView.setBackgroundResource(res_id);
		}
		
		public void setButtonAreaVisibility(int visibility){
			buttonArea.setVisibility(visibility);
		}
		
		public void shareContent(Context context, String subject, String url) {
			
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, subject);
			intent.putExtra(Intent.EXTRA_TEXT, url);
			context.startActivity(Intent.createChooser(intent, "Share"));
			
		}
	}
}
