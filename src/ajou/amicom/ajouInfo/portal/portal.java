/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import java.util.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.library.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class portal extends ListActivity {

	ArrayAdapter<String> mAdapter;

	static String menudata[] = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(intro.DEBUG) Log.i(intro.LOG_TAG, "portal on");
		setContentView(R.layout.portal);

		menudata = new String[8];

		menudata[0] = getResources().getString(R.string.portal_string01);
		menudata[1] = getResources().getString(R.string.portal_string02);
		menudata[2] = getResources().getString(R.string.portal_string03);
		menudata[3] = getResources().getString(R.string.portal_string04);
		menudata[4] = getResources().getString(R.string.portal_string05);
		menudata[5] = getResources().getString(R.string.portal_string06);
		menudata[6] = getResources().getString(R.string.portal_string07);
		menudata[7] = getResources().getString(R.string.portal_string08);

		mAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.main_list_item, new ArrayList<String>());

		setListAdapter(mAdapter);

		addStringData();

	}

	private void addStringData() {
		for (int z = 0; z < 8; z++) {
			mAdapter.add(menudata[z].toString());
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		switch (position) {

		case 0: {
			//Intent intent = new Intent(portal.this, ElectronicAttendance.class);
			//startActivity(intent);
			Toast.makeText(this, "현재 개발중인 메뉴입니다.", 2000).show();
			break;
		}
		
		case 1: {
			Intent intent = new Intent(portal.this, haksaplan.class);
			startActivity(intent);
			break;
		}
		
		case 2: {
			Intent intent = new Intent(portal.this, ElectronicAttendance.class);
			startActivity(intent);
			break;
		}

		case 3: {
			Intent intent = new Intent(portal.this, timetable.class);
			startActivity(intent);
			break;
		}
		
		case 4: {
			Intent intent = new Intent(portal.this, score.class);
			startActivity(intent);
			//Toast.makeText(this, "현재 개발중인 메뉴입니다.", 2000).show();
			break;
		}
		
		case 5: {
			Intent intent = new Intent(portal.this, eclass_notice.class);
			startActivity(intent);
			//Toast.makeText(this, "현재 개발중인 메뉴입니다.", 2000).show();
			break;
		}
		
		case 6: {
			Intent intent = new Intent(portal.this, eclass_note.class);
			intent.putExtra("Page", "1");
			startActivity(intent);
			//Toast.makeText(this, "현재 개발중인 메뉴입니다.", 2000).show();
			break;
		}
		
		case 7: {
			Intent intent = new Intent(portal.this, eclass_homework.class);
			intent.putExtra("Page", "1");
			startActivity(intent);
			//Toast.makeText(this, "현재 개발중인 메뉴입니다.", 2000).show();
			break;
		}
		
		}
	}

}
