/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.main;

import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;

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
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class mainmenu_btn extends ListActivity {

	// Login flag
	public static boolean ajou_login = false;
	public static boolean twitter_login = false;

	// Login Data
	//public static String student_id = null;
	//public static String ajou_id = null;
	//public static String ajou_pw = null;
	public static String twitter_id = null;
	public static String twitter_pw = null;
	
	ArrayAdapter<String> mAdapter;
	ArrayList<String> parsedata_title = new ArrayList<String>();
	ArrayList<String> parsedata_link = new ArrayList<String>();
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainmenu_btn);

		mAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.main_notice_list_item, new ArrayList<String>());

		setListAdapter(mAdapter);

		dbXml();
		
		ImageButton btn_timetable = (ImageButton) findViewById(R.id.btn_timetable);
		btn_timetable.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				if(ajou_login == true) {
        			Intent intent = new Intent(mainmenu_btn.this, timetable.class);
        			startActivity(intent);
        		} else {
        			//Intent intent = new Intent(mainmenu_btn.this, timetable.class);
        			Intent intent = new Intent(mainmenu_btn.this, ajou_login.class);
        			intent.putExtra("login_menu", "timetable");
        			startActivity(intent);
        		}
			}

		});

		ImageButton btn_portal = (ImageButton) findViewById(R.id.btn_portal);
		btn_portal.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				if (ajou_login == true) {
					Intent intent = new Intent(mainmenu_btn.this, portal.class);
					startActivity(intent);
				} else {
					//Intent intent = new Intent(mainmenu_btn.this, portal.class);
					Intent intent = new Intent(mainmenu_btn.this, ajou_login.class);
					intent.putExtra("login_menu", "portal");
					startActivity(intent);
				}
			}

		});

		ImageButton btn_library = (ImageButton) findViewById(R.id.btn_library);
		btn_library.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				if (ajou_login == true) {
					Intent intent = new Intent(mainmenu_btn.this, library.class);
					startActivity(intent);
				} else {
					//Intent intent = new Intent(mainmenu_btn.this, library.class);
					Intent intent = new Intent(mainmenu_btn.this, ajou_login.class);
					intent.putExtra("login_menu", "library");
					startActivity(intent);
				}
			}

		});

		ImageButton btn_menu = (ImageButton) findViewById(R.id.btn_menu);
		btn_menu.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, menuonly.class);
				startActivity(i);
			}

		});

		ImageButton btn_food = (ImageButton) findViewById(R.id.btn_food);
		btn_food.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, foodCategory.class);
				startActivity(i);
			}

		});

		ImageButton btn_bus = (ImageButton) findViewById(R.id.btn_bus);
		btn_bus.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, NMapViewer_bus.class);
				startActivity(i);
			}

		});

		ImageButton btn_map = (ImageButton) findViewById(R.id.btn_map);
		btn_map.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, NMapViewer.class);
				startActivity(i);
			}

		});

		ImageButton btn_phone = (ImageButton) findViewById(R.id.btn_phone);
		btn_phone.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, phone_main.class);
				startActivity(i);
			}

		});

		ImageButton btn_notice = (ImageButton) findViewById(R.id.btn_notice);
		btn_notice.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, amicom_notice.class);
				startActivity(i);
			}

		});

		ImageButton btn_creator = (ImageButton) findViewById(R.id.btn_creator);
		btn_creator.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, creator.class);
				startActivity(i);
			}

		});

		ImageButton btn_bug = (ImageButton) findViewById(R.id.btn_bug);
		btn_bug.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				//Intent i = new Intent(mainmenu_btn.this, creatorBugReport.class);
				//startActivity(i);
				
				Uri uri = Uri.parse("mailto:아주모바일개발팀<comchangsdev@gmail.com>");
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				
				startActivity(it);
				/*
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.addCategory(Intent.CATEGORY_DEFAULT); 
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "[Ajou Mobile BUG] ");
				sendIntent.setType("message/rfc882");
				String[] mailto = { "아주모바일개발팀<comchangsdev@gmail.com>" };
				sendIntent.putExtra(Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(sendIntent, "Send email..."));
				//overridePendingTransition(R.anim.fade, R.anim.hold);
				*/
				
			}

		});

		ImageButton btn_setting = (ImageButton) findViewById(R.id.btn_setting);
		btn_setting.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(mainmenu_btn.this, setting.class);
				startActivity(i);
			}

		});
	}
	
	private void dbXml() {
		try {
			URL text = new URL("http://amicom.jwnc.net/ajoumobile_notice/rss");
			// URL text = new URL(
			// "http://dev.jwnc.net/sysprog/log_read.php?first=1");

			XmlPullParserFactory parserCreator = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();

			parser.setInput(text.openStream(), null);

			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
			int parserEvent = parser.getEventType();
			String tag;

			boolean inTitle = false;
			boolean inLink = false;

			while (parserEvent != XmlPullParser.END_DOCUMENT) {
				switch (parserEvent) {

				case XmlPullParser.TEXT:
					tag = parser.getName();
					if (inTitle) {
						if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] title = " + parser.getText());
						parsedata_title.add(parser.getText().toString());
					}
					if (inLink) {
						parsedata_link.add(parser.getText().toString());

					}
					break;

				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.compareTo("title") == 0) {
						inTitle = false;
					}
					if (tag.compareTo("link") == 0) {
						inLink = false;
					}
					break;

				case XmlPullParser.START_TAG:
					tag = parser.getName();

					if (tag.compareTo("title") == 0) {
						inTitle = true;
					}
					if (tag.compareTo("link") == 0) {
						inLink = true;
					}
					break;
				}
				parserEvent = parser.next();
			}
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Eed");
			addStringData();
		} catch (Exception e) {
			if(intro.DEBUG) Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
			
		}
	}

	private void addStringData() {
		for (int z = 1; z < 2; z++) {
			Log.i("z", Integer.toString(z));
			mAdapter.add(parsedata_title.get(z).toString());
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		//Toast.makeText(this, Integer.toString(position), 600000).show();
		//Toast.makeText(this, parsedata_link.get(position + 1).toString(), 600000).show();
		
		Intent intent = new Intent(mainmenu_btn.this, amicom_notice_view.class);
		intent.putExtra("url", parsedata_link.get(position + 1).toString());
		startActivity(intent);
	}
}
