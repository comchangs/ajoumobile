/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.phone;

import java.io.*;
import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class phone_View extends Activity {
	
	static String[] parsedata;
	static String no;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone_view);  	
        
        Bundle extras = getIntent().getExtras();
		no = (String) extras.get("phonedata");
		
        dbXml();
        
        

		TextView tv1 = (TextView)findViewById(R.id.food_textView1);
        tv1.setText(parsedata[1].toString());
        
        TextView tv2 = (TextView)findViewById(R.id.food_textView2);
        tv2.setText(parsedata[2].toString());
        
        TextView tv3 = (TextView)findViewById(R.id.food_textView3);
        tv3.setText(parsedata[3].toString());
        
        TextView tv4 = (TextView)findViewById(R.id.food_textView4);
        tv4.setText("031-219-" + parsedata[4].toString());
        
        Button launch = (Button)findViewById(R.id.food_button1);
        launch.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    		   startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:031-219-" + parsedata[4].toString())));
    	   }
         
        });
        
    }
    
	private void dbXml() {
		parsedata = new String[10];
		
		for (int i=0; i<10; i++) {
			parsedata[i] = "정보 없음";
		}
		
		try{
        	URL text = new URL( "http://dev.jwnc.net/ajoumobile/phone_view.php?no="+no);

        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();

        	parser.setInput( text.openStream(), null );

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	String tag;
        	
        	
        	boolean inNo = false;
        	boolean inBigType = false;
        	boolean inSmallType = false;
        	boolean inName = false;
        	boolean inTel = false;
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		switch(parserEvent){

        		case XmlPullParser.TEXT:
        			tag = parser.getName();
        			if (inNo) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] no = " + parser.getText() );
        				parsedata[0] = parser.getText();
        			}
        			if (inBigType) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] big_type = " + parser.getText() );
        				parsedata[1] = parser.getText();                  	
        			}
        			if (inSmallType) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] small_type = " + parser.getText() ); 
        				parsedata[2] = parser.getText();
        			}
        			if (inName) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] name = " + parser.getText() );
        				parsedata[3] = parser.getText();
        			}
        			if (inTel) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] tel = " + parser.getText() );  
        				parsedata[4] = parser.getText();
        			}
        			break;
        			
        		case XmlPullParser.END_TAG:
        			tag = parser.getName();
        			if (tag.compareTo("no") == 0) {
        				inNo = false;
        			}
        			if (tag.compareTo("big_type") == 0) {
        				inBigType = false;
        			}
        			if (tag.compareTo("small_type") == 0) {
        				inSmallType = false;
        			}
        			if (tag.compareTo("name") == 0) {
        				inName = false;
        			}
        			if (tag.compareTo("tel") == 0) {
        				inTel = false;
        			}
        			break;	
        			
        		case XmlPullParser.START_TAG:
        			tag = parser.getName();

        			if (tag.compareTo("no") == 0) {
        				inNo = true;
        			}
        			if (tag.compareTo("big_type") == 0) {
        				inBigType = true;
        			}
        			if (tag.compareTo("small_type") == 0) {
        				inSmallType = true;
        			}
        			if (tag.compareTo("name") == 0) {
        				inName = true;
        			}
        			if (tag.compareTo("tel") == 0) {
        				inTel = true;
        			}
        			break;




        		}
        		parserEvent = parser.next();
        	}
        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] End");
        }catch( Exception e ){
        	if(intro.DEBUG) Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
        }
	}

}
