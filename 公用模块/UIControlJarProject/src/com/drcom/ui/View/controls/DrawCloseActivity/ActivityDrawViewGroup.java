package com.drcom.ui.View.controls.DrawCloseActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * ����������,��Ӧ�����¼�
 * @author zhaojunjie
 *
 */
public class ActivityDrawViewGroup extends ViewGroup {

	private static final String TAG = "scroller";

	private int DRAWCLOSELEFTX = 30;	//��Ӧ�����ر��¼�����ʱ��X����
	
	private Scroller scroller;

	private int currentScreenIndex;

	private GestureDetector gestureDetector;
	
	private onFlingListener mFlingListener;

	private ScrollToScreenCallback scrollToScreenCallback;
	
	private boolean scrollable = true;
	private boolean flingable = true;

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	public void setScrollToScreenCallback(
			ScrollToScreenCallback scrollToScreenCallback) {
		this.scrollToScreenCallback = scrollToScreenCallback;
	}

	// ����һ����־λ����ֹ�ײ��onTouch�¼��ظ�����UP�¼�
	private boolean fling;

	public ActivityDrawViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public ActivityDrawViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public ActivityDrawViewGroup(Context context) {
		super(context);
		initView(context);
	}

	private void initView(final Context context) {
		this.scroller = new Scroller(context);

		this.gestureDetector = new GestureDetector(new OnGestureListener() {

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				
				//delta > 0 ��ǰVIEW����ƫ��
				//delta < 0 ��ǰVIEW����ƫ��
				int delta = currentScreenIndex * getWidth() - getScrollX();
				if(e1 == null || e2 == null)
					return true;
				
				double yatan = Math.atan(((e2.getY()-e1.getY())/(e2.getX()-e1.getX())));
				
				if(scrollable){
					if(Math.abs(yatan) > 0.5){
						return true;
					}
					
					if(e1.getX(0) > DRAWCLOSELEFTX){
						return true;
					}
					
//					if(Math.abs(distanceY) > 50){
//						Log.d(TAG, "Y̫��");
//						return true;
//					}
					
//					if( e2.getY() - e1.getY() > e2.getX() - e1.getX()){
//						return true;
//					}
					
//					if(!flingable)
//						return true;
					
					if(delta < 0 && currentScreenIndex == getChildCount() - 1 && distanceX > 0){//��ֹ�ƶ������һҳ
						return true;
					}else if(delta > 0 && currentScreenIndex == 0 && distanceX < 0){	//��ֹ���һҳ֮ǰ�ƶ�
						return true;
					}
					scrollBy((int) distanceX, 0);
					
//					if ((distanceX > 0 && currentScreenIndex < getChildCount() - 1)){	// ��ֹ�ƶ������һҳ
//							|| (distanceX < 0 && getScrollX() > 0)) {				// ��ֹ���һҳ֮ǰ�ƶ�
//						scrollBy((int) distanceX, 0);
//					}
				}
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				Log.d(TAG, "min velocity >>>"+ ViewConfiguration.get(context).getScaledMinimumFlingVelocity()+ " current velocity>>" + velocityX);
				if(scrollable){
//					if (Math.abs(velocityX) > ViewConfiguration.get(context)
//							.getScaledMinimumFlingVelocity()) {// �ж��Ƿ�ﵽ��С�����ٶȣ�ȡ����ֵ��
//						if (velocityX > 0 && currentScreenIndex > 0) {
//							Log.d(TAG, ">>>>fling to left");
//							fling = true;
//							scrollToScreen(currentScreenIndex - 1);
//						} else if (velocityX < 0
//								&& currentScreenIndex < getChildCount() - 1) {
//							Log.d(TAG, ">>>>fling to right");
//							fling = true;
//							scrollToScreen(currentScreenIndex + 1);
//						}
//					}
					
				}
//				if(mFlingListener!=null)
//					mFlingListener.onFling();
				
				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				return false;
			}
		});
	}
	

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) { 
		Log.d(TAG, ">>left: " + left + " top: " + top + " right: " + right
				+ " bottom:" + bottom);

		/**
		 * ���ò��֣�������ͼ˳���������
		 */
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			child.setVisibility(View.VISIBLE);
//			child.measure(right - left, bottom - top);
			child.layout(i * getWidth(), 0, getWidth() + i * getWidth(),
					getHeight());

		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
	}

	@Override
	public void computeScroll() {
		if (scroller.computeScrollOffset()) {
			scrollTo(scroller.getCurrX(), 0);
			postInvalidate();
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		return super.onInterceptTouchEvent(ev);
		
		//�жϻ������������򴫸��ֿؼ��������򲻴�
//		double yatan = Math.atan(((ev.getY(1)-ev.getY(ev.getPointerCount()-1))/(ev.getX(1)-ev.getX(ev.getPointerCount()-1))));
//		if(Math.abs(yatan) > 0.5){
//			return false;
//		}else{
//			return true;
//		}
		
		//�жϻ����������,������ߵ�����������ݸ��ӿؼ�
//		Log.i("xxx", "ev.getAction():" + ev.getAction() + ",ev.getX(0):" + ev.getX(0));
		if(ev.getX(0) > DRAWCLOSELEFTX){
			return super.onInterceptTouchEvent(ev);	//���ݵ��ӿؼ�
		}else{
			return true;
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (!fling) {
				snapToDestination();
			}
			fling = false;
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * �л���ָ����
	 * �ж���
	 * @param whichScreen
	 */
	public void scrollToScreen(int whichScreen) {
		if (getFocusedChild() != null && whichScreen != currentScreenIndex
				&& getFocusedChild() == getChildAt(currentScreenIndex)) {
			getFocusedChild().clearFocus();
		}

		final int delta = whichScreen * getWidth() - getScrollX();
		scroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta)*2);	//���һ���������ٶ�,ԽСԽ��
		invalidate();

		currentScreenIndex = whichScreen;
		if (scrollToScreenCallback != null) {
			scrollToScreenCallback
					.callback(currentScreenIndex);
		}
		
		if(mFlingListener!=null){
			this.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mFlingListener.onFling();
				}
			}, 300);
			
		}
			
	}
	
	/**
	 * �л���ָ����
	 * �޶���
	 * @param whichScreen
	 * @param speed �ٶ�,0:û����Ч��,��ֵԽ��Խ��
	 */
	public void scrollToScreen(int whichScreen,int speed) {
		if (getFocusedChild() != null && whichScreen != currentScreenIndex
				&& getFocusedChild() == getChildAt(currentScreenIndex)) {
			getFocusedChild().clearFocus();
		}

		final int delta = whichScreen * getWidth() - getScrollX();
		scroller.startScroll(getScrollX(), 0, delta, 0,Math.abs(delta) * speed);
		invalidate();
		
		currentScreenIndex = whichScreen;
		if (scrollToScreenCallback != null) {
			scrollToScreenCallback
					.callback(currentScreenIndex);
		}
		
		if(mFlingListener!=null)
			mFlingListener.onFling();
		
	}

	/**
	 * 
	 */
	public void scrollToNextPage(){
		if(currentScreenIndex < getChildCount() - 1)
			scrollToScreen(currentScreenIndex + 1);
	}
	
	/**
	 * 
	 */
	public void scrollToForward(){
		if(currentScreenIndex > 0)
			scrollToScreen(currentScreenIndex - 1);
	}
	
	/**
	 * ���ݵ�ǰx����λ��ȷ���л����ڼ���
	 */
	private void snapToDestination() {
		scrollToScreen((getScrollX() + (getWidth() / 4)) / getWidth());
	}

	interface ScrollToScreenCallback {
		public void callback(int currentIndex);
	}
	
	/**
	 * �л�tab���¼�
	 * @param l
	 */
	public void SetonFlingListener(onFlingListener l){
		mFlingListener = l;
	}
	
	/**
	 * ȡ��ǰҳ��
	 * @return
	 */
	public int getCurrentScreenIndex() {
		return currentScreenIndex;
	}
}
