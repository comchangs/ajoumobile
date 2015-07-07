/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.main;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.portal.*;
import ajou.amicom.ajouInfo.library.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class ajou_login extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ajou_login);
        
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String saved_id = pref.getString("id", "");
    	String saved_password = pref.getString("password", "");
    	String saved_stdnum = pref.getString("stdnum", "");
    	
        
        Bundle extras = getIntent().getExtras();
		final String login_menu = (String) extras.get("login_menu");
        
        Button btn = (Button) findViewById(R.id.login_button);

		final Util util = new Util();

		final EditText id = (EditText) findViewById(R.id.login_id_input);
		final EditText password = (EditText) findViewById(R.id.login_password_input);
		
		if(!saved_id.toString().equals("") && !saved_password.toString().equals("")) {
			
			try {
				if(util.loginTest2(saved_id, saved_password)) {
				
					// LoginData Save
					mainmenu_btn.ajou_login = true;
					//mainmenu_btn.student_id = util.getStudentId();
					//mainmenu_btn.ajou_id = saved_id;
					//mainmenu_btn.ajou_pw = saved_password;
					SharedPreferences pref_save = getSharedPreferences("pref", Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
					SharedPreferences.Editor editor = pref_save.edit(); // Editor를 불러옵니다.
					editor.putString("stdnum", util.getStudentId());
					editor.putString("temp_student_id", util.getStudentId());
					editor.putString("temp_id", saved_id);
					editor.putString("temp_password", saved_password);
					editor.commit(); // 저장합니다.
					
					if(login_menu.equals("portal")) {
						Intent intent = new Intent(ajou_login.this, portal.class);
						startActivity(intent);
						finish();
					} else if (login_menu.equals("library")) {
						Intent intent = new Intent(ajou_login.this, library.class);
						startActivity(intent);
						finish();
					} else if(login_menu.equals("timetable")) {
						Intent intent = new Intent(ajou_login.this, timetable.class);
						startActivity(intent);
						finish();
					}
				}
			} catch (Exception e) {
				Toast.makeText(this, "자동 로그인이 실패하였습니다.\n다시 로그인을 해주세요.", 5000).show();
			}
    	}
		
		
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userid = id.getText().toString();
				String userpassword = password.getText().toString();

				try {
					if (util.loginTest2(userid, userpassword)) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG, "[LOGIN SCREEN] ID: "+userid);
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG, "[LOGIN SCREEN] PW: "+userpassword);
						
						// LoginData Save
						mainmenu_btn.ajou_login = true;
						//mainmenu_btn.student_id = util.getStudentId();
						//mainmenu_btn.ajou_id = userid;
						//mainmenu_btn.ajou_pw = userpassword;
						
						SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
						SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다.
						editor.putString("stdnum", util.getStudentId());
						editor.putString("temp_student_id", util.getStudentId());
						editor.putString("temp_id", userid.toString());
						editor.putString("temp_password", userpassword.toString());
						editor.commit(); // 저장합니다.
						
						CheckBox auto = (CheckBox) findViewById(R.id.auto_login_checkBox1);
						if(auto.isChecked()) {
							editor.putString("id", id.getText().toString());
							editor.putString("password", password.getText().toString());
							editor.commit(); // 저장합니다.
						}
						
						if(login_menu.equals("portal")) {
							Intent intent = new Intent(ajou_login.this, portal.class);
							startActivity(intent);
							finish();
						} else if (login_menu.equals("library")) {
							Intent intent = new Intent(ajou_login.this, library.class);
							startActivity(intent);
							finish();
						} else if(login_menu.equals("timetable")) {
							Intent intent = new Intent(ajou_login.this, timetable.class);
							startActivity(intent);
							finish();
						}
					} else {
						new AlertDialog.Builder(ajou_login.this).setTitle("로그인 오류")
								.setMessage("아이디와 비밀번호를 확인하세요")
								.setPositiveButton("확인",
										new DialogInterface.OnClickListener() {

											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).show();
					}
				} catch (Exception e) {
				}
			}
		});
	}
}
