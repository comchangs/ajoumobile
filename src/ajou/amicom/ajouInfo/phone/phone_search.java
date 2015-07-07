/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.phone;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.*;
import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class phone_search extends Activity {
	Context context;
	String name;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.phone_search);  	
        context = this;
        Bundle extras = getIntent().getExtras();
        name = new String(URLEncoder.encode((String) extras.get("phone")));
        
        new GetDataFromServer().execute(0);
    }
    
	/**
	 * 서버로 부터 다운로드 받아야 하는 데이터의 양이 많은 경우 
	 * 백그라운드 스레드로 데이터를 처리함. UI 스레드 block 방지 -> ANR 방지
	 * 
	 * preExecute, postExecute 에서 메인 UI 의 View 접근 가능.
	 * @author kein
	 */
	class GetDataFromServer extends AsyncTask<Integer, Integer, MemberAdapter>{
		ProgressDialog progressDialog;
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			// 작업 취소되었을 때 행동.
			super.onCancelled();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			// 작업 진행 상황에 따라 프로그레스바 등 바꿀때 사용 
			super.onProgressUpdate(values);
		}

		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// 작업 시작전 처리.
			progressDialog = new ProgressDialog(context);
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("로딩중...");
			progressDialog.show();
			
		}

		@Override
		protected MemberAdapter doInBackground(Integer... params) {
			try{
				// just sleep 
				Thread.sleep(2000);
			}catch(Exception e){}
			
			
			// get datas from server
			String url = "http://dev.jwnc.net/ajoumobile/phone_search.php?name="+name;
			String json = DownloadJson(url); 
			
			Log.e("JSON_DATA",json);
			
			MemberAdapter adapter = null;
			// process data
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray array = jsonObject.optJSONArray("phone");
				int size = array.length();
				
				ArrayList<MemberInfo> m_orders  = new ArrayList<MemberInfo>();
				
				for(int i=0 ; i<size ; i++){
					MemberInfo member = new MemberInfo();
					member.small_type = array.optJSONObject(i).optString("small_type");
					member.name =  array.optJSONObject(i).optString("name");
					member.phone = array.optJSONObject(i).optString("tel");
					member.no = array.optJSONObject(i).optString("no");
					m_orders.add(member);
				}
				
				adapter = new MemberAdapter(context, R.layout.phone_list_item, m_orders);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return adapter;
		}
		
		@Override
		protected void onPostExecute(final MemberAdapter adapter) {
			// 작업 후 처리.
			progressDialog.dismiss();
			
			ListView list = (ListView)findViewById(R.id.listview);
			list.setAdapter(adapter);
			
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
					String no = adapter.getItem(arg2).no;
					Intent intent = new Intent(phone_search.this, phone_View.class);
					intent.putExtra("phonedata", (String)(no.toString()));
					startActivity(intent);
				}
			});
		}
		
	}

//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l, v, position, id);
//		startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + parsedata[position][2].toString())));
//	}
	
	

	class MemberAdapter extends ArrayAdapter<MemberInfo>{
		private ArrayList<MemberInfo> items;
		public MemberAdapter(Context context, int arg1, ArrayList<MemberInfo> objects){
			super(context, arg1, objects);
			this.items = objects;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			View item_view = convertView;
			if(item_view == null){
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				item_view = vi.inflate(R.layout.phone_list_item, null);	
			}
			
			if(items != null){
				((TextView)item_view.findViewById(R.id.small_type)).setText(items.get(position).small_type);
				((TextView)item_view.findViewById(R.id.name)).setText(items.get(position).name);
			}
			return item_view;
		}
	}
	
	class MemberInfo {
		String small_type;
		String name;
		String phone;
		String no;
	}
	
	String DownloadJson(String addr) {
		StringBuilder html = new StringBuilder(); 
		try {
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (conn != null) {
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					for (;;) {
						
						/*
						//json파일의 내용이 용량이 클경우 Stirng 의 허용점인 4096 byte 를 넘어가면 오류발생
				        int bufferSize = 1024 * 4;
				        
				        //char 로 버프 싸이즈 만큼 담기위해 선언
				        char readBuf [] = new char[bufferSize];
				        int resultSize = 0;
				        
				        //파일의 전체 내용 읽어오기
				        while((resultSize = br.read(readBuf))  != -1){
				                if(resultSize == bufferSize){
				                       html.append(readBuf);
				                }else{
				                       for(int i = 0; i < resultSize; i++){
				                            //StringBuilder 에 append
				                            html.append(readBuf[i]);
				                       }
				                }
				        } 
						*/
						String line = br.readLine();
						if (line == null) break;
						html.append(line+'\n'); 
					}
					br.close();
				}
				conn.disconnect();
			}
		} 
		catch (Exception ex) {;}
		return html.toString();
	}

}