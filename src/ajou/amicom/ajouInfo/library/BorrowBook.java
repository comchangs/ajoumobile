/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

import java.util.Vector;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;


public class BorrowBook extends Activity{
	
	private BorrowBookControl borrowBookControl = null;	
	String[] bookExtendURL = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.library_borrowbook);
		
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
		
		String userid = temp_id; // 홈페이지 id
		String password = temp_password; // 홈페이지 password
		
		Vector<BorrowBookVO> bookVector = null;
		
		try {
			borrowBookControl = new BorrowBookControl(userid, password);
		} catch (Exception e) {
			if(intro.DEBUG) Log.e(intro.LOG_TAG, "Error in BorrowBookControl");
		}
		
		if(borrowBookControl.isHasBorrowBook()){
		
			bookVector = borrowBookControl.getBorrowBookVOVector();
			
			final int size = bookVector.size();
			String[] book = new String[size];
			//final String[] startDate = new String[size];
			//final String[] endDate = new String[size];
			bookExtendURL = new String[size];
			StringBuffer stringBuffer = new StringBuffer();
			
			for(int i=0;i<size;++i){
				BorrowBookVO borrowBookVO = bookVector.get(i);
				book[i] = borrowBookVO.getBookName()+ "\n" + "반납예정일: " + borrowBookVO.getEndDate()
				+ "\n" + "연체일: " + borrowBookVO.getOverDueDate() + " / 연체료: " + borrowBookVO.getOverDueFee();
				//startDate[i] = borrowBookVO.getStartDate();
				//endDate[i] = borrowBookVO.getEndDate();
				bookExtendURL[i] = borrowBookVO.getExtendURL();
			}
			
			ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.main_list_item, book);
			ListView bookList = (ListView)findViewById(R.id.borrowbook_list1);
	        bookList.setAdapter(adapt);
	        bookList.setTextFilterEnabled(true);
	        registerForContextMenu(bookList);
	        
	        bookList.setOnItemClickListener(
	        		new AdapterView.OnItemClickListener() {
	        			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
	        				TextView textView = (TextView)view;
	        				for(int i=0;i<size;++i){
	        					if(position == i){
	        						final int cur = i;
	        						//textView.setText(endDate[i]);
	        						AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
	        								BorrowBook.this);
	        						alert_internet_status.setTitle("도서 연장");
	        						alert_internet_status
	        								.setMessage("도서 연장을 시도합니다.");
	        						alert_internet_status.setPositiveButton("연장",
	        								new DialogInterface.OnClickListener() {
	        									public void onClick(DialogInterface dialog, int which) {
	        										dialog.dismiss(); // 닫기
	        										try {
	        											String str = borrowBookControl.bookExtend(bookExtendURL[cur]);
	        											Toast.makeText(BorrowBook.this, str, 2000).show();
	        											Intent intent = new Intent(BorrowBook.this, BorrowBook.class);
	        											startActivity(intent);
	        											finish();
	        										} catch (Exception e) {
	        											// TODO Auto-generated catch block
	        											e.printStackTrace();
	        										}
	        									}
	        								});
	        						alert_internet_status.setNegativeButton("취소",
	        								new DialogInterface.OnClickListener() {
	        									public void onClick(DialogInterface dialog, int which) {
	        										dialog.dismiss(); // 닫기
	        									}
	        								});
	        						alert_internet_status.show();
	        					}
	        				}
	        			}
	        		});
		}
		else{
			String[] book = new String[1];
			book[0] = "대출자료가 없습니다";
			ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.main_list_item, book);
			ListView bookList = (ListView)findViewById(R.id.borrowbook_list1);
	        bookList.setAdapter(adapt);
	        bookList.setTextFilterEnabled(true);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.addSubMenu(0, 0, 0, "연장");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch(item.getItemId()){
		case 0:
			
		default:
			return super.onContextItemSelected(item);	
		}
	}
}
