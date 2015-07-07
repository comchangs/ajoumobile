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
import ajou.amicom.ajouInfo.phone.*;
import ajou.amicom.ajouInfo.portal.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.content.res.AssetManager.AssetInputStream;
import android.net.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.webkit.*;
import android.widget.*;

public class eclass_homework_view extends Activity {
	Context context;
	String clubNm;
	String clubId;
	String workSeq;
	String workCont;
	String workStDt;
	String workEdDt;
	String workTitle;
	File gobal_File;
	ProgressDialog mProgress;
	DownloadThread mThread;
	
	 public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	 private ProgressDialog mProgressDialog;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.eclass_homework_view);
		context = this;
		
		Bundle extras = getIntent().getExtras();
        this.clubNm = new String((String) extras.get("clubNm"));
        this.clubId = new String((String) extras.get("clubId"));
        this.workSeq = new String((String) extras.get("workSeq"));
        this.workCont = new String((String) extras.get("workCont"));
        this.workStDt = new String((String) extras.get("workStDt"));
        this.workEdDt = new String((String) extras.get("workEdDt"));
        this.workTitle = new String((String) extras.get("workTitle"));
             
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(clubNm);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(workTitle);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText(workStDt);
        TextView textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText(workEdDt);
        Source source = new Source(workCont);
		source.fullSequentialParse();
		String str = source.getRenderer().setIncludeHyperlinkURLs(false).toString();
        TextView textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setText(str);
        
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
						final int arg2, long arg3) {
					File path = new File("/mnt/sdcard/AjouMobile/HomeworkData/" + clubNm/*.substring(0, clubNm.indexOf(" (")).replaceAll(" ", "")*/);
				     if(! path.isDirectory()) {
				             path.mkdirs();
				      }
					
					final File file = new File(
							"/mnt/sdcard/AjouMobile/HomeworkData/" + clubNm/*.substring(0, clubNm.indexOf(" (")).replaceAll(" ", "")*/ + "/"
									+ adapter.getItem(arg2).regiFileNm/*.replaceAll(" ", "")*/);
					
					if (fileCheck(file,
							Integer.parseInt(adapter.getItem(arg2).fileSize))) {
						AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
								eclass_homework_view.this);
						alert_internet_status.setTitle("파일 중복");
						alert_internet_status.setMessage("이미 파일이 아래의 경로에 저장되어 있습니다. \n"
								+ file.toString());
						alert_internet_status.setPositiveButton("파일열기",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss(); // 닫기
										fileView(file);
									}
								});
						alert_internet_status.setNegativeButton("다운로드",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss(); // 닫기
										//Intent intent = new Intent(eclass_note_view.this, eclass_note_view_open.class);
										//intent.putExtra("filepath",	file.toString());
										//intent.putExtra("fileSeq",	adapter.getItem(arg2).fileSeq);
										//intent.putExtra("regiId",	adapter.getItem(arg2).regiId);
										//intent.putExtra("noteSeq",	noteSeq);
										//startActivity(intent);
										mProgress = new ProgressDialog(context);
										mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
										mProgress.setTitle("Downloading...");
										mProgress.setMessage("잠시만 기다려주십시오.");
										mProgress.setCancelable(false);
										/*mProgress.setButton("Cancel", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												mThread.mQuit = true;
												mProgress.dismiss();
											}
										});*/
										mProgress.show();
										mThread = new DownloadThread(file, adapter.getItem(arg2).fileSeq, adapter.getItem(arg2).regiId, mAfterThread);
										mThread.start();
									}
								});
						alert_internet_status.show();
					} else {
						//Intent intent = new Intent(eclass_note_view.this, eclass_note_view_open.class);
						//intent.putExtra("filepath",	file.toString());
						//intent.putExtra("fileSeq",	adapter.getItem(arg2).fileSeq);
						//intent.putExtra("regiId",	adapter.getItem(arg2).regiId);
						//intent.putExtra("noteSeq",	noteSeq);
						//startActivity(intent);
						mProgress = new ProgressDialog(context);
						mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						mProgress.setTitle("Downloading...");
						mProgress.setMessage("잠시만 기다려주십시오.");
						mProgress.setCancelable(false);
						/*mProgress.setButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								mThread.mQuit = true;
								mProgress.dismiss();
							}
						});*/
						mProgress.show();
						mThread = new DownloadThread(file, adapter.getItem(arg2).fileSeq, adapter.getItem(arg2).regiId, mAfterThread);
						
						mThread.start();
					}
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
				item_view = vi.inflate(R.layout.eclass_note_view_list_item, null);
			}

			if (items != null) {
				((TextView) item_view.findViewById(R.id.regiFileNm))
						.setText(items.get(position).regiFileNm);
				((TextView) item_view.findViewById(R.id.downCnt))
				.setText(items.get(position).downCnt);
			}
			return item_view;
		}
	}

	class MemberInfo {
		String fileSeq;
		String sysPath;
		String sysFileNm;
		String regiFileNm;
		String fileSize;
		String downCnt;
		String regiId;
	}

	String DownloadXML() {
		SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // Shared Preference를 불러옵니다.
    	
        // 저장된 값들을 불러옵니다.
    	String temp_id = pref.getString("temp_id", "");
    	String temp_password = pref.getString("temp_password", "");
    	String temp_student_id = pref.getString("temp_student_id", "");
    	
    	String myResult = null;
		String ssotoken = null;
		String jsessionid = null;
		String PHAROS_VISITOR = null;
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
			http.setRequestProperty("Host", "portal.ajou.ac.kr");
			http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] ID: "+temp_id);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] PW: "+temp_password);
			
			StringBuffer buffer = new StringBuffer();
			String parameter = null;
			parameter = new String(
					"signed_data=&LOGIN_RNG=XFyu9fFH2439OFQKaBPKdA%3D%3D&nextRedirectUrl=&isPki=noPKI&j_username="+temp_id+"&j_password="+temp_password); //new(20120110)
			//"signed_data=&LOGIN_RNG=z5fZaWFot4JxcKfoTxNOpg%3D%3D&isPki=noPKI&j_username="+id+"&j_password="+password); //old

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
			if (intro.DEBUG) {
				Set set = headers.keySet();
				Object []headersKeys = set.toArray();
				for(int i = 0; i < headers.size(); i++) {
					String key = (String)headersKeys[i];   
					Log.i(intro.LOG_TAG, "[HTTP POST] header - "+key+ " : " +headers.get(key).toString());
				}
			}
			List<String> value = headers.get("set-cookie");
			if (intro.DEBUG) {
				for(int i = 0; i < headers.get("set-cookie").size(); i++) {
					Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(i));
				}
			}
			jsessionid = value.get(4).substring(0, value.get(4).indexOf(";"));
			ssotoken = value.get(2).substring(0, value.get(2).indexOf(";"));
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
		 * 아주대 통합 로그인 과정2
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://portal.ajou.ac.kr/portal/login.action"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 아주대 통합 로그인 과정2");
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
			http.setRequestProperty("Host", "portal.ajou.ac.kr");
			//if (intro.DEBUG)
			//	Log.i(intro.LOG_TAG, "Cookie: " + jsessionid+ "; " + ssotoken.substring(0, ssotoken.indexOf(";")-1));
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
			Map<String, List<String>> headers = http.getHeaderFields();
			if (intro.DEBUG) {
				Set set = headers.keySet();
				Object []headersKeys = set.toArray();
				for(int i = 0; i < headers.size(); i++) {
					String key = (String)headersKeys[i];   
					Log.i(intro.LOG_TAG, "[HTTP POST] header - "+key+ " : " +headers.get(key).toString());
				}
			}
			List<String> value = headers.get("set-cookie");
			if (intro.DEBUG) {
				for(int i = 0; i < headers.get("set-cookie").size(); i++) {
					Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(i));
				}
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
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 아주대 통합 로그인 과정2");
		
		
		/*
		 * 아주대 통합 로그인 과정3
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://portal.ajou.ac.kr/portal/goToMyPage.action"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 아주대 통합 로그인 과정3");
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
			http.setRequestProperty("Host", "portal.ajou.ac.kr");
			//if (intro.DEBUG)
			//	Log.i(intro.LOG_TAG, "Cookie: " + jsessionid+ "; " + ssotoken.substring(0, ssotoken.indexOf(";")-1));
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
			Map<String, List<String>> headers = http.getHeaderFields();
			if (intro.DEBUG) {
				Set set = headers.keySet();
				Object []headersKeys = set.toArray();
				for(int i = 0; i < headers.size(); i++) {
					String key = (String)headersKeys[i];   
					Log.i(intro.LOG_TAG, "[HTTP POST] header - "+key+ " : " +headers.get(key).toString());
				}
			}
			List<String> value = headers.get("set-cookie");
			if (intro.DEBUG) {
				for(int i = 0; i < headers.get("set-cookie").size(); i++) {
					Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(i));
				}
			}
			PHAROS_VISITOR = value.get(0).substring(0, value.get(0).indexOf(";"));
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
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 아주대 통합 로그인 과정3");
		
		
		
		/*
		 * 아주대 통합 로그인 과정4
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://portal.ajou.ac.kr/portal/findPortalMenuByRole.action"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 아주대 통합 로그인 과정4");
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
			http.setRequestProperty("Host", "portal.ajou.ac.kr");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "Cookie: " + jsessionid+ ";\n " + ssotoken + ";\n " + PHAROS_VISITOR);
			http.setRequestProperty("Cookie", jsessionid+ "; " + ssotoken + "; " + PHAROS_VISITOR);
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
			Map<String, List<String>> headers = http.getHeaderFields();
			if (intro.DEBUG) {
				Set set = headers.keySet();
				Object []headersKeys = set.toArray();
				for(int i = 0; i < headers.size(); i++) {
					String key = (String)headersKeys[i];   
					Log.i(intro.LOG_TAG, "[HTTP POST] header - "+key+ " : " +headers.get(key).toString());
				}
			}
			//List<String> value = headers.get("set-cookie");
			//if (intro.DEBUG) {
			//	for(int i = 0; i < headers.get("set-cookie").size(); i++) {
			//		Log.i(intro.LOG_TAG, "[HTTP POST] set-cookie = " + value.get(i));
			//	}
			//}
			while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄 것이므로
														// 라인단위로 읽는다

				if (intro.DEBUG)
					Log.i(intro.LOG_TAG, "[HTTP POST] 수신결과zz" + str + "\n");
				builder.append(str + "\n"); // View에 표시하기 위해 라인 구분자 추가 }
			}
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 전송받음");

			myResult = builder.toString(); // 전송결과를 전역 변수에 저장
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 수신결과" + myResult);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 수신결과" + myResult.length());
			
			Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 아주대 통합 로그인 과정4");

		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		
		
		
		
		
		/*
		 * 이클래스 세션 획득 과정2
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://eclass.ajou.ac.kr/uSosWeb/x/portal_link_work.jsp"); // URL
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
			http.setRequestProperty("Host", "eclass.ajou.ac.kr");
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
					"http://eclass.ajou.ac.kr/uSosWeb/x/login_post_proc.jsp?userno="+temp_student_id); // URL
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
			http.setRequestProperty("Host", "eclass.ajou.ac.kr");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "Cookie: " + jsessionid+ "; " + ssotoken + "; " + PHAROS_VISITOR);
			http.setRequestProperty("Cookie", jsessionid+ "; " + ssotoken + "; " + PHAROS_VISITOR);
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
					"http://eclass.ajou.ac.kr/uSosWeb/eclass/SSO_Login.do?task=SSO_LOGIN"); // URL
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
					"http://eclass.ajou.ac.kr/uSosWeb/x/login_post_proc.jsp?userno=200621756");
			http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; MASN)");
			http.setRequestProperty("Host", "eclass.ajou.ac.kr");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "Cookie: " + jsessionid+ "; " + ssotoken + "; " + PHAROS_VISITOR);
			http.setRequestProperty("Cookie", jsessionid+ "; " + ssotoken + "; " + PHAROS_VISITOR);
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
		 * 이클래스 파일정보획득 과정
		 */
		myResult = null;
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://eclass.ajou.ac.kr/uSosWeb/eclass/findHomeWork.do?taskId=F_FROF_FILE"); // URL
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] Start - 이클래스 파일정보획득 과정");
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
					"http://eclass.ajou.ac.kr/uSosWeb/findMainStud.do#");
			http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Accept-Encoding", "gzip, deflate");
			http.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; MASN)");
			http.setRequestProperty("Host", "eclass.ajou.ac.kr");
			http.setRequestProperty("Pragma", "no-cache");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "jsessionid: " + jsessionid);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "ssotoken: " + ssotoken);
			http.setRequestProperty("Cookie", jsessionid + "; " + PHAROS_VISITOR);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------

			StringBuffer buffer = new StringBuffer();
			String parameter = null;
			parameter = null;
			
			parameter = new String("taskId=F_FROF_FILE&sysType=ECLS&clubId="+clubId+"&workSeq="+workSeq+"&menuCode=2003&menuNo=1");
			//parameter = new String("sysType=ECLS&clubId=S2011220110657316&workSeq=10024116&menuCode=2003&menuNo=1");

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
		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		Log.i(intro.LOG_TAG, "[HTTP POST] 과정완료 - 이클래스 파일정보획득 과정");
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
		boolean fileSeq = false; 
		boolean sysPath = false; 
		boolean sysFileNm = false; 
		boolean regiFileNm = false; 
		boolean fileSize = false;
		boolean downCnt = false; 
		boolean regiId = false; 
		
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

					if (fileSeq) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG,
									"[Parse] fileSeq = " + parser.getText());
						member.fileSeq = parser.getText();
					}
					if (sysPath) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG,
									"[Parse] sysPath = " + parser.getText());
						member.sysPath = parser.getText();
					}
					if (sysFileNm) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG, "[Parse] sysFileNm = "
									+ parser.getText());
						member.sysFileNm = parser.getText();
					}
					if (regiFileNm) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG,
									"[Parse] regiFileNm = " + parser.getText());
						member.regiFileNm = parser.getText();
					}
					if (fileSize) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG,
									"[Parse] fileSize = " + parser.getText());
						member.fileSize = parser.getText();
					}
					if (downCnt) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG, "[Parse] downCnt = "
									+ parser.getText());
						member.downCnt = parser.getText();
					}
					if (regiId) {
						if (intro.DEBUG)
							Log.i(intro.LOG_TAG,
									"[Parse] regiId = " + parser.getText());
						member.regiId = parser.getText();
					}
					break;

				case XmlPullParser.END_TAG:
					tag = parser.getName();
					
					if (tag.compareTo("fileSeq") == 0) {
						fileSeq = false;
					}
					if (tag.compareTo("sysPath") == 0) {
						sysPath = false;
					}
					if (tag.compareTo("sysFileNm") == 0) {
						sysFileNm = false;
					}
					if (tag.compareTo("regiFileNm") == 0) {
						regiFileNm = false;
					}
					if (tag.compareTo("fileSize") == 0) {
						fileSize = false;
					}
					if (tag.compareTo("downCnt") == 0) {
						downCnt = false;
					}
					if (tag.compareTo("regiId") == 0) {
						regiId = false;
					}
					if (tag.compareTo("updtIp") == 0) {
						m_orders.add(member);
						member = new MemberInfo();
					}
					break;

				case XmlPullParser.START_TAG:
					tag = parser.getName();

					if (tag.compareTo("fileSeq") == 0) {
						fileSeq = true;
					}
					if (tag.compareTo("sysPath") == 0) {
						sysPath = true;
					}
					if (tag.compareTo("sysFileNm") == 0) {
						sysFileNm = true;
					}
					if (tag.compareTo("regiFileNm") == 0) {
						regiFileNm = true;
					}
					if (tag.compareTo("fileSize") == 0) {
						fileSize = true;
					}
					if (tag.compareTo("downCnt") == 0) {
						downCnt = true;
					}
					if (tag.compareTo("regiId") == 0) {
						regiId = true;
					}
					break;
				}
				parserEvent = parser.next();
			}
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[Parse] End");
			adapter = new MemberAdapter(context, R.layout.eclass_note_view_list_item,
					m_orders);
		} catch (Exception e) {
			if (intro.DEBUG)
				Log.e(intro.LOG_TAG, "[Parse] Error in network call", e);
		}
		return adapter;
	}
	
	class DownloadThread extends Thread {
		
		Handler mAfter;
		public boolean mQuit;
		File mfile;
		String mfileSeq;
		String mregiId;
		
		
		DownloadThread(File file, String fileSeq, String regiId, Handler after) {
			mfile = file;
			mfileSeq = fileSeq;
			mregiId = regiId;
			mQuit = false;
			mAfter = after;
		}

		public void run() {
			if (intro.DEBUG) Log.i(intro.LOG_TAG, "[Download] Strat");
			fileDown(mfile, mfileSeq, mregiId);
			mAfter.sendEmptyMessage(0);
			if (intro.DEBUG) Log.i(intro.LOG_TAG, "[Download] End");
		}
	}

	Handler mAfterThread = new Handler() {
		public void handleMessage(Message msg) {
			mProgress.dismiss();
			if (mThread.mQuit == false) {
			}
		}
	};
	
	void fileDown (final File file, String fileSeq, String regiId) {
		
		
		
		try {
			// --------------------------
			// URL 설정하고 접속하기
			// --------------------------
			URL url = new URL(
					"http://eclass.ajou.ac.kr/uSosWeb/board/findBoard.do"); // URL2
																					// 설정
			HttpURLConnection http = (HttpURLConnection) url
					.openConnection(); // 접속
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 접속");
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
					"http://eclass.ajou.ac.kr/uSosWeb/findMainStud.do#");
			http.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			http.setRequestProperty("Accept-Encoding",
					"gzip, deflate");
			http.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; MASN)");
			http.setRequestProperty("Host",
					"eclass.ajou.ac.kr");
			http.setRequestProperty("Pragma", "no-cache");
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] 헤더생성");
			// --------------------------
			// 서버로 값 전송
			// --------------------------
			StringBuffer buffer = new StringBuffer();

			String parameter = null;
			parameter = new String(
					"taskId=FILE_DOWN&fileSeq=" + fileSeq); // 파일다운로드

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
			OutputStream out = new FileOutputStream(file);
			writeFile(http.getInputStream(), out);
			out.close();
			
			gobal_File = file;
			Downloaded.sendEmptyMessage(0);


		} catch (MalformedURLException e) {
			Toast.makeText(eclass_homework_view.this,
					"MalformedURLException", 0).show();
		} catch (IOException e) {
			Toast.makeText(eclass_homework_view.this,
					"IOException", 0).show();
			if (intro.DEBUG)
				if (intro.DEBUG)
					Log.e(intro.LOG_TAG, "[download error]\n" + e);
		}
	}
	
	Handler Downloaded = new Handler() {
		public void handleMessage(Message msg) {
			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					eclass_homework_view.this);
			alert_internet_status.setTitle("파일 다운로드 완료!");
			alert_internet_status.setMessage("파일의 저장 경로는 "
					+ gobal_File.toString()
					+ "입니다.\n폰에 해당 파일을 읽을 수 있는 어플리케이션이 설치 되어 있어야 정상적으로 열 수 있습니다.");
			alert_internet_status.setPositiveButton("열기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
							fileView(gobal_File);
						}
					});
			alert_internet_status.setNegativeButton("닫기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
						}
					});
			alert_internet_status.show();
		}
	};
	
	void writeFile(InputStream is, OutputStream os) throws IOException
	{
	     int c = 0;
	     while((c = is.read()) != -1)
	         os.write(c);
	     os.flush();
	}
	
	boolean fileCheck(File file, int length){
		if (file.exists()) {
		 String filename = file.toString();
		 if (file.length() == length) {
			 return true;
		 }
		}
		return false;
	}
	
	void fileView(File file){
		if (file.exists()) {
			String filename = file.toString();
			MimeTypeMap mtm = MimeTypeMap.getSingleton(); // mime type 추출용
			String fileExtension = filename.substring(
					filename.lastIndexOf(".") + 1, filename.length())
					.toLowerCase(); // 파일 확장자 추출
			String mimeType = mtm.getMimeTypeFromExtension(fileExtension); // 확장자를
																			// 통한
																			// mime
																			// type
																			// 추출

			if (mimeType != null) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file), mimeType);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException ane) {
					checkFileExtension(file, fileExtension);
				}
			} else {
				checkFileExtension(file, fileExtension);
			}
		} else {
			Toast.makeText(eclass_homework_view.this,
					"다른 곳으로 이동되거나 삭제된 파일입니다.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void checkFileExtension(File file, String fileExtension) {
		Toast.makeText(eclass_homework_view.this, "지원하지 않는 파일 형식입니다.",
				Toast.LENGTH_SHORT).show();
		if (fileExtension.equals("hwp")) {
			final Intent intent = hwp_checkPackage();
			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					eclass_homework_view.this);
			alert_internet_status.setTitle("뷰어 설치");
			alert_internet_status
					.setMessage("한글 파일은 자동 열기가 되지 않습니다.\n한글 뷰어를 열어 아래 파일을 여기시 바랍니다.\n "
							+ file.toString());
			alert_internet_status.setPositiveButton("열기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
							startActivity(intent);
						}
					});
			alert_internet_status.setNegativeButton("닫기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
						}
					});
			alert_internet_status.show();
		} else if (fileExtension.equals("ppt") || fileExtension.equals("pptx") || 
				fileExtension.equals("xls") || fileExtension.equals("xlsx") || 
				fileExtension.equals("doc") || fileExtension.equals("docx")) {
			final Intent intent = ppt_checkPackage();
			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					eclass_homework_view.this);
			alert_internet_status.setTitle("뷰어 설치");
			alert_internet_status
					.setMessage("뷰어가 설치 되지 않았거나, 바로 실행 할 수 없는 상태인 경우입니다.\n뷰어를 열어 아래 파일을 여기시 바랍니다.\n "
							+ file.toString());
			alert_internet_status.setPositiveButton("열기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
							startActivity(intent);
						}
					});
			alert_internet_status.setNegativeButton("닫기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
						}
					});
			alert_internet_status.show();
		} else if (fileExtension.equals("pdf")) {
			final Intent intent = pdf_checkPackage();
			AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(
					eclass_homework_view.this);
			alert_internet_status.setTitle("뷰어 설치");
			alert_internet_status
					.setMessage("뷰어가 설치 되지 않았거나, 바로 실행 할 수 없는 상태인 경우입니다.\n뷰어를 열어 아래 파일을 여기시 바랍니다.\n "
							+ file.toString());
			alert_internet_status.setPositiveButton("열기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
							startActivity(intent);
						}
					});
			alert_internet_status.setNegativeButton("닫기",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); // 닫기
						}
					});
			alert_internet_status.show();
		}
	}
	
	Intent hwp_checkPackage() {
		final PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=kr.co.hancom.hancomviewer.androidmarket"));
		for (ApplicationInfo packageInfo : packages) {
		if("kr.co.hancom.hancomviewer.androidmarket".equals(packageInfo.packageName)) {
		return new Intent(pm.getLaunchIntentForPackage(packageInfo.packageName));
		}
		Log.d("BaekSupervisor", "Installed package :" + packageInfo.packageName);
		Log.d("BaekSupervisor", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
		}
		return intent;
	}
	
	Intent ppt_checkPackage() {
		final PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=com.tf.thinkdroid.amlite"));
		for (ApplicationInfo packageInfo : packages) {
		if("com.tf.thinkdroid.amlite".equals(packageInfo.packageName)) {
		return new Intent(pm.getLaunchIntentForPackage(packageInfo.packageName));
		}
		Log.d("BaekSupervisor", "Installed package :" + packageInfo.packageName);
		Log.d("BaekSupervisor", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
		}
		return intent;
	}
	
	Intent pdf_checkPackage() {
		final PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=com.adobe.reader"));
		for (ApplicationInfo packageInfo : packages) {
		if("com.adobe.reader".equals(packageInfo.packageName)) {
		return new Intent(pm.getLaunchIntentForPackage(packageInfo.packageName));
		}
		Log.d("BaekSupervisor", "Installed package :" + packageInfo.packageName);
		Log.d("BaekSupervisor", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
		}
		return intent;
	}
}
