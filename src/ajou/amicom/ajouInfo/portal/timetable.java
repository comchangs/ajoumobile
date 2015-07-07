package ajou.amicom.ajouInfo.portal;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

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

public class timetable extends Activity {
	String[] temp;
    int i = 0;
    int j = 0;
	//String personXMLString = "";

    //SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
	//String saved_stdnum = pref.getString("stdnum", "");
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.portal_timetable);  	
/*
        try {
        	 Log.i("jin", "0");
            personXMLString = getXMLFileFromAssets();
            Log.i("jin", "11.");
     } catch (IOException e) {

           // TODO Auto-generated catch block

                  e.printStackTrace();
                  Log.i("jin", "111.");
     }
 */
        /*
    	if (saved_stdnum.equals("")) {
    		Toast.makeText(this, "최초 1회의 로그인 후 시간표 조회가 가능합니다.", 0).show();
    		Intent i = new Intent(this, ajou_login.class);
			startActivity(i);
			finish();
    	}*/
    	
        HttpPostData();

		Button btn_timetable1 = (Button) findViewById(R.id.button1);
		btn_timetable1.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				HttpPostData(1);
			}

		});
		
		Button btn_timetable2 = (Button) findViewById(R.id.button2);
		btn_timetable2.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				HttpPostData(2);
			}

		});
		
		Button btn_timetable3 = (Button) findViewById(R.id.button3);
		btn_timetable3.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				HttpPostData(3);
			}

		});
		
		Button btn_timetable4 = (Button) findViewById(R.id.button4);
		btn_timetable4.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				HttpPostData(4);
			}

		});
	}
	
	public void HttpPostData() {
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
    	
		try {
			temp = null;
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://haksa.ajou.ac.kr/uni/uni/cour/tlsn/findCourPersonalTakingLessonAply.action"); // URL
																											// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] Start");
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
			//http.setRequestProperty("Pragma", "no-cache");
			// http.setRequestProperty("Cookie",
			// "PHAROS_VISITOR=000000000131c7d833155e68ca1e000f; JSESSIONID=xjJsZSjAbTs3qAl0bLmxjMlYukPiCyex3OFGW1vr0CpbcOMTHWNbftYw91Vm0P7C.hakdang_servlet_engine2; ssotoken=zLabtQxo4DivPTbUTfuJ%2F2P7Nzqm5fYw1abwwbgkQpI0VVVj3IbLeTJmJTj83gTrugceRNoBLie0izSmOBgQ9LGgGWgUX%2FyVv1VnuHTkNwUkp1g5e0u9HZvcviD5BnsmByjKUNB79stTXMYvTEQbKg%3D%3D; SSOGlobalLogouturl=get^http://portal.ajou.ac.kr/com/sso/logout.jsp$; PDNM=%C1%A4%B9%AE%C3%A2; PDGR=%C7%D0%BA%CE%BB%FD; PDTEL=0635333519; PDDEC=DS03001003001; PDDE=%BB%EA%BE%F7%C1%A4%BA%B8%BD%C3%BD%BA%C5%DB%B0%F8%C7%D0%C0%FC%B0%F8; PDCN=200621756; PDID=%C1%A4%B9%AE%C3%A2; PDFAX=01071313519; PDSCHOOLREG=%C0%E7%C7%D0; PDREAD=1; PDLEND=1; PDEM=comchangs%40ajou%2Eac%2Ekr; PDGRC=13; PHAROS_VISITOR=000000000131c7c0951958bdca1e000f; JSESSIONID=gHFb1Zg8Kj77onlny2dqJHDnkwkwyv453rg2KijoOYY2gQIAHRp5isDR11zjHQRp.hakdang_servlet_engine1");
			// http.setRequestProperty("Content-Length", "270");
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------
			

			StringBuffer buffer = new StringBuffer();
			
			String stdnum = temp_student_id; // 학번 ** 테스트시 입력 후 하기 바람!!
			String strShtmCd = "";// 학기설정 부분 초기화 U0002001->1학기
			String strYy = "";// 학년도설정 부분 초기화 2011
			
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			
			String curdate = dateFormat.format(calendar.getTime()).toString();
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "CurDate: "+curdate);
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "Month String: "+ curdate.substring(4, 6));
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "Month Int: "+ Integer.parseInt(curdate.substring(4, 6)));
			
			strYy = curdate.substring(0, 4);
			TextView hakgi = (TextView) findViewById(R.id.hakgi);
			if (Integer.parseInt(curdate.substring(4, 6)) < 3) {
				strYy = Integer.toString(Integer.parseInt(curdate.substring(0, 4)) - 1);
				strShtmCd = "U0002004";
				hakgi.setText(strYy + "학년도 동계학기");
			} else if (Integer.parseInt(curdate.substring(4, 6)) < 7) {
				strShtmCd = "U0002001";
				hakgi.setText(strYy + "학년도 1학기");
			} else if (Integer.parseInt(curdate.substring(4, 6)) < 8) {
				strShtmCd = "U0002002";
				hakgi.setText(strYy + "학년도 하계학기");
			} else if (Integer.parseInt(curdate.substring(4, 6)) <= 12) {
				strShtmCd = "U0002003";
				hakgi.setText(strYy + "학년도 2학기");
			}
			
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] stdnum="+stdnum);
			String parameter = null;
			parameter = new String(
					"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
							+ "<root>\n<params>\n"
							+ "<param id=\"strYy\" type=\"STRING\">"
							+strYy
							+"</param>\n"
							+ "<param id=\"strShtmCd\" type=\"STRING\">"
							+strShtmCd
							+"</param>\n"
							+ "<param id=\"strStdNo\" type=\"STRING\">"
							+ stdnum
							+ "</param>\n"
							+ "<param id=\"admin\" type=\"STRING\">admin</param>\n"
							+ "</params>\n" + "</root>");

			buffer.append(parameter);

			OutputStreamWriter outStream = new OutputStreamWriter(
					http.getOutputStream(), "UTF-8");
			PrintWriter writer = new PrintWriter(outStream);
			writer.write(buffer.toString());
			writer.flush();
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 바디생성");
			// --------------------------
			// 서버에서 전송받기
			// --------------------------

			InputStreamReader tmp = new InputStreamReader(
					http.getInputStream(), "UTF-8");
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 1");
			BufferedReader reader = new BufferedReader(tmp);
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 2");
			StringBuilder builder = new StringBuilder();
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 3");
			String str;
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 4");
			while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄 것이므로
														// 라인단위로 읽는다
				
				builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자 추가 }
			}
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 전송받음");

			String myResult = builder.toString(); // 전송결과를 전역 변수에 저장
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 수신결과" + myResult);
			PullParserFromXML(myResult);
			Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			Toast.makeText(this, "IOException", 0).show();
		}
		
		TextView tv1 = (TextView) findViewById(R.id.sub1);
		tv1.setText(temp[0].toString());

		TextView tv2 = (TextView) findViewById(R.id.sub2);
		tv2.setText(temp[1].toString());

		TextView tv3 = (TextView) findViewById(R.id.sub3);
		tv3.setText(temp[2].toString());

		TextView tv4 = (TextView) findViewById(R.id.sub4);
		tv4.setText(temp[3].toString());

		TextView tv5 = (TextView) findViewById(R.id.sub5);
		tv5.setText(temp[4].toString());

		TextView tv6 = (TextView) findViewById(R.id.sub6);
		tv6.setText(temp[5].toString());

		TextView tv7 = (TextView) findViewById(R.id.sub7);
		tv7.setText(temp[6].toString());

		TextView tv8 = (TextView) findViewById(R.id.sub8);
		tv8.setText(temp[7].toString());

		TextView tv9 = (TextView) findViewById(R.id.sub9);
		tv9.setText(temp[8].toString());
	} // HttpPostData
	
	
	public void HttpPostData(int hakgi) {
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
		try {
			temp = null;
			
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://haksa.ajou.ac.kr/uni/uni/cour/tlsn/findCourPersonalTakingLessonAply.action"); // URL
																											// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] Start");
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
			//http.setRequestProperty("Pragma", "no-cache");
			// http.setRequestProperty("Cookie",
			// "PHAROS_VISITOR=000000000131c7d833155e68ca1e000f; JSESSIONID=xjJsZSjAbTs3qAl0bLmxjMlYukPiCyex3OFGW1vr0CpbcOMTHWNbftYw91Vm0P7C.hakdang_servlet_engine2; ssotoken=zLabtQxo4DivPTbUTfuJ%2F2P7Nzqm5fYw1abwwbgkQpI0VVVj3IbLeTJmJTj83gTrugceRNoBLie0izSmOBgQ9LGgGWgUX%2FyVv1VnuHTkNwUkp1g5e0u9HZvcviD5BnsmByjKUNB79stTXMYvTEQbKg%3D%3D; SSOGlobalLogouturl=get^http://portal.ajou.ac.kr/com/sso/logout.jsp$; PDNM=%C1%A4%B9%AE%C3%A2; PDGR=%C7%D0%BA%CE%BB%FD; PDTEL=0635333519; PDDEC=DS03001003001; PDDE=%BB%EA%BE%F7%C1%A4%BA%B8%BD%C3%BD%BA%C5%DB%B0%F8%C7%D0%C0%FC%B0%F8; PDCN=200621756; PDID=%C1%A4%B9%AE%C3%A2; PDFAX=01071313519; PDSCHOOLREG=%C0%E7%C7%D0; PDREAD=1; PDLEND=1; PDEM=comchangs%40ajou%2Eac%2Ekr; PDGRC=13; PHAROS_VISITOR=000000000131c7c0951958bdca1e000f; JSESSIONID=gHFb1Zg8Kj77onlny2dqJHDnkwkwyv453rg2KijoOYY2gQIAHRp5isDR11zjHQRp.hakdang_servlet_engine1");
			// http.setRequestProperty("Content-Length", "270");
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
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
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "CurDate: "+curdate);
			
			TextView hakgi_tv = (TextView) findViewById(R.id.hakgi);
			strYy = curdate.substring(0, 4);
			if (hakgi == 4) {
				strYy = Integer.toString(Integer.parseInt(curdate.substring(0, 4)) - 1);
				strShtmCd = "U0002004";
				hakgi_tv.setText(strYy + "학년도 동계학기");
			} else if (hakgi == 1) {
				strShtmCd = "U0002001";
				hakgi_tv.setText(strYy + "학년도 1학기");
			} else if (hakgi == 2) {
				strShtmCd = "U0002002";
				hakgi_tv.setText(strYy + "학년도 하계학기");
			} else if (hakgi == 3) {
				strShtmCd = "U0002003";
				hakgi_tv.setText(strYy + "학년도 2학기");
			}
			
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] stdnum="+stdnum);
			String parameter = null;
			parameter = new String(
					"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
							+ "<root>\n<params>\n"
							+ "<param id=\"strYy\" type=\"STRING\">"
							+strYy
							+"</param>\n"
							+ "<param id=\"strShtmCd\" type=\"STRING\">"
							+strShtmCd
							+"</param>\n"
							+ "<param id=\"strStdNo\" type=\"STRING\">"
							+ stdnum
							+ "</param>\n"
							+ "<param id=\"admin\" type=\"STRING\">admin</param>\n"
							+ "</params>\n" + "</root>");

			buffer.append(parameter);

			OutputStreamWriter outStream = new OutputStreamWriter(
					http.getOutputStream(), "UTF-8");
			PrintWriter writer = new PrintWriter(outStream);
			writer.write(buffer.toString());
			writer.flush();
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 바디생성");
			// --------------------------
			// 서버에서 전송받기
			// --------------------------

			InputStreamReader tmp = new InputStreamReader(
					http.getInputStream(), "UTF-8");
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 1");
			BufferedReader reader = new BufferedReader(tmp);
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 2");
			StringBuilder builder = new StringBuilder();
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 3");
			String str;
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 4");
			while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄 것이므로
														// 라인단위로 읽는다
				
				builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자 추가 }
			}
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 전송받음");

			String myResult = builder.toString(); // 전송결과를 전역 변수에 저장
			if(intro.DEBUG) Log.i(intro.LOG_TAG, "[HTTP POST] 수신결과" + myResult);
			PullParserFromXML(myResult);
			//Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			Toast.makeText(this, "IOException", 0).show();
		}
		
		TextView tv1 = (TextView) findViewById(R.id.sub1);
		tv1.setText(temp[0].toString());

		TextView tv2 = (TextView) findViewById(R.id.sub2);
		tv2.setText(temp[1].toString());

		TextView tv3 = (TextView) findViewById(R.id.sub3);
		tv3.setText(temp[2].toString());

		TextView tv4 = (TextView) findViewById(R.id.sub4);
		tv4.setText(temp[3].toString());

		TextView tv5 = (TextView) findViewById(R.id.sub5);
		tv5.setText(temp[4].toString());

		TextView tv6 = (TextView) findViewById(R.id.sub6);
		tv6.setText(temp[5].toString());

		TextView tv7 = (TextView) findViewById(R.id.sub7);
		tv7.setText(temp[6].toString());

		TextView tv8 = (TextView) findViewById(R.id.sub8);
		tv8.setText(temp[7].toString());

		TextView tv9 = (TextView) findViewById(R.id.sub9);
		tv9.setText(temp[8].toString());
	} // HttpPostData

	
	
