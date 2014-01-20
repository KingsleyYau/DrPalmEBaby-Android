package com.drcom.ui.View.tool;

import android.content.Context;
import android.graphics.Point;

public class MyMothod {
	/**
	 * dp转px
	 * @param context
	 * @param dpvalue
	 * @return
	 */
	public static int Dp2Px(Context context , int dpvalue){
		int px =(int)( dpvalue * (double)context.getResources().getDisplayMetrics().densityDpi/160);
		return px;
	}
	
	/**
	 * px转dp
	 * @param context
	 * @param pxvalue
	 * @return
	 */
	public static int Px2Dp(Context context , int pxvalue){
		int dp =(int)( pxvalue / ((double)context.getResources().getDisplayMetrics().densityDpi/160));
		return dp;
	}
	
	/**
	 * get the pivot point(中心点)
	 * @param left
	 * @param right
	 * @param width
	 * @param height
	 * @return
	 */
	public static Point getPivot(int left, int top, int width, int height){
		Point point = new Point();
		point.x = left + width/2;
		point.y = top + height/3;
		return point;
	}
}
