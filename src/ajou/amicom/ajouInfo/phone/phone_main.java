/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.phone;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.food.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class phone_main extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone_main);
        
        Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				EditText edit = (EditText)findViewById(R.id.editText1);
				String str = edit.getText().toString();
				Intent intent = new Intent(phone_main.this, phone_search.class);
	    		intent.putExtra("phone", str);
	    		startActivity(intent);
			}
		});
		
        
        ImageButton launch7 = (ImageButton)findViewById(R.id.imageButton1);
        launch7.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(phone_main.this, phone.class);
    	    intent.putExtra("phone_page", (String)("1"));
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch8 = (ImageButton)findViewById(R.id.imageButton2);
        launch8.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(phone_main.this, phone.class);
    	    intent.putExtra("phone_page", (String)("2"));
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch9 = (ImageButton)findViewById(R.id.imageButton3);
        launch9.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(phone_main.this, phone.class);
    	    intent.putExtra("phone_page", (String)("3"));
    		startActivity(intent);
    	   }
         
        });
        
   	}
}
