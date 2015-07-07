/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.amicom;

import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.webkit.*;

public class amicom_notice_view extends Activity {

	WebView mWebView;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.amicom_notice_view);

		Bundle extras = getIntent().getExtras();
		String url = (String) extras.get("url");
		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true); // 웹뷰에서 자바스크립트실행가능
		mWebView.loadUrl(url);

		mWebView.setWebViewClient(new HelloWebViewClient()); // WebViewClient 지정

	}

	// @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
