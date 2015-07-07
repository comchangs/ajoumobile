/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.main.*;
import android.app.*;
import android.content.*;
import android.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;

public class ElectronicAttendanceControl {
	private String Cookie = new String();
	private String[] className2 = null;
	private Vector<ElectronicAttendanceVO> electronicAttendanceVOVector = new Vector<ElectronicAttendanceVO>();
	private String temp = null;
	
	public Vector<ElectronicAttendanceVO> method(String id, String password, String stdnum)
			throws Exception {
		String location = null; // Redirect 주소지
		/* cookie값 start */
		String cookie_old = null;
		String ssoportal_old = null;
		String ssotoken_old = null;
		String jsessionid_old = null;
		String jsessionid2_old = null;
		/* cookie값 end */
		String studentId_old = null; // 학번
		StringBuffer stringBuffer_old = new StringBuffer();
		BufferedReader bufferedReader_old = null;
		PrintWriter printWriter_old = null;
		URL url_old = null;
		Map<String, List<String>> headers_old = null;
		HashMap<String, List<Element>> htmlElement_old = new HashMap<String, List<Element>>();
		Vector<String> tagNames_old = new Vector<String>();
		List<String> value_old = null;
		List<Element> listElement_old = null;
		URLConnection urlConnection_old = null;
		HttpURLConnection.setFollowRedirects(false);
		HttpURLConnection httpUrlConnection = null;
		HttpURLConnection.setFollowRedirects(false);
		Util util_old = new Util();
		String line_old = new String();

		int classNumber = 0;
		String[] className = new String[10];
		String[] lec_year = new String[10];
		String[] lec_term = new String[10];
		String[] lec_no = new String[10];
		String[] lec_division_code = new String[10];
		String[] lec_use_opus = new String[10];
		String[] show = new String[10];
/*--------------------------------------------------------------------------------------------------
		/* ajou_login_1 *
		url = new URL(StringURL.login_url); // ajou_login_1
		urlConnection = url.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		stringBuffer.append("j_username").append("=").append(id).append("&");
		stringBuffer.append("j_password").append("=").append(password);

		printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "EUC-KR"));
		headers = httpUrlConnection.getHeaderFields();
		value = headers.get("set-cookie");
		ssoportal = value.get(0);
		ssotoken = value.get(1);
		ssoportal = ssoportal.substring(0, ssoportal.indexOf(";"));
		ssotoken = ssotoken.substring(0, ssotoken.indexOf(";"));
		value = headers.get("location");
		location = value.get(0);
		jsessionid = location.substring(location.indexOf(";") + 1,
				location.length());
		location = location.substring(0, location.indexOf(";"));

		String line = new String();
		stringBuffer = new StringBuffer();
		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);

		/* http://portal.ajou.ac.kr/portal/login.action with cookie jsseionid *
		cookie = jsessionid;
		cookie = cookie + ";" + ssoportal;
		cookie = cookie + ";" + ssotoken;

		url = new URL(location); //
		urlConnection = url.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		httpUrlConnection.setRequestProperty("cookie", cookie);

		bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "EUC-KR"));

		stringBuffer = new StringBuffer();
		line = new String();
		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);
		cookie = jsessionid;
		cookie = cookie + ";" + ssoportal;
		cookie = cookie + ";" + ssotoken;

		url = new URL(
				StringURL.findStudentId);
		urlConnection = url.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		httpUrlConnection.setRequestProperty("cookie", cookie);

		bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "EUC-KR"));

		stringBuffer = new StringBuffer();
		line = new String();
		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);
		/*
		 * http://portal.ajou.ac.kr/portal/findPortalMenuByRole.action HTML 파싱
		 * (전자출석부url, 학번)
		 *
		tagNames.clear();
		tagNames.add(HTMLElementName.A);
		htmlElement = util.getHttpElementsMapFromString(
				stringBuffer.toString(), tagNames);
		listElement = htmlElement.get(HTMLElementName.A);
		String temp = listElement.get(listElement.size() - 1)
				.getAttributeValue("href");
		location = temp.substring(temp.indexOf("'") + 1, temp.indexOf("',"));
		studentId = temp.substring(temp.indexOf("=") + 1, temp.indexOf("',"));
		/* http://u-campus.ajou.ac.kr/atdc/sso/login_sso.jsp?userno=학번 *
		cookie = jsessionid;
		cookie = cookie + ";" + ssoportal;
		cookie = cookie + ";" + ssotoken;
---------------------------------------------------------------------------------------------*/
    	
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
				Log.i(intro.LOG_TAG, "[HTTP POST] ID: "+id);
			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] PW: "+password);
			
