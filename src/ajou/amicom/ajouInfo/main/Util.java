/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.main;

import java.io.*;
import java.net.*;
import java.util.*;

import ajou.amicom.ajouInfo.*;
import android.util.*;

import net.htmlparser.jericho.*;

public class Util {
	private String jsessionid = null;
	private String studentId = null;
	
	public String getsession(String body, String id) {
		String session = null;
		int start = body.indexOf(id);
		if (id.equals("jsessionid")) {
			start = body.indexOf("=", start);
			++start;
			int end = body.indexOf("\"", start);
			session = body.substring(start, end);
		} else if (id.equals("pdread")) {
			start = body.indexOf("=", start);
			++start;
			int end = start + 1;
			session = body.substring(start, end);
		}
		return session;
	}

	public HashMap<String, List<Element>> getHttpElementsMapFromString(
			String responseBody, Vector<String> tagNames) throws Exception {
		HashMap<String, List<Element>> elementMap = new HashMap<String, List<Element>>();

		Source source = new Source(responseBody);
		source.fullSequentialParse();
		List<Element> element = null;
		for (String tag : tagNames) {
			element = source.getAllElements(tag);
			if (element != null) {
				elementMap.put(tag, element);
			}
		}
		return elementMap;
	}

	public boolean loginTest2(String id, String password) throws Exception {

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
			Vector<String> tagNames = new Vector<String>();
			HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
			List<Element> listElement = null;
			Util util = new Util();
			tagNames.clear();
			tagNames.add(HTMLElementName.A);
			htmlElement = util.getHttpElementsMapFromString(myResult
					.toString(), tagNames);
			listElement = htmlElement.get(HTMLElementName.A);
			String temp = listElement.get(listElement.size() - 1)
					.getAttributeValue("href");
			String location = temp
					.substring(temp.indexOf("'") + 1, temp.indexOf("',"));
			studentId = temp.substring(temp.indexOf("=") + 1, temp
					.indexOf("',"));

			if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "[HTTP POST] studentId" + studentId);
			// adapter = PullParserFromXML(myResult);
			// Toast.makeText(this, "전송 후 결과 받음", 0).show();
		} catch (MalformedURLException e) {
			// Toast.makeText(this, "MalformedURLException", 0).show();
		} catch (IOException e) {
			// Toast.makeText(this, "IOException", 0).show();
		}
		
		if(studentId == null)
			return false;
		else
			return true;
		
		
		
		
		/*
		
		
		
		
		String stringURL = StringURL.login_url;
		URL url = null;
		StringBuffer stringBuffer = new StringBuffer();
		PrintWriter printWriter = null;
		List<String> value = null;
		Map<String, List<String>> headers = null;

		url = new URL(stringURL);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;

		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		stringBuffer.append("j_username").append("=").append(id).append("&");
		stringBuffer.append("j_password").append("=").append(password);

		printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
				.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		new BufferedReader(new InputStreamReader(httpUrlConnection
				.getInputStream(), "EUC-KR"));
		headers = httpUrlConnection.getHeaderFields();
		value = headers.get("location");
		if ((value != null) & (value.get(0).contains("login.action"))) {
			jsessionid = value.get(0).substring(
					value.get(0).lastIndexOf(";") + 1);
			headers = httpUrlConnection.getHeaderFields();
			value = headers.get("set-cookie");
			String ssoportal = value.get(0);
			String ssotoken = value.get(1);
			ssoportal = ssoportal.substring(0, ssoportal.indexOf(";"));
			ssotoken = ssotoken.substring(0, ssotoken.indexOf(";"));
			String cookie = jsessionid;
			cookie = cookie + ";" + ssoportal;
			cookie = cookie + ";" + ssotoken + ";";
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

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
					httpUrlConnection.getInputStream(), "EUC-KR"));

			stringBuffer = new StringBuffer();
			String line = new String();
			while ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);
			/*
			 * http://portal.ajou.ac.kr/portal/findPortalMenuByRole.action HTML
			 * 파싱 (전자출석부url, 학번)
			 *
			Vector<String> tagNames = new Vector<String>();
			HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
			List<Element> listElement = null;
			Util util = new Util();
			tagNames.clear();
			tagNames.add(HTMLElementName.A);
			htmlElement = util.getHttpElementsMapFromString(stringBuffer
					.toString(), tagNames);
			listElement = htmlElement.get(HTMLElementName.A);
			String temp = listElement.get(listElement.size() - 1)
					.getAttributeValue("href");
			String location = temp
					.substring(temp.indexOf("'") + 1, temp.indexOf("',"));
			studentId = temp.substring(temp.indexOf("=") + 1, temp
					.indexOf("',"));
			return true;
		} else
			return false;
		*/

	}
	
	public boolean loginTest(String id, String password) throws Exception {

		String stringURL = StringURL.login_url;
		URL url = null;
		StringBuffer stringBuffer = new StringBuffer();
		PrintWriter printWriter = null;
		List<String> value = null;
		Map<String, List<String>> headers = null;

		url = new URL(stringURL);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;

		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		stringBuffer.append("j_username").append("=").append(id).append("&");
		stringBuffer.append("j_password").append("=").append(password);

		printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
				.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		new BufferedReader(new InputStreamReader(httpUrlConnection
				.getInputStream(), "EUC-KR"));
		headers = httpUrlConnection.getHeaderFields();
		value = headers.get("location");
		if ((value != null) & (value.get(0).contains("login.action"))) {
			jsessionid = value.get(0).substring(
					value.get(0).lastIndexOf(";") + 1);
			headers = httpUrlConnection.getHeaderFields();
			value = headers.get("set-cookie");
			String ssoportal = value.get(0);
			String ssotoken = value.get(1);
			ssoportal = ssoportal.substring(0, ssoportal.indexOf(";"));
			ssotoken = ssotoken.substring(0, ssotoken.indexOf(";"));
			String cookie = jsessionid;
			cookie = cookie + ";" + ssoportal;
			cookie = cookie + ";" + ssotoken + ";";
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

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
					httpUrlConnection.getInputStream(), "EUC-KR"));

			stringBuffer = new StringBuffer();
			String line = new String();
			while ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);
			/*
			 * http://portal.ajou.ac.kr/portal/findPortalMenuByRole.action HTML
			 * 파싱 (전자출석부url, 학번)
			 */
			Vector<String> tagNames = new Vector<String>();
			HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
			List<Element> listElement = null;
			Util util = new Util();
			tagNames.clear();
			tagNames.add(HTMLElementName.A);
			htmlElement = util.getHttpElementsMapFromString(stringBuffer
					.toString(), tagNames);
			listElement = htmlElement.get(HTMLElementName.A);
			String temp = listElement.get(listElement.size() - 1)
					.getAttributeValue("href");
			String location = temp
					.substring(temp.indexOf("'") + 1, temp.indexOf("',"));
			studentId = temp.substring(temp.indexOf("=") + 1, temp
					.indexOf("',"));
			return true;
		} else
			return false;

	}

	public String getJsessionId() {
		return jsessionid;
	}

	public String getStudentId() {
		return studentId;
	}
}
