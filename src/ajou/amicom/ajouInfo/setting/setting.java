/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.setting;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.food.*;
import ajou.amicom.ajouInfo.library.*;
import ajou.amicom.ajouInfo.main.*;
import ajou.amicom.ajouInfo.portal.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class setting extends Activity {
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting);
        
        
        SharedPreferences pref_load = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String saved_id = pref_load.getString("id", "");
    	String saved_password = pref_load.getString("password", "");

    	String temp_id = pref_load.getString("temp_id", "");
    	String temp_password = pref_load.getString("temp_password", "");
    	String temp_student_id = pref_load.getString("temp_student_id", "");
    	
    	final EditText id = (EditText) findViewById(R.id.setting_editText1);
    	final EditText password = (EditText) findViewById(R.id.setting_editText2);
		final TextView auto = (TextView) findViewById(R.id.setting_textView2);
		Button logout = (Button)findViewById(R.id.setting_button1);
		//CheckBox auto = (CheckBox)findViewById(R.id.setting_checkBox1);
		
		
		final SharedPreferences pref_save = getSharedPreferences("pref", Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
		final SharedPreferences.Editor editor = pref_save.edit(); // Editor를 불러옵니다.
		
		if(!saved_id.toString().equals("") && !saved_password.toString().equals("")) {

			mainmenu_btn.ajou_login = true;
			//mainmenu_btn.ajou_id = saved_id;
			//mainmenu_btn.ajou_pw = saved_password;
    		auto.setText("자동 로그인이 설정 되어 있습니다.\n로그아웃하면 자동 로그인 설정이 해제됩니다.");

    	}
		
		if(mainmenu_btn.ajou_login == true)
		{
			id.setText(temp_id);
    		password.setText(temp_password);
			
			logout.setOnClickListener(new Button.OnClickListener(){
		    	   public void onClick(View v) {
		    		    editor.putString("id", "");
						editor.putString("password", "");
						editor.commit(); // 저장합니다.
						mainmenu_btn.ajou_login = false;
						
						Toast.makeText(setting.this, "로그아웃 되었습니다.", 5000).show();
						
						id.setText("로그아웃 상태입니다.");
			    		password.setText("");
			    		auto.setText("자동 로그인이 설정 되어 있지 않습니다.");
		    	   }
		        });
		} else {
			id.setText("로그아웃 상태입니다.");
    		password.setText("");
    		
    		logout.setOnClickListener(new Button.OnClickListener(){
  	    	   public void onClick(View v) {
  	    		    Toast.makeText(setting.this, "로그인이 되어 있지 않습니다.", 5000).show();
  	    	   }
  	        });
		}
		
    	
    	/*if (auto.isChecked()) {
			editor.putString("id", "");
			editor.putString("password", "");
			editor.commit(); // 저장합니다.
			
			Toast.makeText(setting.this, "자동 로그인이 해제 되었습니다.", 5000).show();
	   } else {
		   Toast.makeText(setting.this, "자동 로그인 설정은 로그아웃 하신 후\n아주포털 로그인에서 하세요.", 5000).show();
	   }*/

        
    }
}