/*
	private String getXMLFileFromAssets() throws IOException {
        AssetManager assetManager = getResources().getAssets();
        AssetInputStream ais = (AssetInputStream)assetManager.open("time.xml");      
        BufferedReader br = new BufferedReader(new InputStreamReader(ais));

        String line;
        StringBuilder data = new StringBuilder();
        while((line=br.readLine()) != null)
               data.append(line);
        Log.i("jin", data.toString());
        return data.toString();

  }
*/
    
	private void PullParserFromXML(String data) {
		temp = new String[9];
		i = 0;
	    j = 0;
		
		for (int i=0; i<9; i++) {
			temp[i] = " ";
		}
		if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse]");
		boolean ltRoomNm = false;	//강의실
    	boolean ltTmNm = false;		//
    	boolean sbjtKorNm = false;
    	boolean modDttm = false;
		
		try{
        	

        	XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
        	XmlPullParser parser = parserCreator.newPullParser();
        	String tag;
        	parser.setInput( new StringReader(data));

        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] Start");
        	int parserEvent = parser.getEventType();
        	
        	
        
        	
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
        			
        			if (ltRoomNm) {
        				temp[i] = "("+parser.getText()+")";
        				        				
        			}
        			if (ltTmNm) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] ltTmNm = " +  parser.getText() );
        				temp[i] += "\n"+ parser.getText();       
        				
        			}
        			if (modDttm) {
        				//j=1;
        			}
        			
        			if (sbjtKorNm) {
        				if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] sbjkKorNm = " +  parser.getText() );  
        				temp[i] = parser.getText()+" "+temp[i];
        				//if(j!=1)
        					i++;
        				//else
        				//	j=0;
        			}
        			break;
        			
        		case XmlPullParser.END_TAG:
        			tag = parser.getName();
        			if (tag.compareTo("ltRoomNm") == 0) {
        				ltRoomNm = false;
        			}
        			if (tag.compareTo("ltTmNm") == 0) {
        				ltTmNm = false;
        			}
        			if (tag.compareTo("modDttm") == 0) {
        				modDttm = false;
        			}
        			if (tag.compareTo("sbjtKorNm") == 0) {
        				sbjtKorNm = false;
        			}
        			break;	
        			
        		case XmlPullParser.START_TAG:
        			tag = parser.getName();

        			if (tag.compareTo("ltRoomNm") == 0) {
        				ltRoomNm = true;
        			}
        			if (tag.compareTo("ltTmNm") == 0) {
        				ltTmNm = true;
        			}
        			if (tag.compareTo("modDttm") == 0) {
        				modDttm = true;
        			}
        			if (tag.compareTo("sbjtKorNm") == 0) {
        				sbjtKorNm = true;
        			}
        			break;




        		}
        		parserEvent = parser.next();
        	}
        	if(intro.DEBUG) Log.i(intro.LOG_TAG, "[Parse] End");
        }catch( Exception e ){
        	if(intro.DEBUG) Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
        }
	}
	
}
