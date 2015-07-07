/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.food;

import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class foodCategory extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.food_category);
        
        Button btn = (Button)findViewById(R.id.button1);
		btn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				EditText edit = (EditText)findViewById(R.id.editText1);
				String str = edit.getText().toString();
				Intent intent = new Intent(foodCategory.this, food_search.class);
	    		intent.putExtra("restaurant_name", str);
	    		startActivity(intent);
			}
		});
        
        ImageButton launch = (ImageButton)findViewById(R.id.imageButton1);
        launch.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "한식");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch2 = (ImageButton)findViewById(R.id.imageButton2);
        launch2.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "중식");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch3 = (ImageButton)findViewById(R.id.imageButton3);
        launch3.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "일식");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch4 = (ImageButton)findViewById(R.id.imageButton4);
        launch4.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "양식");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch5 = (ImageButton)findViewById(R.id.imageButton5);
        launch5.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "분식");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch6 = (ImageButton)findViewById(R.id.imageButton6);
        launch6.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "치킨");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch7 = (ImageButton)findViewById(R.id.imageButton7);
        launch7.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "피자");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch8 = (ImageButton)findViewById(R.id.imageButton8);
        launch8.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "고기류");
    		startActivity(intent);
    	   }
         
        });
        
        ImageButton launch9 = (ImageButton)findViewById(R.id.imageButton9);
        launch9.setOnClickListener(new Button.OnClickListener(){

    	   public void onClick(View v) {
    	    Intent intent = new Intent(foodCategory.this, food.class);
    		intent.putExtra("category", "기타");
    		startActivity(intent);
    	   }
         
        });
        
	}
}
