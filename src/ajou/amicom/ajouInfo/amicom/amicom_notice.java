/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.amicom;

import java.net.*;
import java.util.*;

import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.main.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.webkit.*;
import android.widget.*;

public class amicom_notice extends ListActivity {

	ArrayAdapter<String> mAdapter;
	ArrayList<String> parsedata_title = new ArrayList<String>();
	ArrayList<String> parsedata_link = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.amicom_notice);

		mAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.main_list_item, new ArrayList<String>());

		setListAdapter(mAdapter);

		dbXml();

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
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] End");

			addStringData();
		} catch (Exception e) {
			if(intro.DEBUG) Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
		}
	}

	private void addStringData() {
		for (int z = 1; z < parsedata_title.size(); z++) {
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
		
		Intent intent = new Intent(amicom_notice.this, amicom_notice_view.class);
		intent.putExtra("url", parsedata_link.get(position + 1).toString());
		startActivity(intent);
	}
}
