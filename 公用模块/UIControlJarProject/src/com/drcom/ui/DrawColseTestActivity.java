package com.drcom.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.drcom.ui.R;
import com.drcom.ui.View.controls.DrawCloseActivity.BaseDrawColseActivity;

public class DrawColseTestActivity extends BaseDrawColseActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.SetView(R.layout.view2);
        
        WebView wv = (WebView)findViewById(R.id.webview1);
        wv.loadUrl("http://www.hao123.com");
		wv.setWebViewClient(new WebViewClient() {
			//重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
//				super.onReceivedError(view, errorCode, description, failingUrl);
				view.clearView();
			}
		});
	}
}
