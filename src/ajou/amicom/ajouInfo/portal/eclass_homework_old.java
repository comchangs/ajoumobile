package ajou.amicom.ajouInfo.portal;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

import net.htmlparser.jericho.*;

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

public class eclass_homework_old extends Activity {
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eclass_homework);
		context = this;

		new GetDataFromServer().execute(0);
	}

	/**
	 * 서버로 부터 다운로드 받아야 하는 데이터의 양이 많은 경우 백그라운드 스레드로 데이터를 처리함. UI 스레드 block 방지 ->
	 * ANR 방지
	 * 
	 * preExecute, postExecute 에서 메인 UI 의 View 접근 가능.
	 * 
	 * @author kein
	 */
	class GetDataFromServer extends AsyncTask<Integer, Integer, MemberAdapter> {
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
			try {
				// just sleep
				//Thread.sleep(5000);
			} catch (Exception e) {
			}

			// get datas from server
			MemberAdapter adapter = null;
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "Start DownloadXML");
			adapter = PullParserFromXML(DownloadXML());
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "End DownloadXML");

			return adapter;
		}

		@Override
		protected void onPostExecute(final MemberAdapter adapter) {
			// 작업 후 처리.
			progressDialog.dismiss();

			ListView list = (ListView) findViewById(R.id.eclass_notice_listview);
			list.setAdapter(adapter);

			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// String no = adapter.getItem(arg2).no;
					// Intent intent = new Intent(haksaplan.this,
					// phone_View.class);
					// intent.putExtra("phonedata", (String)(no.toString()));
					// startActivity(intent);
					
					
					AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
							eclass_homework_old.this);
					alert_internet_status.setTitle("이클래스 과제");
					//alert_internet_status.setTitle(adapter.getItem(arg2).title);
					
					Source source = new Source(adapter.getItem(arg2).noteCont);
					source.fullSequentialParse();
					String str = source.getRenderer().setIncludeHyperlinkURLs(false).toString();

					alert_internet_status
							.setMessage(adapter.getItem(arg2).title + "\n" + str);
					alert_internet_status.setPositiveButton("닫기",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss(); // 닫기
								}
							});
					alert_internet_status.show();
					
				}
			});

		}

	}

	class MemberAdapter extends ArrayAdapter<MemberInfo> {
		private ArrayList<MemberInfo> items;

		public MemberAdapter(Context context, int arg1,
				ArrayList<MemberInfo> objects) {
			super(context, arg1, objects);
			this.items = objects;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View item_view = convertView;
			if (item_view == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				item_view = vi.inflate(R.layout.eclass_notice_list_item, null);
			}

			if (items != null) {
				((TextView) item_view.findViewById(R.id.title))
						.setText(items.get(position).title);
				((TextView) item_view.findViewById(R.id.regiDttm)).setText(items
						.get(position).regiDttm);
			}
			return item_view;
		}
	}

	class MemberInfo {
		String title;
		String noteCont;
		String regiDttm;
	}

	String DownloadXML() {
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
		// MemberAdapter adapter = null;
		String myResult = null;
		String ssotoken = null;
		String jsessionid = null;
		try {
			/*
			 * 아주대 통합 로그인 과정1
			 */
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://portal.ajou.ac.kr/j_aims_security_check.action"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 아주대 통합 로그인 과정1");
			// --------------------------
			// 전송 모드 설정 - 기본적인 설정이다
			// --------------------------
			http.setDefaultUseCaches(false);
			http.setDoInput(true); // 서버에서 읽기 모드 지정
			http.setDoOutput(true); // 서버로 쓰기 모드 지정
			http.setRequestMethod("POST"); // 전송 방식은 POST

			// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
			http.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
			http.setRequestProperty("Accept-Language", "ko-KR");
			http.setRequestProperty("Referer",
					"http://portal.ajou.ac.kr/public/portal/portlet/UserLoginPre.jsp");
			http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; MASN)");
			http.setRequestProperty("Host", "portal.ajou.ac.kr");
			 http.setRequestProperty("Pragma", "no-cache");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------

			StringBuffer buffer = new StringBuffer();
			String parameter = null;
			parameter = new String(
					"signed_data=&LOGIN_RNG=shM8oqJ9uFgjpUIdgKVnNA%3D%3D&isPki=noPKI&j_username="+temp_id+"&j_password="+temp_password);

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
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 4");
			Map<String, List<String>> headers = http.getHeaderFields();
			List<String> value = headers.get("set-cookie");
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(0));
				Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(1));
				Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(2));
				Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(3));
				//Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(4));
				//Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(5));
				//Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(6));
				//Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(7));
				//Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(8));
				
			}
			ssotoken = value.get(1).substring(0, value.get(0).indexOf(";"));
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "[HTTP POST] ssotoken = " + ssotoken);
			}
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
			// adapter = PullParserFromXML(myResult);
			// Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 아주대 통합 로그인 과정1");

		
		
		
		
		
		
		/*
		 * 이클래스 세션 획득 과정2
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://eclass.ajou.ac.kr:8090/uSosWeb/x/portal_link_work.jsp"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 이클래스 세션 획득 과정2");
			// --------------------------
			// 전송 모드 설정 - 기본적인 설정이다
			// --------------------------
			http.setDefaultUseCaches(false);
			http.setDoInput(true); // 서버에서 읽기 모드 지정
			http.setDoOutput(true); // 서버로 쓰기 모드 지정
			http.setRequestMethod("POST"); // 전송 방식은 POST

			// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
			http.setRequestProperty("Accept", "text/html, application/xhtml+xml, */*");
			http.setRequestProperty("Accept-Language", "ko-KR");
			http.setRequestProperty("Referer",
					"http://portal.ajou.ac.kr/portal/findPortletByRole.action");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; MASN)");
			http.setRequestProperty("Host", "eclass.ajou.ac.kr:8090");
			// http.setRequestProperty("Pragma", "no-cache");
			http.setRequestProperty("Cookie", ssotoken);
			// http.setRequestProperty("Content-Length", "270");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------

			StringBuffer buffer = new StringBuffer();
			String parameter = null;
			parameter = new String(
					"userid="+temp_id+"&plocale=ko_KR&purl=http://portal.ajou.ac.kr/public/portal/portlet/redirectPortlet.jsp&prole=SS0001&userno="+temp_student_id);

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
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 4");
			Map<String, List<String>> headers = http.getHeaderFields();
			List<String> value = headers.get("set-cookie");
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(0));
			}
			jsessionid = value.get(0).substring(0, value.get(0).indexOf(";"));
			if (intro.DEBUG) {
				Log.i(intro.LOG_TAG, "[HTTP POST] ssotoken = " + jsessionid);
			}
			
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
			// adapter = PullParserFromXML(myResult);
			// Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 이클래스 세션 획득 과정2");

		
		
		
		
		
		
		
		
		
		/*
		 * 이클래스 인증 과정3
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://eclass.ajou.ac.kr:8090/uSosWeb/x/login_post_proc.jsp?userno="+temp_student_id); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 이클래스 인증 과정3");
			// --------------------------
			// 전송 모드 설정 - 기본적인 설정이다
			// --------------------------
			http.setDefaultUseCaches(false);
			http.setDoInput(true); // 서버에서 읽기 모드 지정
			http.setDoOutput(true); // 서버로 쓰기 모드 지정
			http.setRequestMethod("GET"); // 전송 방식은 POST

			// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
			http.setRequestProperty("Accept", "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			http.setRequestProperty("Accept-Language", "ko-KR");
			http.setRequestProperty("Referer",
					"http://portal.ajou.ac.kr/portal/goToMyPage.action");
			http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; MASN)");
			http.setRequestProperty("Host", "eclass.ajou.ac.kr:8090");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "Cookie: " + jsessionid+ "; " + ssotoken.substring(0, ssotoken.indexOf(";")-1));
			http.setRequestProperty("Cookie", jsessionid+ "; " + ssotoken);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------
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
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 4");
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
			// adapter = PullParserFromXML(myResult);
			// Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 이클래스 인증 과정3");
		
		
		
		
		
		
		
		/*
		 * 이클래스 인증 과정3
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://eclass.ajou.ac.kr:8090/uSosWeb/eclass/SSO_Login.do?task=SSO_LOGIN"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 이클래스 인증 과정3");
			// --------------------------
			// 전송 모드 설정 - 기본적인 설정이다
			// --------------------------
			http.setDefaultUseCaches(false);
			http.setDoInput(true); // 서버에서 읽기 모드 지정
			http.setDoOutput(true); // 서버로 쓰기 모드 지정
			http.setRequestMethod("GET"); // 전송 방식은 POST

			// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
			http.setRequestProperty("Accept", "application/x-ms-application, image/jpeg, application/xaml+xml, image/gif, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			http.setRequestProperty("Accept-Language", "ko-KR");
			http.setRequestProperty("Referer",
					"http://eclass.ajou.ac.kr:8090/uSosWeb/x/login_post_proc.jsp?userno=200621756");
			http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; MASN)");
			http.setRequestProperty("Host", "eclass.ajou.ac.kr:8090");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "Cookie: " + jsessionid+ "; " + ssotoken.substring(0, ssotoken.indexOf(";")-1));
			http.setRequestProperty("Cookie", jsessionid+ "; " + ssotoken);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------
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
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 4");
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
			// adapter = PullParserFromXML(myResult);
			// Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 이클래스 인증 과정3");
		
		
		
		
		
		
		
		
		
		/*
		 * 이클래스 내용획득 과정4
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://eclass.ajou.ac.kr:8090/uSosWeb/eclass/findMain.do"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 이클래스 내용획득 과정4");
			// --------------------------
			// 전송 모드 설정 - 기본적인 설정이다
			// --------------------------
			http.setDefaultUseCaches(false);
			http.setDoInput(true); // 서버에서 읽기 모드 지정
			http.setDoOutput(true); // 서버로 쓰기 모드 지정
			http.setRequestMethod("POST"); // 전송 방식은 POST

			// 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
			http.setRequestProperty("Accept", "*/*");
			http.setRequestProperty("Accept-Language", "ko");
			http.setRequestProperty("Referer",
					"http://eclass.ajou.ac.kr:8090/uSosWeb/findMainStud.do");
			http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; MASN)");
			http.setRequestProperty("Host", "eclass.ajou.ac.kr:8090");
			http.setRequestProperty("Pragma", "no-cache");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "jsessionid: " + jsessionid);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "ssotoken: " + ssotoken.substring(0, ssotoken.indexOf(";")-1));
			http.setRequestProperty("Cookie", jsessionid+";");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------

			StringBuffer buffer = new StringBuffer();
			String parameter = null;
			parameter = new String(
					"taskId=F_STU_MAIN&flag=homeWork");

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
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 4");
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
			// adapter = PullParserFromXML(myResult);
			// Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 이클래스 내용획득 과정4");
		// return adapter;
		return myResult.toString().substring(2);
	}

	private MemberAdapter PullParserFromXML(String data) {
		MemberAdapter adapter = null;
		ArrayList<MemberInfo> m_orders = new ArrayList<MemberInfo>();

		if (intro.DEBUG)
			Log.i(intro.LOG_TAG, "[Parse] DATA = " + data);
		
		if (intro.DEBUG)
			Log.i(intro.LOG_TAG, "[Parse]");
		boolean title = false; // 제목
		boolean noteCont = false; // 내용
		boolean regiDttm = false; // 작성일

		try {

			XmlPullParserFactory parserCreator = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();
			String tag;
			parser.setInput(new StringReader(data));

			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[Parse] Start");
			int parserEvent = parser.getEventType();

			MemberInfo member = new MemberInfo();
			while (parserEvent != XmlPullParser.END_DOCUMENT) {
				switch (parserEvent) {
				case XmlPullParser.START_DOCUMENT: // 문서의 시작
					if (intro.DEBUG)
						Log.i(intro.LOG_TAG, "[Parse] 문서시작");
					break;

				case XmlPullParser.END_DOCUMENT:
					if (intro.DEBUG)
						Log.i(intro.LOG_TAG, "[Parse] 문서끝");// 문서의 끝
					break;

				case XmlPullParser.TEXT:
					tag = parser.getName();

					if (title) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG,
									"[Parse] title = " + parser.getText());
						member.title = parser.getText();
					}
					if (noteCont) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG, "[Parse] noteCont = "
									+ parser.getText());
						member.noteCont = parser.getText();
					}
					if (regiDttm) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG, "[Parse] regiDttm = "
									+ parser.getText());
						member.regiDttm = parser.getText();
					}
					break;

				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.compareTo("title") == 0) {
						title = false;
					}
					if (tag.compareTo("noteCont") == 0) {
						noteCont = false;
					}
					if (tag.compareTo("regiDttm") == 0) {
						regiDttm = false;
						m_orders.add(member);
						member = new MemberInfo();
					}
					break;

				case XmlPullParser.START_TAG:
					tag = parser.getName();

					if (tag.compareTo("title") == 0) {
						title = true;
					}
					if (tag.compareTo("noteCont") == 0) {
						noteCont = true;
					}
					if (tag.compareTo("regiDttm") == 0) {
						regiDttm = true;
					}
					break;
				}
				parserEvent = parser.next();
			}
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[Parse] End");
			adapter = new MemberAdapter(context, R.layout.eclass_notice_list_item,
					m_orders);
		} catch (Exception e) {
			if (intro.DEBUG)
				Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
		}
		return adapter;
	}

}
