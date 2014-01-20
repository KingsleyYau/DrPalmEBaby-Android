package com.drcom.drpalm.Activitys.events.review;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.drcom.drpalm.GlobalVariables;
import com.drcom.drpalm.Activitys.ModuleActivity;
import com.drcom.drpalm.Activitys.events.NewEventActivity;
import com.drcom.drpalm.Tool.request.RequestOperationReloginCallback;
import com.drcom.drpalm.View.controls.MyMothod;
import com.drcom.drpalm.View.events.review.ReviewActivityManagement;
import com.drcom.drpalm.objs.EventDetailsItem;
import com.drcom.drpalm.objs.ReviewResult;
import com.drcom.drpalmebaby.R;

/**
 * 回评
 * @author zhaojunjie
 *
 */
public class ReviewActivity  extends ModuleActivity{
	public static String REPLY_EVENT_ID = "ReplyEventId";
	
	//控件
	private LinearLayout mLayout;
	private Button buttonSave;
	
	//变量
	private int eventid = 0;
	private ReviewActivityManagement mReviewActivityManagement;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.event_review_view, mLayout_body);
		
		mLayout = (LinearLayout)findViewById(R.id.LinearLayoutMain);
		
		Bundle extras = getIntent().getExtras();
		if(extras.containsKey(REPLY_EVENT_ID))
		{
			eventid = extras.getInt(REPLY_EVENT_ID, 0);
		}
		
		mReviewActivityManagement = new ReviewActivityManagement(ReviewActivity.this,eventid);
		mReviewActivityManagement.InitUI(mLayout);
		
		initTitlebar();
		hideToolbar();
	}
	
	/**
	 * Titlebar
	 */
	private void initTitlebar() {

		LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalVariables.btnWidth_Titlebar), MyMothod.Dp2Px(this, GlobalVariables.btnHeight_Titlebar));
		p.setMargins(0, 0, 5, 0);
		
		// 发送
		buttonSave = new Button(this);
		buttonSave.setBackgroundResource(R.drawable.btn_title_blue_selector);
		buttonSave.setText(getString(R.string.consultation_commit));
		buttonSave.setTextAppearance(ReviewActivity.this, R.style.TitleBtnText);
		buttonSave.setLayoutParams(p);
		buttonSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mReviewActivityManagement.Summit(callback);
			}
		});

		setTitleRightButton(buttonSave);
	}
	
	/**
	 * 提交结果
	 */
	private RequestOperationReloginCallback callback = new RequestOperationReloginCallback(){
		
		@Override
		public void onError(String str) {
			Log.i("zjj", "提交回评 返回失败");
			HideLoadingDialog();
		}
		@Override
		public void onSuccess() {		
			Log.i("zjj", "提交回评  返回成功");
			HideLoadingDialog();
			finishDraw();
		}								
	};
}
