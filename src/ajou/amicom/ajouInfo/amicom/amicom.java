/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.amicom;

import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class amicom extends ListActivity {
	/*
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone);

        //startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "010-7131-3519")));
	}
	*/
	ArrayAdapter <String> mAdapter;
	static String[][] parsedata;
    static int i = 0;
    static int j = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amicom);  	

        mAdapter = new ArrayAdapter<String>(
        		getApplicationContext(), 
        		android.R.layout.simple_list_item_1, 
        		new ArrayList<String>());
        
        setListAdapter(mAdapter);
        
        i = 0;
        dbXml();
        
        //Button bt = (Button)findViewById(R.id.btn_logalllist);
        /*bt.setOnClickListener(new OnClickListener(){


			public void onClick(View arg0) {
				dbXml();
				addStringData();
			}
		});
		*/
    }
    
	private void dbXml() {
		parsedata = new String[7][3];
		j = 0;
		try{
        	URL text = new URL( "http://dev.jwnc.net/ajoumobile/excutive.xml" );

        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();

        	parser.setInput( text.openStream(), null );

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	String tag;
        	
        	
        	boolean inPosition = false;
        	boolean inName = false;
        	boolean inPhone = false;
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		switch(parserEvent){

        		case XmlPullParser.TEXT:
        			tag = parser.getName();
        			if (inPosition) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] position = " + parser.getText() );
        				j++;
        				i++;
        				parsedata[j-1][0] = parser.getText();
        			}
        			if (inName) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] name = " + parser.getText() );
        				parsedata[j-1][1] = parser.getText();                  	
        			}
        			if (inPhone) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] phone = " + parser.getText() ); 
        				parsedata[j-1][2] = parser.getText();
        			}
        			break;
        			
        			
        		case XmlPullParser.END_TAG:
        			tag = parser.getName();
        			if (tag.compareTo("position") == 0) {
        				inPosition = false;
        			}
        			if (tag.compareTo("name") == 0) {
        				inName = false;
        			}
        			if (tag.compareTo("phone") == 0) {
        				inPhone = false;
        			}
        			break;	
        			
        		case XmlPullParser.START_TAG:
        			tag = parser.getName();

        			if (tag.compareTo("position") == 0) {
        				inPosition = true;
        			}
        			if (tag.compareTo("name") == 0) {
        				inName = true;
        			}
        			if (tag.compareTo("phone") == 0) {
        				inPhone = true;
        			}
        			break;

        		}
        		parserEvent = parser.next();
        	}
        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] End");
        	
            addStringData();
        }catch( Exception e ){
        	if(intro.DEBUG) Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
        }
	}
	
    private void addStringData(){
    	for(int z= 0; z < j; z++) {
	    	mAdapter.add(parsedata[z][0].toString()+" "+parsedata[z][1].toString());
    	}
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + parsedata[position][2].toString())));
	}
}
