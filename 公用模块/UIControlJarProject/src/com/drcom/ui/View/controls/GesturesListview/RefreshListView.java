package com.drcom.ui.View.controls.GesturesListview;

import java.util.Date;

import com.drcom.ui.View.tool.MyMothod;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 下拉刷新Listview
 * 
 * @author mcx
 * 
 */
public class RefreshListView extends ListView implements OnScrollListener {

	private static final String TAG = "listview";

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	private final static int CLICK_REFRESH = 5;
	
	private String Color_alpha = "#00000000";	//透明色

	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 3;

	private LayoutInflater inflater;

	private LinearLayout headView;

	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;

	private int startY;
	private int firstItemIndex;
	private int visibleCount;

	private int state;

	private boolean isBack;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;

	private boolean isClickRefresh = false;

	private boolean isClearRefresh = false;

	private Context ctx;

	 private GestureDetector mGestureDetector;

	public RefreshListView(Context context) {
		super(context);
		init(context);
		this.ctx = context;
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		this.ctx = context;
	}

	private int mImgArrowResid;
	private int mStrReleaseResid;
	private int mStrPullResid;
	private int mStrRefreshResid;
	private int mStrTapResid;
	private int mStrLatestResid;
	/**
	 * 设置图片&文字资源ID
	 * @param imgArrowResid		箭头
	 * @param strReleaseResid	"松开可以刷新"
	 * @param strPullResid		"下拉可以刷新"
	 * @param strRefreshResid	"加载中..."
	 * @param strTapResid		"点击刷新"
	 * @param strLatestResid	"最后更新时间："
	 */
	public void setResId(int imgArrowResid,int strReleaseResid,int strPullResid,int strRefreshResid,
			int strTapResid,int strLatestResid){
		mImgArrowResid = imgArrowResid;
		mStrReleaseResid = strReleaseResid;
		mStrPullResid = strPullResid;
		mStrRefreshResid = strRefreshResid;
		mStrTapResid = strTapResid;
		mStrLatestResid = strLatestResid;
		
		arrowImageView.setBackgroundResource(imgArrowResid);
		tipsTextview.setText(strPullResid);
		lastUpdatedTextView.setText(strLatestResid);
	}
	
	private LinearLayout getHeadview(Context context){
		LinearLayout headView = new LinearLayout(context);
		
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		RelativeLayout Rlayout = new RelativeLayout(context);
		Rlayout.setLayoutParams(lp1);
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL);
		lp2.setMargins(30, 0, 0, 0);
		FrameLayout Flayout = new FrameLayout(context);
		Flayout.setLayoutParams(lp2);
		
