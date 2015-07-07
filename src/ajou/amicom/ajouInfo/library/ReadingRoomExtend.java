/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

import java.text.*;
import java.util.*;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.food.*;
import ajou.amicom.ajouInfo.main.*;

public class ReadingRoomExtend extends Activity {

	SeatExtendControl control = null;
	SeatExtendControl control2 = null;
	String seatNumber = null;
	String seatTime = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.library_readingroomextend);
		
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
		
		//Bundle extras = getIntent().getExtras();
		String id = temp_id; // 홈페이지 id
		String pw = temp_password; // 홈페이지 password
		String studentId = temp_student_id; // 학번
		
		TextView tv = (TextView)findViewById(R.id.readingroomextend_tv01);
		TextView tv2 = (TextView)findViewById(R.id.readingroomextend_tv02);
		Button btn = (Button)findViewById(R.id.readingroomextend_btn01);
		
		try {
			control = new SeatExtendControl(id, pw, studentId);
			
			seatNumber = control.getSeatNumber();
			seatTime = control.getSeatTime();
			
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[좌석연장] getSeatNumber = " + seatNumber );
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[좌석연장] getSeatTime = " + seatTime );
			
			tv.setText(seatNumber);
			tv2.setText(seatTime);
			control2 = control;
		} catch (Exception e) {
		}
		
		btn.setOnClickListener(new OnClickListener(){


			public void onClick(View arg0) {
				if(seatNumber.equals("열람실 발급 내역이 없습니다")) {
					Toast.makeText(ReadingRoomExtend.this, "현재는 연장을 할 수 없습니다.",3500).show();
				} else {
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMddHHmmss");
					
					String curdate = dateFormat.format(calendar.getTime()).toString();
					if(intro.DEBUG) Log.i(intro.LOG_TAG, "CurDate: "+curdate);
					
					//TODO 현재시간과 비교하여 2시간 이후 인지 체크 - seatTime
					if(seatNumber.equals("열람실 발급 내역이 없습니다")) {
						Toast.makeText(ReadingRoomExtend.this, "현재는 연장 가능한 시간이 아닙니다.",3500).show();
					} else {
						try {
							control.extend();
							AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(ReadingRoomExtend.this);
							alert_internet_status.setTitle("연장 결과");
							alert_internet_status.setMessage(control.getMessage());
							alert_internet_status.setPositiveButton("확인",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss(); // 닫기
											Intent intent = new Intent(ReadingRoomExtend.this, ReadingRoomExtend.class);
											startActivity(intent);
											finish();
										}
									});
							alert_internet_status.show();
							//Toast.makeText(ReadingRoomExtend.this, control.getMessage(),3500).show();
							//Toast.makeText(ReadingRoomExtend.this, "연장되었습니다.",3500).show();
							//Intent intent = new Intent(ReadingRoomExtend.this, ReadingRoomExtend.class);
							//startActivity(intent);
							//finish();
						} catch (Exception e) {
							Toast.makeText(ReadingRoomExtend.this, "연장 과정에 에러가 발생하였습니다.",3500).show();
						}
						//Toast.makeText(ReadingRoomExtend.this, "현재는 연장을 할 수 없습니다.",3500).show();
					}
				}
			}
		});
	}	
}
