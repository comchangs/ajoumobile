/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import java.util.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;

public class notice extends Activity {
	WebView wv = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.portal_notice);

		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
    	
		Bundle extras = getIntent().getExtras();
		String id = (String) temp_id; // 홈페이지 id
		String pw = (String) temp_password; // 홈페이지 password
		String studentId = (String) temp_student_id; // 학번
		wv = (WebView) findViewById(R.id.notice_wv);
		wv.getSettings().setJavaScriptEnabled(true);

		NoticeControl control = null;
		try {
			control = new NoticeControl(id, pw, studentId);
		} catch (Exception e) {
			//Intent intent = new Intent(null, SchoolMenu.class);
			//startActivity(intent);
		}
		final Vector<NoticeVO> noticeVector = control.getNoticeVector();
		String title[] = new String[noticeVector.size()];
		for (int i = 0; i < noticeVector.size(); ++i) {
			title[i] = noticeVector.get(i).getTitle();
		}

		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, title);
		ListView subMenu = (ListView) findViewById(R.id.notice_list);
		subMenu.setAdapter(adapt);
		subMenu.setTextFilterEnabled(true);

		subMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NoticeVO nVO = noticeVector.get(position);
				String url = StringURL.notice03 + nVO.getNtc_seq()
						+ StringURL.notice04 + nVO.getPage();
				url = url + StringURL.notice05 + nVO.getPa_no()
						+ StringURL.notice06 + nVO.getFile_cnt();
				wv.setWebViewClient(new AjouWebViewClient());
				wv.loadUrl(url);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
			wv.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class AjouWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}
}
