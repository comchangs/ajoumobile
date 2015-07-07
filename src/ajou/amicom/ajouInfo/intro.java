/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo;

import ajou.amicom.ajouInfo.main.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class intro extends Activity {
	public static final String LOG_TAG = "Ajou Mobile";
	public static final boolean DEBUG = true;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.intro);

		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobile = false;
        boolean isWifi = false;
        boolean isWimax = false;
        try {
        	isMobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        } catch(Exception e) {
        	if(intro.DEBUG) Log.e(intro.LOG_TAG, "Can't Used at 3G Network");
        }
        try {
			isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        } catch(Exception e) {
        	if(intro.DEBUG) Log.e(intro.LOG_TAG, "Can't Used at Wifi Network");
        }
		try {
			isWimax = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX).isConnectedOrConnecting();
		} catch(Exception e) {
        	if(intro.DEBUG) Log.e(intro.LOG_TAG, "Can't Used at 4G Network");
        }
		if(intro.DEBUG) Log.e(intro.LOG_TAG, "Network Test = " + (isMobile || isWifi || isWimax));

		if (isMobile || isWifi || isWimax) {
			Handler mHandler = new Handler();
			mHandler.postDelayed(new Runnable() {
				public void run() {
					//Intent i = new Intent(intro.this, mainMenu.class);
					Intent i = new Intent(intro.this, mainmenu_btn.class);
					startActivity(i);
					if(intro.DEBUG) Log.i(intro.LOG_TAG, "Start");
					finish();
				}
			}, 3000); // 3000ms
		} else {
			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					this);
			alert_internet_status.setTitle("인터넷연결");
			alert_internet_status
					.setMessage("현재 인터넷 사용이 불가능한 상태입니다.\n이 어플리케이션은 인터넷 연결이 필요하오니 인터넷 연결상태를 확인히시고 다시 이용해 주세요.");
			alert_internet_status.setPositiveButton("닫기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss(); // 닫기
							finish();
						}
					});
			alert_internet_status.show();
		}
	}
}