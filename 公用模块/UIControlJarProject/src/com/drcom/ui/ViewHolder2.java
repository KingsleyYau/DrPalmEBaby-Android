package com.drcom.ui;
//Download by http://www.codefans.net
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class ViewHolder2 extends LinearLayout {

	public ViewHolder2(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(Color.YELLOW);
		
		LayoutParams p =new LayoutParams(200, 200);
		
		WebView wv = new WebView(context);
		wv.setLayoutParams(p);
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
		
		this.addView(wv);
	}


}