		LinearLayout.LayoutParams lp6 = new LinearLayout.LayoutParams(MyMothod.Dp2Px(context, 20),MyMothod.Dp2Px(context, 40));
		arrowImageView = new ImageView(context);
		arrowImageView.setLayoutParams(lp6);
		
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleSmall);
		progressBar.setLayoutParams(lp3);
		
		Flayout.addView(arrowImageView);
		Flayout.addView(progressBar);
		
		RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp4.addRule(RelativeLayout.CENTER_HORIZONTAL);
		LinearLayout Llayout = new LinearLayout(context);
		Llayout.setLayoutParams(lp4);
		Llayout.setOrientation(LinearLayout.VERTICAL);
		Llayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		LinearLayout.LayoutParams lp5 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tipsTextview = new TextView(context);
		tipsTextview.setLayoutParams(lp5);
		lastUpdatedTextView = new TextView(context);
		lastUpdatedTextView.setLayoutParams(lp5);
		
		Llayout.addView(tipsTextview);
		Llayout.addView(lastUpdatedTextView);
		
		Rlayout.addView(Llayout);
		Rlayout.addView(Flayout);
		headView.addView(Rlayout);
		return headView;
	}
	
	private void init(Context context) {
		setCacheColorHint(Color.parseColor(Color_alpha));
//		inflater = LayoutInflater.from(context);

//		headView = (LinearLayout) inflater.inflate(R.layout.head, null);
		

		headView = getHeadview(context);
//		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
//		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
//		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
//		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		Log.v("size", "width:" + headContentWidth + " height:" + headContentHeight);

		addHeaderView(headView, null, false);
		headView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				state = REFRESHING;
				changeHeaderViewByState();
				onRefresh();
			}
		});
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;

		 mGestureDetector = new GestureDetector(new MyGestureListener());//
		// 创建手势检测对象
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
		Log.i("xpf", "firstVisiableItem " + firstVisiableItem + " arg2= " + arg2 + " arg3= " + arg3);

		visibleCount = arg2;
		firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	 @Override
	 public boolean dispatchTouchEvent(MotionEvent ev) {
		 mGestureDetector.onTouchEvent(ev);
		 onTouchEvent(ev);
		 return super.dispatchTouchEvent(ev);
	 }

	private float x1;
	private boolean touchResult = true;
	public boolean onTouchEvent(MotionEvent event) {
		if (!getClearRefresh()) {
			if (!isClickRefresh) {
				if (isRefreshable) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x1 = event.getX();
						
						if (firstItemIndex == 0 && !isRecored) {
							isRecored = true;
							startY = (int) event.getY();
							Log.v(TAG, "在down时候记录当前位置‘");
						}
//						touchResult = true;
						break;

					case MotionEvent.ACTION_UP:

						Log.i("zjj", "---******----ACTION_UP----******-------");
						
						if (state != REFRESHING && state != LOADING) {
							if (state == DONE) {
								// 什么都不做
							}
							if (state == PULL_To_REFRESH) {
								state = DONE;
								changeHeaderViewByState();

								Log.v(TAG, "由下拉刷新状态，到done状态");
							}
							if (state == RELEASE_To_REFRESH) {
								state = REFRESHING;
								changeHeaderViewByState();
								onRefresh();

								Log.v(TAG, "由松开刷新状态，到done状态");
							}
						}

						isRecored = false;
						isBack = false;

//						touchResult = true;
						break;

					case MotionEvent.ACTION_MOVE:
						int tempY = (int) event.getY();

//						Log.i("zjj", "-----------ACTION_MOVE------------------" + Math.abs(event.getX() - x1));
//						if(Math.abs(event.getX() - x1)> 50){
////							return false;
//							touchResult = false;
//							break;
//						}
//						Log.i("zjj", "--------");
						
						if (!isRecored && firstItemIndex == 0) {
							Log.v(TAG, "在move时候记录下位置");
							isRecored = true;
							startY = tempY;
						}

						if (state != REFRESHING && isRecored && state != LOADING) {

							// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

							// 可以松手去刷新了
							if (state == RELEASE_To_REFRESH) {

								setSelection(0);

								// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
								if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
									state = PULL_To_REFRESH;
									changeHeaderViewByState();

									Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
								}
								// 一下子推到顶了
								else if (tempY - startY <= 0) {
									state = DONE;
									changeHeaderViewByState();

									Log.v(TAG, "由松开刷新状态转变到done状态");
								}
								// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
								else {
									// 不用进行特别的操作，只用更新paddingTop的值就行了
								}
							}
							// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
							if (state == PULL_To_REFRESH) {

								setSelection(0);

								// 下拉到可以进入RELEASE_TO_REFRESH的状态
								if ((tempY - startY) / RATIO >= headContentHeight) {
									state = RELEASE_To_REFRESH;
									isBack = true;
									changeHeaderViewByState();

									Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
								}
								// 上推到顶了
								else if (tempY - startY <= 0) {
									state = DONE;
									changeHeaderViewByState();

									Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
								}
							}

							// done状态下
							if (state == DONE) {
								if (tempY - startY > 0) {
									state = PULL_To_REFRESH;
									changeHeaderViewByState();
								}
							}

							// 更新headView的size
							if (state == PULL_To_REFRESH) {
								headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);

							}

							// 更新headView的paddingTop
							if (state == RELEASE_To_REFRESH) {
								headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
							}

						}

						break;
					}
				}
			}
		}

