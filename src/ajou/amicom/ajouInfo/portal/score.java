package ajou.amicom.ajouInfo.portal;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import org.json.*;
import org.xmlpull.v1.*;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.main.*;
import ajou.amicom.ajouInfo.portal.*;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.content.res.AssetManager.AssetInputStream;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class score extends Activity {
	Context context;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.portal_score);  	
        context = this;
       
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
		String myResult;
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
				//Thread.sleep(5000);
			}catch(Exception e){}
			
			// get datas from server
			MemberAdapter adapter = null;
			if (intro.DEBUG) Log.i(intro.LOG_TAG, "Start DownloadXML");
			adapter = PullParserFromXML(DownloadXML());
			if (intro.DEBUG) Log.i(intro.LOG_TAG, "End DownloadXML");
			
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
					//String no = adapter.getItem(arg2).no;
					//Intent intent = new Intent(haksaplan.this, phone_View.class);
					//intent.putExtra("phonedata", (String)(no.toString()));
					//startActivity(intent);
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
				item_view = vi.inflate(R.layout.score_list_item, null);	
			}
			
			if(items != null){
				((TextView)item_view.findViewById(R.id.year)).setText(items.get(position).year);
				((TextView)item_view.findViewById(R.id.hakgi)).setText(items.get(position).hakgi);
				((TextView)item_view.findViewById(R.id.subject)).setText(items.get(position).subject);
				((TextView)item_view.findViewById(R.id.grade)).setText(items.get(position).grade);
			}
			return item_view;
		}
	}
	
	class MemberInfo {
		String hakgi;
		String year;
		String subject;
		String grade;
	}
	
	String DownloadXML() {
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
		//MemberAdapter adapter = null;
		String myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://haksa.ajou.ac.kr/uni/uni/scor/inqy/findScorStdSngjInq.action"); // URL
																										// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start");
			// --------------------------
			// 전송 모드 설정 - 기본적인 설정이다
			// --------------------------
			http.setDefaultUseCaches(false);
			http.setDoInput(true); // 서버에서 읽기 모드 지정
			http.setDoOutput(true); // 서버로 쓰기 모드 지정
			http.setRequestMethod("POST"); // 전송 방식은 POST

			// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
			http.setRequestProperty("Accept", "*/*");
			http.setRequestProperty("Accept-Language", "ko-KR");
			http.setRequestProperty("Referer",
					"http://haksa.ajou.ac.kr/aimsDdx1.5.2.swf");
			http.setRequestProperty("x-flash-version", "10,3,181,26");
			http.setRequestProperty("content-type", "text/xml;charset=utf-8");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; MASN)");
			http.setRequestProperty("Host", "haksa.ajou.ac.kr");
			// http.setRequestProperty("Pragma", "no-cache");
			// http.setRequestProperty("Cookie",
			// "PHAROS_VISITOR=000000000131c7d833155e68ca1e000f; JSESSIONID=xjJsZSjAbTs3qAl0bLmxjMlYukPiCyex3OFGW1vr0CpbcOMTHWNbftYw91Vm0P7C.hakdang_servlet_engine2; ssotoken=zLabtQxo4DivPTbUTfuJ%2F2P7Nzqm5fYw1abwwbgkQpI0VVVj3IbLeTJmJTj83gTrugceRNoBLie0izSmOBgQ9LGgGWgUX%2FyVv1VnuHTkNwUkp1g5e0u9HZvcviD5BnsmByjKUNB79stTXMYvTEQbKg%3D%3D; SSOGlobalLogouturl=get^http://portal.ajou.ac.kr/com/sso/logout.jsp$; PDNM=%C1%A4%B9%AE%C3%A2; PDGR=%C7%D0%BA%CE%BB%FD; PDTEL=0635333519; PDDEC=DS03001003001; PDDE=%BB%EA%BE%F7%C1%A4%BA%B8%BD%C3%BD%BA%C5%DB%B0%F8%C7%D0%C0%FC%B0%F8; PDCN=200621756; PDID=%C1%A4%B9%AE%C3%A2; PDFAX=01071313519; PDSCHOOLREG=%C0%E7%C7%D0; PDREAD=1; PDLEND=1; PDEM=comchangs%40ajou%2Eac%2Ekr; PDGRC=13; PHAROS_VISITOR=000000000131c7c0951958bdca1e000f; JSESSIONID=gHFb1Zg8Kj77onlny2dqJHDnkwkwyv453rg2KijoOYY2gQIAHRp5isDR11zjHQRp.hakdang_servlet_engine1");
			// http.setRequestProperty("Content-Length", "270");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------

			StringBuffer buffer = new StringBuffer();

			String stdnum = temp_student_id; // 학번 ** 테스트시 입력 후 하기 바람!!
			String strShtmCd = "";// 학기설정 부분 초기화 U0002001->1학기
			String strYy = "";// 학년도설정 부분 초기화 2011

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMddHHmmss");

			String curdate = dateFormat.format(calendar.getTime()).toString();
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "CurDate: " + curdate);

			strYy = curdate.substring(0, 4);

			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] stdnum=" + stdnum);
			String parameter = null;
			parameter = new String(
					"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
							+ "<root>\n"
							+ "<params>\n"
							+ "<param id=\"stdNo\" type=\"STRING\">" + stdnum
							+ "</param>\n" + "</params>\n" + "</root>");

			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 파라미터 = \n"+parameter);
			buffer.append(parameter);

			OutputStreamWriter outStream = new OutputStreamWriter(
					http.getOutputStream(), "UTF-8");
			PrintWriter writer = new PrintWriter(outStream);
			writer.write(buffer.toString());
			writer.flush();
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 바디생성");
			// --------------------------
			// 서버에서 전송받기
			// --------------------------

			InputStreamReader tmp = new InputStreamReader(
					http.getInputStream(), "UTF-8");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 1");
			BufferedReader reader = new BufferedReader(tmp);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 2");
			StringBuilder builder = new StringBuilder();
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 3");
			String str;
			if (intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 4");
			while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄 것이므로
														// 라인단위로 읽는다

				builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자 추가 }
			}
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 전송받음");

			myResult = builder.toString(); // 전송결과를 전역 변수에 저장
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 수신결과" + myResult);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 수신결과" + myResult.length());
			//adapter = PullParserFromXML(myResult);
			//Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			//Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			//Toast.makeText(this, "IOException", 0).show();
		}
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료");
		//return adapter;
		return myResult.toString();
	}

	private MemberAdapter PullParserFromXML(String data) {
		MemberAdapter adapter = null;
		ArrayList<MemberInfo> m_orders  = new ArrayList<MemberInfo>();
		
		if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse]");
		boolean orgShtmCd = false;	// 학기
    	boolean orgYy = false;	// 연도
    	boolean sbjtNm = false;	// 과목명
    	boolean sngjGrdFg = false;	// 성적
		
		try{
        	

        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();
        	String tag;
        	parser.setInput( new StringReader(data));

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	
        	
        	MemberInfo member = new MemberInfo();
        	while (parserEvent != XmlPullParser.END_DOCUMENT ){
        		switch(parserEvent){
        		  case XmlPullParser.START_DOCUMENT:            // 문서의 시작
        			  if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] 문서시작");
                      break;


                case XmlPullParser.END_DOCUMENT: 
                	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] 문서끝");// 문서의 끝
                      break;

                      
        		case XmlPullParser.TEXT:
        			tag = parser.getName();
        			
        			if (orgShtmCd) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] orgShtmCd = " +  parser.getText() );
        				if(parser.getText().equals("U0002001"))
        					member.hakgi = "1학기";
        				else if(parser.getText().equals("U0002002"))
        					member.hakgi = "하계학기";
        				else if(parser.getText().equals("U0002003"))
        					member.hakgi = "2학기";
        				else if(parser.getText().equals("U0002004"))
        					member.hakgi = "동계학기";
        			}	
        			if (orgYy) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] orgYy = " +  parser.getText() );  
        				member.year =  parser.getText()+"년";
        			}
        			if (sbjtNm) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] sbjtNm = " +  parser.getText() );
        				member.subject = parser.getText();
        			}	
        			if (sngjGrdFg) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] sngjGrdFg = " +  parser.getText() );  
        				member.grade =  parser.getText();
        			}
        			break;
        			
        		case XmlPullParser.END_TAG:
        			tag = parser.getName();
        			if (tag.compareTo("orgShtmCd") == 0) {
        				orgShtmCd = false;
        			}
        			if (tag.compareTo("orgYy") == 0) {
        				//if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] KorScheNm End"); 
        				orgYy = false;
        			}
        			if (tag.compareTo("sbjtNm") == 0) {
        				sbjtNm = false;
        			}
        			if (tag.compareTo("sngjGrdFg") == 0) {
        				//if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] KorScheNm End"); 
        				sngjGrdFg = false;
        				m_orders.add(member);
        				member = new MemberInfo();
        			}
        			break;	
        			
        		case XmlPullParser.START_TAG:
        			tag = parser.getName();

        			if (tag.compareTo("orgShtmCd") == 0) {
        				orgShtmCd = true;
        			}
        			if (tag.compareTo("orgYy") == 0) {
        				//if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] KorScheNm Start"); 
        				orgYy = true;
        			}
        			if (tag.compareTo("sbjtNm") == 0) {
        				sbjtNm = true;
        			}
        			if (tag.compareTo("sngjGrdFg") == 0) {
        				//if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] KorScheNm Start"); 
        				sngjGrdFg = true;
        			}
        			break;
        		}
        		parserEvent = parser.next();
        	}
        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] End");
        	adapter = new MemberAdapter(context, R.layout.score_list_item, m_orders);
        }catch( Exception e ){
        	if(intro.DEBUG) Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
        }
        return adapter;
	}
	
}
