/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import java.util.*;
import android.app.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;


public class ClassAttendance extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.portal_class_atendance);
		
		if(intro.DEBUG) Log.i(intro.LOG_TAG, "atendance on");
		
		ElectronicAttendanceControl eAttendanceControl = new ElectronicAttendanceControl();
		Vector<ElectronicAttendanceStatusVO> eVector = new Vector<ElectronicAttendanceStatusVO>();
		Bundle extras = getIntent().getExtras();
		
		String lec_year = (String) extras.get("lec_year");
		String lec_no = (String) extras.get("lec_no");
		String lec_term = (String) extras.get("lec_term");
		String lec_division_code = (String) extras.get("lec_division_code");
		/*String lec_use_pous = (String) extras.get("lec_use_opus");
		String show = (String) extras.get("show");*/
		String cookie = (String) extras.get("cookie");
		if(intro.DEBUG) Log.i(intro.LOG_TAG, lec_year+" "+lec_no+" "+lec_term+" "+lec_division_code);
		eAttendanceControl.setCookie(cookie);
		try {
			eVector = eAttendanceControl.getStatus(lec_year, lec_term, lec_no, lec_division_code);
		} catch (Exception e) {}
		String[] text = new String[eVector.size()];
		for(int i=0;i<eVector.size();++i){
			ElectronicAttendanceStatusVO eVO = eVector.get(i);
			text[i] = eVO.getDate() +" "+ eVO.getStatus();
		}
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.main_list_item, text);
	    ListView subMenu = (ListView)findViewById(R.id.classattendance_list);
	    subMenu.setAdapter(adapt);
	    subMenu.setTextFilterEnabled(true);
	}
}