//		return super.onTouchEvent(event);
//		return false;
//		Log.i("zjj", "touchResult:" + touchResult);
		return touchResult ;
		
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setTextSize(15);
//			tipsTextview.setText(R.string.pull_to_refresh_release_label);
			tipsTextview.setText(mStrReleaseResid);

			Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			tipsTextview.setTextSize(15);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);

//				tipsTextview.setText(R.string.pull_to_refresh_pull_label);
				tipsTextview.setText(mStrPullResid);
			} else {
//				tipsTextview.setText(R.string.pull_to_refresh_pull_label);
				tipsTextview.setText(mStrPullResid);
			}
			Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setTextSize(15);
//			tipsTextview.setText(R.string.pull_to_refresh_refreshing_label);
			tipsTextview.setText(mStrRefreshResid);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
//			arrowImageView.setImageResource(R.drawable.arrow);
			arrowImageView.setImageResource(mImgArrowResid);
			tipsTextview.setTextSize(15);
//			tipsTextview.setText(R.string.pull_to_refresh_pull_label);
			tipsTextview.setText(mStrPullResid);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "当前状态，done");
			break;
		case CLICK_REFRESH:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setTextSize(25);
//			tipsTextview.setText(R.string.pull_to_refresh_tap_label);
			tipsTextview.setText(mStrTapResid);
			lastUpdatedTextView.setVisibility(View.GONE);
			break;
		}
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		if (isClickRefresh) {
			if (getAdapter().getCount() > 1 + getFooterViewsCount()) {
				hideHeadView();
			} else {
				setHeadViewVisible();
			}
		} else {
			state = DONE;
//			lastUpdatedTextView.setText(ctx.getResources().getString(R.string.pull_to_refresh_latest_label) + new Date().toLocaleString());
			lastUpdatedTextView.setText(ctx.getResources().getString(mStrLatestResid) + new Date().toLocaleString());
			changeHeaderViewByState();
		}
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	// 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
//		lastUpdatedTextView.setText(ctx.getResources().getString(R.string.pull_to_refresh_latest_label) + new Date().toLocaleString());
		lastUpdatedTextView.setText(ctx.getResources().getString(mStrLatestResid) + new Date().toLocaleString());
		// if(!getClearRefresh()){
		// if(adapter.getCount()>0){
		// hideHeadView();
		// }else{
		// setHeadViewVisible();
		// }
		// }
		super.setAdapter(adapter);
	}
	
	/**
	 * 选项总数为空时,调用此方法,显示"点击刷新"
	 */
	public void setHeadViewVisible() {
		isClickRefresh = true;
		state = CLICK_REFRESH;
		headView.setClickable(true);
		changeHeaderViewByState();
	}

	public void setOnloadingRefreshVisible() {
		state = REFRESHING;
		changeHeaderViewByState();

		Log.v(TAG, "当前状态,正在刷新...");
	}

	public void hideHeadView() {
		isClickRefresh = false;
		state = DONE;
		headView.setClickable(false);
		changeHeaderViewByState();
	}

	/**
	 * "点击刷新"列表数据后,选项总数为空时,调用此方法,清除"正在刷新"的状态"显示"点击刷新"
	 * @param flags true 清除下拉刷新效果（注：此方法调用应先于setAdapter,否则可能出现head）
	 */
	public void clearPullToRefresh(boolean flags) {
		isClearRefresh = flags;
	}

	private boolean getClearRefresh() {
		return isClearRefresh;
	}

	public void setFirstVisableItem(int firstVisableItem, int visibleItemCount) {
		firstItemIndex = firstVisableItem;
		visibleCount = visibleItemCount;
	}
	
	private class MyGestureListener implements OnGestureListener {

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
//			if (e1.getX() - e2.getX() > 150 && Math.abs(velocityX) > 150) {
//				Log.i("zjj", "Listview向左手势");
//			}
//			Log.i("zjj", "Listview手势");
			if (Math.abs(velocityX) > 50) {
				Log.i("zjj", "Listview手势左右");
				touchResult = false;
			}
			
			if (Math.abs(velocityY) > 50) {
				Log.i("zjj", "Listview手势上下");
				touchResult = true;
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

	}
}