			StringBuffer buffer = new StringBuffer();
			String parameter = null;
			parameter = new String(
					"signed_data=&LOGIN_RNG=XFyu9fFH2439OFQKaBPKdA%3D%3D&nextRedirectUrl=&isPki=noPKI&j_username="+id+"&j_password="+password); //new(20120110)
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
		
		url_old = new URL("http://u-campus.ajou.ac.kr/atdc/sso/login_sso.jsp?userno="+stdnum);
		urlConnection_old = url_old.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection_old;
		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		httpUrlConnection.setRequestProperty("cookie", cookie_old);

		bufferedReader_old = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "UTF-8"));

		/* 새로운 jsessionid가입력 됨 */
		headers_old = httpUrlConnection.getHeaderFields();
		value_old = headers_old.get("set-cookie");
		jsessionid2_old = value_old.get(0);
		jsessionid2_old = jsessionid2_old.substring(0, jsessionid2_old.indexOf(";"));

		stringBuffer_old = new StringBuffer();
		line_old = new String();
		while ((line_old = bufferedReader_old.readLine()) != null)
			stringBuffer_old.append(line_old);
		// textView.setText(stringBuffer.toString()); //주민등록번호 나옴

		/* http://u-campus.ajou.ac.kr/atdc/student/login/login.ajou */
		location = "http://u-campus.ajou.ac.kr/atdc/student/login/login.ajou";

		cookie_old = jsessionid2_old;
		cookie_old = cookie_old;/* + ";" + ssoportal;*/
		cookie_old = cookie_old + ";" + ssotoken;

		url_old = new URL(location);
		urlConnection_old = url_old.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection_old;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");
		httpUrlConnection.setRequestProperty("cookie", cookie_old);

		stringBuffer_old = new StringBuffer();
		stringBuffer_old.append("id").append("=").append(stdnum).append("&");
		stringBuffer_old.append("iddi").append("=").append("&");
		stringBuffer_old.append("sso").append("=").append("Y");

		printWriter_old = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter_old.write(stringBuffer_old.toString());
		printWriter_old.flush();

		bufferedReader_old = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "UTF-8"));

		stringBuffer_old = new StringBuffer();
		line_old = new String();
		while ((line_old = bufferedReader_old.readLine()) != null)
			stringBuffer_old.append(line_old);

		/* http://u-campus.ajou.ac.kr/atdc/student/login/login.ajou HTML 파싱 */
		tagNames_old.clear();
		tagNames_old.add(HTMLElementName.A);
		htmlElement_old = util_old.getHttpElementsMapFromString(
				stringBuffer_old.toString(), tagNames_old);
		listElement_old = htmlElement_old.get(HTMLElementName.A);
		String temp1 = null;
		for (int i = 0; i < listElement_old.size(); ++i) {
			CharSequence cs = listElement_old.get(i);
			temp1 = cs.toString();
			if (temp1.indexOf("href=\"#\"") != -1) {
				// <a href="#"
				// onclick="javascript:fSelectItem('2010', '3', '201002688', '1', 'N', 'ok');">
				// 과목명 </a>
				temp1 = temp1.substring(temp1.indexOf("'"));
				lec_year[classNumber] = temp1.substring(temp1.indexOf("'") + 1,
						temp1.indexOf("',"));
				temp1 = temp1.substring(temp1.indexOf("', ") + 3);
				lec_term[classNumber] = temp1.substring(temp1.indexOf("'") + 1,
						temp1.indexOf("',"));
				temp1 = temp1.substring(temp1.indexOf("', ") + 3);
				lec_no[classNumber] = temp1.substring(temp1.indexOf("'") + 1,
						temp1.indexOf("',"));
				temp1 = temp1.substring(temp1.indexOf("', ") + 3);
				lec_division_code[classNumber] = temp1.substring(
						temp1.indexOf("'") + 1, temp1.indexOf("',"));
				temp1 = temp1.substring(temp1.indexOf("', ") + 3);
				lec_use_opus[classNumber] = temp1.substring(
						temp1.indexOf("'") + 1, temp1.indexOf("',"));
				temp1 = temp1.substring(temp1.indexOf("', ") + 3);
				show[classNumber] = temp1.substring(temp1.indexOf("'") + 1,
						temp1.indexOf("')"));
				temp1 = temp1.substring(temp1.indexOf("\""));
				className[classNumber] = temp1.substring(
						temp1.indexOf(">") + 1, temp1.indexOf("</")).trim();
				++classNumber;
			}
		}
		
		for (int i = 0; i < classNumber; ++i) {
			ElectronicAttendanceVO ElectVO = new ElectronicAttendanceVO();
			ElectVO.setClassName(className[i]);
			ElectVO.setLec_division_code(lec_division_code[i]);
			ElectVO.setLec_no(lec_no[i]);
			ElectVO.setLec_term(lec_term[i]);
			ElectVO.setLec_use_opus(lec_use_opus[i]);
			ElectVO.setLec_year(lec_year[i]);
			ElectVO.setShow(show[i]);
			electronicAttendanceVOVector.add(ElectVO);
		}
		
		Cookie = cookie_old;
		
		return getElectronicAttendanceVOVector();
	}

	public String getCookie() {
		return Cookie;
	}
	
	public String[] getClassName() {
		return className2;
	}

	public Vector<ElectronicAttendanceVO> getElectronicAttendanceVOVector() {
		return electronicAttendanceVOVector;
	}
	
	public Vector<ElectronicAttendanceStatusVO> getStatus(String lec_year, String lec_term, String lec_no, String lec_division_code) throws Exception{
		Vector<ElectronicAttendanceStatusVO> eAttendanceStatusVector = new Vector<ElectronicAttendanceStatusVO>();
		StringBuffer stringBuffer = null;
	    HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
    	Vector<String> tagNames = new Vector<String>();
    	List<Element> listElement = null;
    	
		String stringUrl = "http://u-campus.ajou.ac.kr/atdc/student/attendance/listPresence.ajou";
		URL url = new URL(stringUrl);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection)urlConnection;
        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setDefaultUseCaches(false);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setInstanceFollowRedirects(false);
        httpUrlConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
        httpUrlConnection.setRequestProperty("cookie", Cookie);
        
        stringBuffer = new StringBuffer();
        stringBuffer.append("searchLec_year").append("=").append(lec_year).append("&");
		stringBuffer.append("searchLec_term").append("=").append(lec_term).append("&");
		stringBuffer.append("searchLec_no").append("=").append(lec_no).append("&");
		stringBuffer.append("searchLec_division_code").append("=").append(lec_division_code);
		
		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();
		
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(), "UTF-8"));
        
        stringBuffer = new StringBuffer();
        String line = new String();
        while((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);
       
             
        Util util = new Util();
		tagNames.clear();
		tagNames.add("td class");
		htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
		listElement = htmlElement.get("td class");
		
		stringBuffer = new StringBuffer();
		for(int j=0;j<listElement.size();++j){
			
			CharSequence cs = listElement.get(j);
			String temp1 = cs.toString();
			stringBuffer.append(temp1);
		}
		String temp1 = stringBuffer.toString();
		temp = temp1;
		for(int i=0;;++i){
			ElectronicAttendanceStatusVO eVO = new ElectronicAttendanceStatusVO();
			eVO.setDate(temp1.substring(temp1.indexOf("\">")+2, temp1.indexOf("</")));
			temp1 = temp1.substring(temp1.indexOf("</")+1);
			eVO.setClassTime(temp1.substring(temp1.indexOf("\">")+2, temp1.indexOf("</")));
			temp1 = temp1.substring(temp1.indexOf("</")+1);
			eVO.setLocation(temp1.substring(temp1.indexOf("\">")+2, temp1.indexOf("</")).trim());
			temp1 = temp1.substring(temp1.indexOf("</")+1);
			eVO.setCheckTime(temp1.substring(temp1.indexOf("\">")+2, temp1.indexOf("</")));
			temp1 = temp1.substring(temp1.indexOf("</")+1);
			eVO.setCheckor(temp1.substring(temp1.indexOf("\">")+2, temp1.indexOf("</")));
			temp1 = temp1.substring(temp1.indexOf("</")+1);
			String status = temp1.substring(temp1.indexOf("ges/")+4, temp1.indexOf("gif")+3);
			if(status.equals("btn_s_att.gif")){
				status = "출석";
			}
			else if(status.equals("btn_s_lat.gif"))
				status = "지각";
			else if(status.equals("btn_s_abs.gif")){
				status = "결석";
			}
			eVO.setStatus(status);
			temp1 = temp1.substring(temp1.indexOf("</", temp1.indexOf("gif")));
			temp1 = temp1.substring(temp1.indexOf("</")+1);
			eVO.setOption(temp1.substring(temp1.indexOf("\">")+2, temp1.indexOf("</")));
			temp1 = temp1.substring(temp1.indexOf("</")+1);
			
			if(temp1.indexOf("</td>")==-1)
				break;
			
			eAttendanceStatusVector.add(eVO);
		}
		return eAttendanceStatusVector;
	}
	
	public void setCookie(String cookie){
		this.Cookie = cookie;
	}
	public String getTemp(){
		return temp;
	}
}
