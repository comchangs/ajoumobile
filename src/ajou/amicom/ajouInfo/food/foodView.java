/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.food;

import java.io.*;
import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.creator.*;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class foodView extends Activity {
	
	static String[] parsedata;
	static String no;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.food_view);  	
        
        Bundle extras = getIntent().getExtras();
		no = (String) extras.get("fooddata");
		
        dbXml();
        
        
        ImageView img = (ImageView)findViewById(R.id.food_imageView1);

		String imageurl = "http://dev.jwnc.net/ajoumobile/food_img/"+parsedata[9].toString();
		try {
			InputStream is = new URL(imageurl).openStream();
			Bitmap bit = BitmapFactory.decodeStream(is);
			img.setImageBitmap(bit);
			is.close();
		} catch (Exception e) {;}
		
		TextView tv1 = (TextView)findViewById(R.id.food_textView1);
        tv1.setText(parsedata[2].toString());
        
        TextView tv2 = (TextView)findViewById(R.id.food_textView2);
        tv2.setText(parsedata[1].toString());
        
        TextView tv3 = (TextView)findViewById(R.id.food_textView3);
        tv3.setText("전화번호: " + parsedata[3].toString());
        
        TextView tv4 = (TextView)findViewById(R.id.food_textView4);
        tv4.setText("영업시간: " + parsedata[4].toString());
        
        TextView tv5 = (TextView)findViewById(R.id.food_textView5);
        tv5.setText("배달여부: " + parsedata[5].toString());
        
        TextView tv6 = (TextView)findViewById(R.id.food_textView6);
        tv6.setText("메뉴\n" + parsedata[6].toString());
        
        ImageView img2 = (ImageView)findViewById(R.id.food_imageView2);
        String imageurl2 = "http://openapi.naver.com/map/getStaticMap?version=1.0&crs=EPSG:4326&center="+parsedata[7].toString()+","+parsedata[8].toString()+"&level=13&w=480&h=320&maptype=default&markers="+parsedata[7].toString()+","+parsedata[8].toString()+"&key=9d783f1cc865486314e037bacd00e88f&uri=amicom.jwnc.net";
		try {
			InputStream is = new URL(imageurl2).openStream();
			Bitmap bit = BitmapFactory.decodeStream(is);
			img2.setImageBitmap(bit);
			is.close();
		} catch (Exception e) {;}
        
        Button launch = (Button)findViewById(R.id.food_button1);
        launch.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    		   startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + parsedata[3].toString())));
    	   }
         
        });
        
    }
    
	private void dbXml() {
		parsedata = new String[10];
		
		for (int i=0; i<10; i++) {
			parsedata[i] = "정보 없음";
		}
		
		try{
        	URL text = new URL( "http://dev.jwnc.net/ajoumobile/food_view.php?no="+no);

        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();

        	parser.setInput( text.openStream(), null );

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	String tag;
        	
        	
        	boolean inNo = false;
        	boolean inCategory = false;
        	boolean inName = false;
        	boolean inTel = false;
        	boolean inTime = false;
        	boolean inDelivery = false;
        	boolean inMenu = false;
        	boolean inPosition_x = false;
        	boolean inPosition_y= false;
        	boolean inPhoto= false;
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		switch(parserEvent){

        		case XmlPullParser.TEXT:
        			tag = parser.getName();
        			if (inNo) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] no = " + parser.getText() );
        				parsedata[0] = parser.getText();
        			}
        			if (inCategory) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] category = " + parser.getText() );
        				parsedata[1] = parser.getText();                  	
        			}
        			if (inName) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] restaurant_name = " + parser.getText() ); 
        				parsedata[2] = parser.getText();
        			}
        			if (inTel) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] tel = " + parser.getText() );
        				parsedata[3] = parser.getText();
        			}
        			if (inTime) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] time = " + parser.getText() );  
        				parsedata[4] = parser.getText();
        			}
        			if (inDelivery) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] delivery = " + parser.getText() );  
        				parsedata[5] = parser.getText();
        			}
        			if (inMenu) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] menu = " + parser.getText() );
        				parsedata[6] = parser.getText();
        			}
        			if (inPosition_x) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] position_x = " + parser.getText() );  
        				parsedata[7] = parser.getText();
        			}
        			if (inPosition_y) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] position_y = " + parser.getText() );
        				parsedata[8] = parser.getText();
        			}
        			if (inPhoto) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] photo = " + parser.getText() );
        				parsedata[9] = parser.getText();
        			}
        			break;
        			
        		case XmlPullParser.END_TAG:
        			tag = parser.getName();
        			if (tag.compareTo("no") == 0) {
        				inNo = false;
        			}
        			if (tag.compareTo("category") == 0) {
        				inCategory = false;
        			}
        			if (tag.compareTo("restaurant_name") == 0) {
        				inName = false;
        			}
        			if (tag.compareTo("tel") == 0) {
        				inTel = false;
        			}
        			if (tag.compareTo("time") == 0) {
        				inTime = false;
        			}
        			if (tag.compareTo("delivery") == 0) {
        				inDelivery = false;
        			}
        			if (tag.compareTo("menu") == 0) {
        				inMenu = false;
        			}
        			if (tag.compareTo("position_x") == 0) {
        				inPosition_x = false;
        			}
        			if (tag.compareTo("position_y") == 0) {
        				inPosition_y = false;
        			}
        			if (tag.compareTo("photo") == 0) {
        				inPhoto = false;
        			}
        			break;	
        			
        		case XmlPullParser.START_TAG:
        			tag = parser.getName();

        			if (tag.compareTo("no") == 0) {
        				inNo = true;
        			}
        			if (tag.compareTo("category") == 0) {
        				inCategory = true;
        			}
        			if (tag.compareTo("restaurant_name") == 0) {
        				inName = true;
        			}
        			if (tag.compareTo("tel") == 0) {
        				inTel = true;
        			}
        			if (tag.compareTo("time") == 0) {
        				inTime = true;
        			}
        			if (tag.compareTo("delivery") == 0) {
        				inDelivery = true;
        			}
        			if (tag.compareTo("menu") == 0) {
        				inMenu = true;
        			}
        			if (tag.compareTo("position_x") == 0) {
        				inPosition_x = true;
        			}
        			if (tag.compareTo("position_y") == 0) {
        				inPosition_y = true;
        			}
        			if (tag.compareTo("photo") == 0) {
        				inPhoto = true;
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
