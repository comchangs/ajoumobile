/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class SchoolSchedule extends Activity{
	WebView wv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.portal_schoolschedule);
		
		wv = (WebView) findViewById(R.id.schedule_wv01);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl(StringURL.schedule01);
	}
	
}
