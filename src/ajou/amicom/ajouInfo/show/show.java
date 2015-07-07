/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.show;

import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;


import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class show extends ListActivity {
	ArrayAdapter <String> mAdapter;
	Vector<String[]> parsedata = new Vector<String[]>();
    static int i = 0;
    static int j = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);  	

        mAdapter = new ArrayAdapter<String>(
        		getApplicationContext(), 
        		android.R.layout.simple_list_item_1, 
        		new ArrayList<String>());
        
        setListAdapter(mAdapter);
        
        i = 0;
        j = 0;
        dbXml();
        addStringData();
        
        Button bt = (Button)findViewById(R.id.show_add_button1);
        bt.setOnClickListener(new OnClickListener(){


			public void onClick(View arg0) {
				dbXml();
			}
		});
    }
    
	private void dbXml() {
		//parsedata = new String[30][3];
		String temp[] = new String[3];
		
		try{
        	URL text = new URL( "http://dev.jwnc.net/ajoumobile/show.php?first="+i );

        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();

        	parser.setInput( text.openStream(), null );

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	String tag;
        	
        	
        	boolean inNo = false;
        	boolean inName = false;
        	boolean inClubname = false;
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		switch(parserEvent){

        		case XmlPullParser.TEXT:
        			tag = parser.getName();
        			if (inNo) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] no = " + parser.getText() );
        				i++;
        				temp[0] = parser.getText();
        			}
        			if (inName) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] name = " + parser.getText() );
        				temp[1] = parser.getText();                  	
        			}
        			if (inClubname) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] clubname = " + parser.getText() );  
        				temp[2] = parser.getText();
        				parsedata.add(temp);
        				temp = new String[3];
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
        			if (tag.compareTo("clubname") == 0) {
        				inClubname = false;
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
        			if (tag.compareTo("clubname") == 0) {
        				inClubname = true;
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
    	
    	
    	String temp[] = new String[3];
    	while(i != j) {
    		temp = (String[])(parsedata.elementAt(j));
    		mAdapter.add((String)("[" + temp[2].toString() + "] " + temp[1].toString()));
    		j++;
    	}
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String temp[];
		
		Intent intent = new Intent(show.this, showView.class);
		temp = (String[])(parsedata.elementAt(position));
		intent.putExtra("showdata", (String)(temp[0].toString()));
		startActivity(intent);
	}
}
