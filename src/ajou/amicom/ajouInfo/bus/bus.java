/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.bus;

import java.util.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class bus extends ListActivity {
	
	ArrayAdapter <String> mAdapter;
	
	static String busdata[][] = null;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bus);

        busdata = new String[12][2];
        
        busdata[0][0] = "03117 효성초등학교";
    	busdata[0][1] = "03117";
    	
    	busdata[1][0] = "03119 효성초등학교";
    	busdata[1][1] = "03119";
    	
    	busdata[2][0] = "03124 유신고등학교";
    	busdata[2][1] = "03124";
    	
    	busdata[3][0] = "03125 유신고등학교";
    	busdata[3][1] = "03125";
    	
    	busdata[4][0] = "04039 아주대병원";
    	busdata[4][1] = "04039";
    	
    	busdata[5][0] = "04041 아주대병원";
    	busdata[5][1] = "04041";
    	
    	busdata[6][0] = "04237 아주대정문";
    	busdata[6][1] = "04237";
    	
    	busdata[7][0] = "03126 아주대정문";
    	busdata[7][1] = "03126";
    	
    	busdata[8][0] = "04238 아주대입구";
    	busdata[8][1] = "04238";
    	
    	busdata[9][0] = "03129 아주대입구";
    	busdata[9][1] = "03129";
    	
    	busdata[10][0] = "03132 아주대입구삼거리";
    	busdata[10][1] = "03132";
    	
    	busdata[11][0] = "04023 아주대입구삼거리";
    	busdata[11][1] = "04023";
    	
    	
        mAdapter = new ArrayAdapter<String>(
        		getApplicationContext(), 
        		android.R.layout.simple_list_item_1, 
        		new ArrayList<String>());
        
        setListAdapter(mAdapter);
        
        addStringData();
        
        Toast.makeText(this, "정류소 번호를 확인하신 후 보시기 바랍니다.",1000).show();
	}
	
	private void addStringData(){
     	for(int z= 0; z < 12; z++) {
	    	mAdapter.add(busdata[z][0].toString());
    	}
    }
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(bus.this, busView.class);
		intent.putExtra("busdata", busdata[position][1]);
		startActivity(intent);
	}
}
