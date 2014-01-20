package com.drcom.ui.View.controls.TravelTab;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.drcom.ui.R;

/**
 * 左右滑动切换的Tabhost
 * tab栏有滑动效果
 * @author zhaojunjie
 *
 */
public class TravelTabHost extends RelativeLayout {
	private TravelTab tb;
	private Context mContext;
	
	/** 导航页面 */
	private ViewPager mViewPager;
	/** 页面适配器 */
	private SelectionPageAdapter mSelectionPageAdapter;
	/** 页面集合 */
	private ArrayList<View> mPageViews = new ArrayList<View>();
	
	public TravelTabHost(Context context) {
		super(context);
		inintView(context);
	}
	
	public TravelTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		inintView(context);
	}

	private void inintView(Context context) {
		this.mContext = context;
		
		LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.setLayoutParams(p);
		
		LinearLayout.LayoutParams ptb = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		ptb.gravity = Gravity.TOP;
		tb = new TravelTab(context);
		tb.setId(11);
		tb.setLayoutParams(ptb);
		
		RelativeLayout.LayoutParams ptvp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ptvp.addRule(RelativeLayout.BELOW, tb.getId());
		tb.setTravelHandler(mTravelTabHandler);
		
		mViewPager = new ViewPager(context);
		mViewPager.setLayoutParams(ptvp);
		mSelectionPageAdapter = new SelectionPageAdapter();
		mViewPager.setAdapter(mSelectionPageAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				tb.moveToAndFollowWith(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mViewPager.setCurrentItem(0);
		
		this.addView(mViewPager);
		this.addView(tb);
	}
	
	/**
	 * tab item切换事件
	 */
	private Handler mTravelTabHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
			if(msg.what < mViewPager.getChildCount())
				mViewPager.setCurrentItem(msg.what);
		}
	};
	
	/**
	 * 初始化
	 * @param backgroundID
	 * @param coverID
	 * @param arrowleftID
	 * @param arrowrightID
	 * @param traveltabItemList
	 * @param viewList
	 */
	public void initView(int backgroundID, int coverID, int arrowleftID , int arrowrightID,
			ArrayList<TraveltabItem> traveltabItemList,
			ArrayList<View> viewList){
		
		mPageViews = viewList;
		mSelectionPageAdapter.notifyDataSetChanged();
		
		tb.initView(R.drawable.bg, R.drawable.cover,R.drawable.arrow_left,R.drawable.arrow_right,traveltabItemList);
	}
	
	/**
	 * selection page adapter 
	 * @author chen
	 * @date 2012-10-25 下午6:04:16
	 */
	class SelectionPageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mPageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mPageViews.get(arg1));
		}

		/**
		 * 获取每一个item对象
		 */
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mPageViews.get(arg1));
			return mPageViews.get(arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
		
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override
		public void startUpdate(View arg0) {}

		@Override
		public void finishUpdate(View arg0) {}
	}
}
