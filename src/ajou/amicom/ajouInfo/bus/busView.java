/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.bus;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.webkit.*;

public class busView extends Activity {

	
	WebView mWebView;
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bus_view);
		
		Bundle extras = getIntent().getExtras();
		final String busdata = (String) extras.get("busdata");
		
		mWebView = (WebView) findViewById(R.id.webview); 
        mWebView.getSettings().setJavaScriptEnabled(true);  // 웹뷰에서 자바스크립트실행가능
        if (busdata.equals("03117")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25ED%259A%25A8%25EC%2584%25B1%25EC%25B4%2588%25EB%2593%25B1%25ED%2595%2599%25EA%25B5%2590&mobileNo=03117&stationId=202000038");
        } else if (busdata.equals("03119")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25ED%259A%25A8%25EC%2584%25B1%25EC%25B4%2588%25EB%2593%25B1%25ED%2595%2599%25EA%25B5%2590&mobileNo=03119&stationId=202000032");
        } else if (busdata.equals("03124")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%25B0%25BD%25ED%2598%2584%25EA%25B3%25A0%25EA%25B5%2590.%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25ED%2595%2599%25EA%25B5%2590.%25EC%259C%25A0%25EC%258B%25A0%25EA%25B3%25A0%25EA%25B5%2590&mobileNo=03124&stationId=202000039");
        } else if (busdata.equals("03125")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%25B0%25BD%25ED%2598%2584%25EA%25B3%25A0%25EA%25B5%2590.%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25ED%2595%2599%25EA%25B5%2590.%25EC%259C%25A0%25EC%258B%25A0%25EA%25B3%25A0%25EA%25B5%2590&mobileNo=03125&stationId=202000061");
        } else if (busdata.equals("04039")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EB%25B3%2591%25EC%259B%2590&mobileNo=04039&stationId=203000171");
        } else if (busdata.equals("04041")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EB%25B3%2591%25EC%259B%2590&mobileNo=04041&stationId=203000170");
        } else if (busdata.equals("04237")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25ED%2595%2599%25EA%25B5%2590.%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EB%25B3%2591%25EC%259B%2590%25EC%259E%2585%25EA%25B5%25AC.%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EC%2582%25BC%25EA%25B1%25B0%25EB%25A6%25AC&mobileNo=04237&stationId=203000066");
        } else if (busdata.equals("03126")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25ED%2595%2599%25EA%25B5%2590.%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EB%25B3%2591%25EC%259B%2590%25EC%259E%2585%25EA%25B5%25AC.%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EC%2582%25BC%25EA%25B1%25B0%25EB%25A6%25AC&mobileNo=03126&stationId=202000005");
        } else if (busdata.equals("04238")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25ED%2595%2599%25EA%25B5%2590%25EC%259E%2585%25EA%25B5%25AC&mobileNo=04238&stationId=203000067");
        } else if (busdata.equals("03129")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25ED%2595%2599%25EA%25B5%2590%25EC%259E%2585%25EA%25B5%25AC&mobileNo=03129&stationId=202000004");
        } else if (busdata.equals("03132")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EC%259E%2585%25EA%25B5%25AC&mobileNo=03132&stationId=202000073");
        } else if (busdata.equals("04023")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EC%2595%2584%25EC%25A3%25BC%25EB%258C%2580%25EC%259E%2585%25EA%25B5%25AC&mobileNo=04023&stationId=203000179");
        } else if (busdata.equals("04208")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EA%25B5%25AC%25EB%25A7%25A4%25ED%2583%2584%25EC%258B%259C%25EC%259E%25A5%25EC%259E%2585%25EA%25B5%25AC&mobileNo=04208&stationId=203000258");
        } else if (busdata.equals("04042")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EB%25B2%2595%25EC%259B%2590%25EC%2582%25AC%25EA%25B1%25B0%25EB%25A6%25AC&mobileNo=04042&stationId=203000068");
        } else if (busdata.equals("04179")) {
        	mWebView.loadUrl("http://m.gbis.go.kr/jsp/busArrival.jsp?routeId=&stationName=%25EB%25B2%2595%25EC%259B%2590%25EC%2582%25AC%25EA%25B1%25B0%25EB%25A6%25AC&mobileNo=04179&stationId=203000178");
        }
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
