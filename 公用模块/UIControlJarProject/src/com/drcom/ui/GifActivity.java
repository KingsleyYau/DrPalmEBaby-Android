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
		gifview.setSrc(R.raw.loading);	//����ͼƬ��Դ
		gifview.setDelta(1);			//�����ٶ�
		gifview.setStart();				//��ʼ����
//		gifview.setStop();				//ֹͣ����
	}
}
