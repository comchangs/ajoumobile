/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;

public class ReadingRoomCondition_view extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.library_readingroomcondition_view);
		
		Bundle extras = getIntent().getExtras();
		final String url = (String) extras.get("url");
		
		final ImageView iv = (ImageView)findViewById(R.id.readingroomconditionmenu_iv);
		
		ReadingRoomConditionControl control = null;
		
		try {
			control = new ReadingRoomConditionControl();
			iv.setImageBitmap(control.getURLImage(url));
		} catch (Exception e) {
		}
	}
}
