package com.drcom.drpalm.View.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.drcom.drpalmebaby.R;

public class TypegifView extends View implements Runnable {
	gifOpenHelper gHelper;
	private boolean isStop = false;
	int delta;
	String title;

	Bitmap bmp;

	Thread updateTimer = null;
	private Bitmap image;

	// construct - refer for java
	public TypegifView(Context context) {
		this(context, null);
	}

	// construct - refer for xml
	public TypegifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		image = Bitmap.createBitmap(54, 54, Config.RGB_565);
		// 添加属性
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.gifView);
		int n = ta.getIndexCount();

		for (int i = 0; i < n; i++) {
			int attr = ta.getIndex(i);

			switch (attr) {
			case R.styleable.gifView_src:
				int id = ta.getResourceId(R.styleable.gifView_src, 0);
				setSrc(id);
				break;

			case R.styleable.gifView_delay:
				int idelta = ta.getInteger(R.styleable.gifView_delay, 1);
				setDelta(idelta);
				break;

			case R.styleable.gifView_stop:
				boolean sp = ta.getBoolean(R.styleable.gifView_stop, false);
				if (!sp) {
					setStop();
				}
				break;
			}

		}

		ta.recycle();

	}

	/**
	 * 设置停止
	 * 
	 * @param stop
	 */
	public synchronized void setStop() {
		isStop = false;
	}

	/**
	 * 设置启动
	 */
	public synchronized void setStart() {
		if (!isStop) {
			isStop = true;

			if (updateTimer == null) {
				updateTimer = new Thread(this);
				updateTimer.start();
			} else {
				this.notify();

			}
		}
	}

	/**
	 * 设置GIF 资源ID
	 * 
	 * @param id
	 */
	public void setSrc(int id) {

		gHelper = new gifOpenHelper(image);
		gHelper.read(TypegifView.this.getResources().openRawResource(id));
		bmp = gHelper.getImage();// 得到第一张图片
	}

	/**
	 * 设置动画播放速度(1:1秒1帧)
	 * @param is
	 */
	public void setDelta(int is) {
		delta = is;
	}

	// to meaure its Width & Height
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		return gHelper.getWidth();
	}

	private int measureHeight(int measureSpec) {
		return gHelper.getHeigh();
	}

	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, 0, 0, null);
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("run()");
		while (true) {
			if (isStop) {
				try {
					this.postInvalidate();
					// Thread.sleep(gHelper.getDelay(1));
					Thread.sleep(gHelper.nextDelay() / delta); // 原来代码
					bmp = gHelper.nextBitmap();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			} else {
				synchronized (this) {
					try {
						this.wait(); // 暂停线程
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}