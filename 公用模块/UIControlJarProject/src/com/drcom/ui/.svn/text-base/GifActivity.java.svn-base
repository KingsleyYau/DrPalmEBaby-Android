package com.drcom.ui;

import com.drcom.ui.R;
import com.drcom.ui.View.controls.Gif.TypegifView;

import android.app.Activity;
import android.os.Bundle;

public class GifActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewgif);
		
		TypegifView gifview = (TypegifView)findViewById(R.id.title_gifViewloading);
		gifview.setSrc(R.raw.loading);	//设置图片资源
		gifview.setDelta(1);			//动画速度
		gifview.setStart();				//开始播放
//		gifview.setStop();				//停止播放
	}
}
