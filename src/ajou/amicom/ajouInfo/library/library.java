/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

import java.util.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.portal.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class library extends ListActivity {
	ArrayAdapter<String> mAdapter;

	static String menudata[] = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.library);

		menudata = new String[4];

		menudata[0] = getResources().getString(R.string.library_string01);
		menudata[1] = getResources().getString(R.string.library_string02);
		menudata[2] = getResources().getString(R.string.library_string03);
		menudata[3] = getResources().getString(R.string.library_string04);

		mAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.main_list_item, new ArrayList<String>());

		setListAdapter(mAdapter);

		addStringData();

	}

	private void addStringData() {
		for (int z = 0; z < 4; z++) {
			mAdapter.add(menudata[z].toString());
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		switch (position) {
		case 0: {
			//Intent intent = new Intent(library.this, BorrowBook.class);
			//startActivity(intent);
			Toast.makeText(this, "현재 개발중인 메뉴입니다.", 2000).show();
			break;
		}

		case 1: {
			Intent intent = new Intent(library.this, BorrowBook.class);
			startActivity(intent);
			break;
		}

		case 2: {
			Intent intent = new Intent(library.this, ReadingRoomCondition.class);
			startActivity(intent);
			break;
		}

		case 3: {
			Intent intent = new Intent(library.this, ReadingRoomExtend.class);
			startActivity(intent);
			break;
		}
		}
	}
}
