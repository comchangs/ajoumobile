/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.main;

import java.util.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.amicom.*;
import ajou.amicom.ajouInfo.bus.*;

import ajou.amicom.ajouInfo.creator.*;
import ajou.amicom.ajouInfo.food.*;
import ajou.amicom.ajouInfo.library.*;
import ajou.amicom.ajouInfo.main.*;
import ajou.amicom.ajouInfo.map.*;

import ajou.amicom.ajouInfo.phone.*;
import ajou.amicom.ajouInfo.portal.*;
import ajou.amicom.ajouInfo.setting.*;
import ajou.amicom.ajouInfo.twitter.*;
import ajou.amicom.ajouInfo.show.*;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class mainMenu extends ListActivity {
	
	// Login flag
	//public static boolean ajou_login = false; 
	//public static boolean twitter_login = false; 
	
	// Login Data
	//public static String student_id = null;
	//public static String ajou_id = null;
	//public static String ajou_pw = null;
	//public static String twitter_id = null;
	//public static String twitter_pw = null;
	
	ArrayAdapter <String> mAdapter;
	
	static String menudata[] = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        
        

        menudata = new String[11];
        
        menudata[0] = getResources().getString(R.string.mainmenu_string01);
        menudata[1] = getResources().getString(R.string.mainmenu_string02);
        menudata[2] = getResources().getString(R.string.mainmenu_string03);
        menudata[3] = getResources().getString(R.string.mainmenu_string04);
        menudata[4] = getResources().getString(R.string.mainmenu_string05);
        menudata[5] = getResources().getString(R.string.mainmenu_string06);
        menudata[6] = getResources().getString(R.string.mainmenu_string07);
        menudata[7] = getResources().getString(R.string.mainmenu_string08);
        menudata[8] = getResources().getString(R.string.mainmenu_string09);
        menudata[9] = getResources().getString(R.string.mainmenu_string10);
        menudata[10] = getResources().getString(R.string.mainmenu_string11);
        
    	
        mAdapter = new ArrayAdapter<String>(
        		getApplicationContext(), 
        		android.R.layout.simple_list_item_1, 
        		new ArrayList<String>());
        
        setListAdapter(mAdapter);
        
        addStringData();
        
	}
	
	private void addStringData(){
     	for(int z= 0; z < 11; z++) {
	    	mAdapter.add(menudata[z].toString());
    	}
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		switch (position) {
		case 0: {
			Intent intent = new Intent(mainMenu.this, amicom_notice.class);
			startActivity(intent);
			break;
		}
		
		case 1: {
        		//if(ajou_login == true) {
        		//	Intent intent = new Intent(mainMenu.this, portal.class);
        		//	startActivity(intent);
        		//} else {
        		//	Intent intent = new Intent(mainMenu.this, ajou_login.class);
        		//	intent.putExtra("login_menu", "portal");
        		//	startActivity(intent);
        		//}
        		break;
        	}
          
		/*case 1: {
        		if(ajou_login == true) {
        			Intent intent = new Intent(mainMenu.this, library.class);
        			startActivity(intent);
        		} else {
        			Intent intent = new Intent(mainMenu.this, ajou_login.class);
        			intent.putExtra("login_menu", "library");
        			startActivity(intent);
        		}
        		break;
        	}*/
		
		case 2: {
    			Intent intent = new Intent(mainMenu.this, cafeteria.class);
    			startActivity(intent);
    		break;
    	}
        
		case 3: {
				Intent intent = new Intent(mainMenu.this, foodCategory.class);
				startActivity(intent);
				break;
			}
        
		case 4: {
        		Intent intent = new Intent(mainMenu.this, NMapViewer_bus.class);
        		startActivity(intent);
        		break;
        	}
		
		case 5: {
			Intent intent = new Intent(mainMenu.this, show.class);
			startActivity(intent);
			break;
    		}
		
		case 6: {
    		//if(twitter_login == true) {
    			//Intent intent = new Intent(mainMenu.this, twitter.class);
		Intent intent = new Intent(mainMenu.this, twitterWebView.class);
    			startActivity(intent);
    		//} else {
    		//	Intent intent = new Intent(mainMenu.this, twitter_login.class);
    		//	startActivity(intent);
    		//}
    		break;
    		}
		
		case 7: {
				Intent intent = new Intent(mainMenu.this, phone.class);
				startActivity(intent);
				break;
			}

		case 8: {
				Intent intent = new Intent(mainMenu.this,NMapViewer.class);
				startActivity(intent);
				break;
			}
        
		case 9: {
				Intent intent = new Intent(mainMenu.this, setting.class);
				startActivity(intent);
				break;
        	}
        
		case 10: {
				Intent intent = new Intent(mainMenu.this, creator.class);
				startActivity(intent);
				break;
        	}

		}
	}
	
}