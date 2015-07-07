/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.phone.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

	public class cafeteria extends TabActivity {

		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			TabHost tabHost = getTabHost();
			
			tabHost.addTab(tabHost.newTabSpec("cafe1").setIndicator("아향")
					.setContent(new Intent(this, cafe1.class)));
			tabHost.addTab(tabHost.newTabSpec("cafe2").setIndicator("학생회관")
					.setContent(new Intent(this, cafe2.class)));
			tabHost.addTab(tabHost.newTabSpec("cafe3").setIndicator("기숙사식당")
					.setContent(new Intent(this, cafe3.class)));
			tabHost.addTab(tabHost.newTabSpec("cafe4").setIndicator("교직원식당")
					.setContent(new Intent(this, cafe4.class)));
		}
	}
