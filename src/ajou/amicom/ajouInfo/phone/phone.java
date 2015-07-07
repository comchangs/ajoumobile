/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.phone;

import java.net.*;

import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;


public class phone extends TabActivity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone);
        
        Bundle extras = getIntent().getExtras();
        String phone_page = new String(URLEncoder.encode((String) extras.get("phone_page")));
        //setContentView(R.layout.phone);  	

        TabHost tabhost = getTabHost();

        tabhost.addTab(tabhost.newTabSpec("phone_tab1").setIndicator("대학/대학원",getResources().getDrawable(R.drawable.telephone_icon)).setContent(new Intent(this, phone_tab1.class)));
        tabhost.addTab(tabhost.newTabSpec("phone_tab2").setIndicator("연구/행정기관",getResources().getDrawable(R.drawable.telephone_icon)).setContent(new Intent(this, phone_tab2.class)));
        tabhost.addTab(tabhost.newTabSpec("phone_tab3").setIndicator("지원기관/기타",getResources().getDrawable(R.drawable.telephone_icon)).setContent(new Intent(this, phone_tab3.class)));
        tabhost.setCurrentTab(Integer.parseInt(phone_page)-1);
	}
}
