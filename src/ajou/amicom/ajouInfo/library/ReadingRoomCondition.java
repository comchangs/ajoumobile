/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;

public class ReadingRoomCondition extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.library_readingroomconditionmenu);
		
		Button btn1 = (Button)findViewById(R.id.readingroomconditionmenu_btn1); //B1
		Button btn2 = (Button)findViewById(R.id.readingroomconditionmenu_btn2); //C1
		Button btn3 = (Button)findViewById(R.id.readingroomconditionmenu_btn3); //C2
		Button btn4 = (Button)findViewById(R.id.readingroomconditionmenu_btn4); //D1
		//final ImageView iv = (ImageView)findViewById(R.id.readingroomconditionmenu_iv);
		final ImageView iv = (ImageView)findViewById(R.id.readingroomconditionmenu_iv);
		
		ReadingRoomConditionControl control = null;
		
		try {
			control = new ReadingRoomConditionControl();
		} catch (Exception e) {
		}
		
		ReadingRoomVO b1 = control.getB1();
		ReadingRoomVO c1 = control.getC1();
		ReadingRoomVO c2 = control.getC2();
		ReadingRoomVO d1 = control.getD1();
		
		if(b1.isOpen()){
			btn1.setText("잔여 좌석: "+b1.getRemain());
			btn1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ReadingRoomCondition.this, ReadingRoomCondition_view.class);
					intent.putExtra("url", StringURL.readingRoom03);
					startActivity(intent);
					//iv.setImageBitmap(control2.getURLImage(StringURL.readingRoom03));
				}
			});	
		}
		else if(!b1.isOpen())
			btn1.setText("폐실중입니다.");
		
		if(c1.isOpen()){
			btn2.setText("잔여 좌석: "+c1.getRemain());
			btn2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ReadingRoomCondition.this, ReadingRoomCondition_view.class);
					intent.putExtra("url", StringURL.readingRoom04);
					startActivity(intent);
					//iv.setImageBitmap(control2.getURLImage(StringURL.readingRoom04));
				}
			});	
		}
		else if(!c1.isOpen())
			btn2.setText("폐실중입니다.");
		
		if(c2.isOpen()){
			btn3.setText("잔여 좌석: "+c2.getRemain());
			btn3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ReadingRoomCondition.this, ReadingRoomCondition_view.class);
					intent.putExtra("url", StringURL.readingRoom05);
					startActivity(intent);
					//iv.setImageBitmap(control2.getURLImage(StringURL.readingRoom05));
				}
			});	
		}
		else if(!c2.isOpen())
			btn3.setText("폐실중입니다.");
		
		if(d1.isOpen()){
			btn4.setText("잔여 좌석: "+d1.getRemain());
			btn4.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(ReadingRoomCondition.this, ReadingRoomCondition_view.class);
					intent.putExtra("url", StringURL.readingRoom06);
					startActivity(intent);
					//iv.setImageBitmap(control2.getURLImage(StringURL.readingRoom06));
				}
			});	
		}
		else if(!d1.isOpen())
			btn4.setText("폐실중입니다.");
	}
	
}
