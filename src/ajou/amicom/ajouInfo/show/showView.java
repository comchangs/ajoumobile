/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.show;

import java.io.*;
import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class showView extends Activity {
	
	static String[] parsedata;
    static String no;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_view);  
        
        Bundle extras = getIntent().getExtras();
		no = (String) extras.get("showdata");
        
        dbXml();
        
        ImageView img = (ImageView)findViewById(R.id.show_imageView1);

		String imageurl = "http://dev.jwnc.net/ajoumobile/show_img/"+parsedata[5].toString();
		try {
			InputStream is = new URL(imageurl).openStream();
			Bitmap bit = BitmapFactory.decodeStream(is);
			img.setImageBitmap(bit);
			is.close();
		} catch (Exception e) {;}
		
        TextView tv1 = (TextView)findViewById(R.id.show_textView1);
        tv1.setText(parsedata[1].toString());
        
        TextView tv2 = (TextView)findViewById(R.id.show_textView2);
        tv2.setText("공연일시: " + parsedata[2].toString());
        
        TextView tv3 = (TextView)findViewById(R.id.show_textView3);
        tv3.setText("공연소개: " + parsedata[3].toString());
        
        TextView tv4 = (TextView)findViewById(R.id.show_textView4);
        tv4.setText(parsedata[4].toString());
    }
    
	private void dbXml() {
		parsedata = new String[6];
		
		for (int i=0; i<6; i++) {
			parsedata[i] = "정보 없음";
		}
		
		try{
        	URL text = new URL( "http://dev.jwnc.net/ajoumobile/show_view.php?no="+no );
        	Log.i("Parse URL", text.toString());
        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();

        	parser.setInput( text.openStream(), null );

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	String tag;
        	
        	
        	boolean inNo = false;
        	boolean inName = false;
        	boolean inDatetime = false;
        	boolean inContents = false;
        	boolean inClubname = false;
        	boolean inPoster = false;
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		switch(parserEvent){

        		case XmlPullParser.TEXT:
        			tag = parser.getName();
        			if (inNo) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] no = " + parser.getText() );
        				parsedata[0] = parser.getText();
        			}
        			if (inName) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] name = " + parser.getText() );
        				parsedata[1] = parser.getText();                  	
        			}
        			if (inDatetime) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] datetime = " + parser.getText() ); 
        				parsedata[2] = parser.getText();
        			}
        			if (inContents) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] contents = " + parser.getText() );
        				parsedata[3] = parser.getText();
        			}
        			if (inClubname) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] clubname = " + parser.getText() );  
        				parsedata[4] = parser.getText();
        			}
        			if (inPoster) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] poster = " + parser.getText() );  
        				parsedata[5] = parser.getText();
        			}
        			break;
        			
        		case XmlPullParser.END_TAG:
        			tag = parser.getName();
        			if (tag.compareTo("no") == 0) {
        				inNo = false;
        			}
        			if (tag.compareTo("name") == 0) {
        				inName = false;
        			}
        			if (tag.compareTo("datetime") == 0) {
        				inDatetime = false;
        			}
        			if (tag.compareTo("contents") == 0) {
        				inContents = false;
        			}
        			if (tag.compareTo("clubname") == 0) {
        				inClubname = false;
        			}
        			if (tag.compareTo("poster") == 0) {
        				inPoster = false;
        			}
        			break;	
        			
        		case XmlPullParser.START_TAG:
        			tag = parser.getName();

        			if (tag.compareTo("no") == 0) {
        				inNo = true;
        			}
        			if (tag.compareTo("name") == 0) {
        				inName = true;
        			}
        			if (tag.compareTo("datetime") == 0) {
        				inDatetime = true;
        			}
        			if (tag.compareTo("contents") == 0) {
        				inContents = true;
        			}
        			if (tag.compareTo("clubname") == 0) {
        				inClubname = true;
        			}
        			if (tag.compareTo("poster") == 0) {
        				inPoster = true;
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
