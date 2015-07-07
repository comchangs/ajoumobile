/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import java.util.Vector;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ElectronicAttendance extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.protal_electronic_attendance);
		
		
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
		
		ElectronicAttendanceControl eControl = new ElectronicAttendanceControl();
		Vector<ElectronicAttendanceVO> eVO = null;
		Bundle extras = getIntent().getExtras();
		String id = temp_id; // 홈페이지 id
		String password = temp_password; // 홈페이지 password
		String stdnum = temp_student_id; // 홈페이지 password
		String cookie = null;
		String[] className = null;
		try {
			eVO = eControl.method(id, password, stdnum);
			cookie = eControl.getCookie();
		} catch (Exception e) {
			//Intent intent = new Intent(this, ajou_Login.class);
			//startActivity(intent);
		}
		
		final String cookie2 = cookie;
		final Vector<ElectronicAttendanceVO> eVO3 = eVO;
		int size = eVO3.size();
		className = new String[size];
		for(int i=0;i<size;++i){
			ElectronicAttendanceVO eVO2 = eVO3.get(i);
			className[i] = eVO2.getClassName();
		}
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this,
				R.layout.main_list_item, className);
		ListView subMenu = (ListView) findViewById(R.id.list2);
		subMenu.setAdapter(adapt);
		subMenu.setTextFilterEnabled(true);
		subMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ElectronicAttendance.this,
						ClassAttendance.class);
				ElectronicAttendanceVO eVO2 = eVO3.get(position);
				intent.putExtra("lec_year", eVO2.getLec_year());
				intent.putExtra("lec_no", eVO2.getLec_no());
				intent.putExtra("lec_term", eVO2.getLec_term());
				intent.putExtra("lec_division_code",
						eVO2.getLec_division_code());
				intent.putExtra("lec_use_opus",
						eVO2.getLec_use_opus());
				intent.putExtra("show", eVO2.getShow());
				intent.putExtra("cookie", cookie2);
				if ((eVO2.getLec_use_opus() == "N")) {
					new AlertDialog.Builder(ElectronicAttendance.this)
							.setTitle("전자 출결")
							.setMessage("전자출결이 아닌 과목 입니다")
							.setPositiveButton("확인",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				} else
					startActivity(intent);
			}
		});
		if(subMenu.getCount() == 0) {
			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					this);
			alert_internet_status.setTitle("조회불가");
			alert_internet_status
					.setMessage("현재 홈페이지 상에 조회된 전자출석 과목이 없습니다.");
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
