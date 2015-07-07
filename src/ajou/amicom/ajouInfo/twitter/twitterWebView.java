/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.twitter;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import android.app.*;
import android.os.*;
import android.view.*;
import android.webkit.*;

public class twitterWebView extends Activity {
	
	WebView mWebView;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.twitter_webview);
		
		mWebView = (WebView) findViewById(R.id.webview); 
        mWebView.getSettings().setJavaScriptEnabled(true);  // 웹뷰에서 자바스크립트실행가능
        mWebView.loadUrl("https://mobile.twitter.com/searches?q=%23Ajou");
       
        mWebView.setWebViewClient(new HelloWebViewClient());  // WebViewClient 지정          
       
	}
	
	//@Override 
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
