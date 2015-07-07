/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.food;

import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.show.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class food_search extends ListActivity {
	/** Called when the activity is first created. */
	ArrayAdapter <String> mAdapter;
	Vector<String[]> parsedata = new Vector<String[]>();
    static int i = 0;
    static int j = 0;
    static String restaurant_name;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.food);  	
        
        Bundle extras = getIntent().getExtras();
        restaurant_name = URLEncoder.encode((String) extras.get("restaurant_name"));

        mAdapter = new ArrayAdapter<String>(
        		getApplicationContext(), 
        		R.layout.main_list_item, 
        		new ArrayList<String>());
        
        setListAdapter(mAdapter);
        
        i = 0;
        j = 0;
        dbXml();
        
        Button bt = (Button)findViewById(R.id.food_add_button1);
        bt.setOnClickListener(new OnClickListener(){


			public void onClick(View arg0) {
				dbXml();
			}
		});
    }
    
	private void dbXml() {
		//parsedata = new String[30][8];
		String temp[] = new String[2];
		
		try{
        	URL text = new URL( "http://dev.jwnc.net/ajoumobile/food_search.php?restaurant_name="+restaurant_name+"&first="+i );

        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();

        	parser.setInput( text.openStream(), null );

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	String tag;
        	
        	
        	boolean inNo = false;
        	boolean inName = false;
        	boolean inDebug = false;

        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		switch(parserEvent){

        		case XmlPullParser.TEXT:
        			tag = parser.getName();
        			if (inNo) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] no = " + parser.getText() );
        				i++;
        				Log.e("i", Integer.toString(i));
        				temp[0] = parser.getText();
        			}
        			if (inName) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] restaurant_name = " + parser.getText() ); 
        				temp[1] = parser.getText();
        				parsedata.add(temp);
        				temp = new String[2];
        			}
        			if (inDebug) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] debug = " + parser.getText() ); 
        				//Toast.makeText(food.this, parser.getText(), 5000).show();
        			}
        			break;
        			
        		case XmlPullParser.END_TAG:
        			tag = parser.getName();
        			if (tag.compareTo("no") == 0) {
        				inNo = false;
        			}
        			if (tag.compareTo("restaurant_name") == 0) {
        				inName = false;
        			}
        			if (tag.compareTo("debug") == 0) {
        				inDebug = false;
        			}
        			break;	
        			
        		case XmlPullParser.START_TAG:
        			tag = parser.getName();

        			if (tag.compareTo("no") == 0) {
        				inNo = true;
        			}
        			if (tag.compareTo("restaurant_name") == 0) {
        				inName = true;
        			}
        			if (tag.compareTo("debug") == 0) {
        				inDebug = true;
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
    		mAdapter.add((String)(temp[1].toString()));
    		j++;
    	}
    }

    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String temp[];
		
		Intent intent = new Intent(food_search.this, foodView.class);
		temp = (String[])(parsedata.elementAt(position));
		intent.putExtra("fooddata", (String)(temp[0].toString()));
		startActivity(intent);
	}
}