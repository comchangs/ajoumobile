/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.htmlparser.jericho.Element;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;
import android.util.*;

public class SeatExtendControl {
	private String seatNumber;
	private String seatTime;
	private String temp;
	private String studentId;
	private String message;
	private String id;
	private String password;

	public SeatExtendControl(String id, String password, String studentId)
			throws Exception {

		this.id = id;
		this.password = password;
		this.studentId = studentId;

		HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
		Vector<String> tagNames = new Vector<String>();
		List<Element> listElement = null;
		StringBuffer stringBuffer = new StringBuffer();
		PrintWriter printWriter = null;
		URL url = new URL(
				"http://u-campus.ajou.ac.kr/ltms/usrextend/vew.usrextend");
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		stringBuffer.append("ajou_usr_idno").append("=").append(studentId);

		printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(httpUrlConnection.getInputStream(),
						"utf-8"));

		stringBuffer = new StringBuffer();
		String line = new String();

		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);

		Util util = new Util();

		tagNames.clear();
		tagNames.add("td height");
		htmlElement = util.getHttpElementsMapFromString(
				stringBuffer.toString(), tagNames);
		listElement = htmlElement.get("td height");

		stringBuffer = new StringBuffer();

		boolean seatInfo = false;
		for (int i = 0; i < listElement.size(); ++i) {
			CharSequence cs = listElement.get(i);
			String temp1 = cs.toString();
			if (temp1.indexOf(studentId) != -1)
				seatInfo = true;
			if (seatInfo) {
				stringBuffer.append(i).append("\n").append(temp1).append("\n");
			}
		}

		temp = stringBuffer.toString();
		if (temp.indexOf("열람실 발급 내역") == -1) {
			temp = temp.substring(temp.indexOf("</b>"));
			seatNumber = temp.substring(temp.indexOf("<b>") + 3,
					temp.indexOf("</b>", temp.indexOf("<b>")));
			temp = temp.substring(temp.indexOf("</b>", temp.indexOf("<b>")));
			seatTime = temp.substring(temp.indexOf("<b>") + 3,
					temp.indexOf("</b>", temp.indexOf("<b>")));
		} else {
			temp = temp.substring(temp.indexOf("</b>"));
			seatNumber = temp.substring(temp.indexOf("<b>") + 3,
					temp.indexOf("</b>", temp.indexOf("<b>")));
			seatTime = "\n";
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void extend() throws Exception {
		HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
		Vector<String> tagNames = new Vector<String>();
		List<Element> listElement = null;
		Util util = new Util();
		String result = null;
		String jsession = null;
		StringBuffer stringBuffer = null;

		URL url = new URL(
				"http://library.ajou.ac.kr/ksign/login_proc_20100609.asp");
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		stringBuffer = new StringBuffer();

		stringBuffer.append("UID").append("=").append(id).append("&");
		stringBuffer.append("UPW").append("=").append(password);

		PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(httpUrlConnection.getInputStream(),
						"EUC-KR"));

		stringBuffer = new StringBuffer();
		String line = new String();

		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line + "\n");
		if (intro.DEBUG)
			Log.i(intro.LOG_TAG, "내용확인1" + stringBuffer.toString());
		stringBuffer = new StringBuffer();
		Map<String, List<String>> setCookie = httpUrlConnection
				.getHeaderFields();
		List<String> setCookieList = setCookie.get("set-cookie");
		for (int i = 0; i < setCookieList.size(); ++i) {
			stringBuffer.append(
					setCookieList.get(i).substring(0,
							setCookieList.get(i).indexOf(";"))).append(";");
		}

		String cookie = stringBuffer.toString();
		stringBuffer = new StringBuffer();

		url = new URL("http://u-campus.ajou.ac.kr/ltms/usrextend/udt.usrextend");
		urlConnection = url.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("Accept",
				"text/html, application/xhtml+xml, */*");
		httpUrlConnection.setRequestProperty("Referer",
				"http://u-campus.ajou.ac.kr/ltms/usrextend/vew.usrextend");
		httpUrlConnection.setRequestProperty("Accept-Language", "ko-KR");
		httpUrlConnection
				.setRequestProperty("User-Agent",
						"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; MASN)");
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");
		httpUrlConnection
				.setRequestProperty("Accept-Encoding", "gzip, deflate");
		httpUrlConnection.setRequestProperty("Pragma", "no-cache");
		httpUrlConnection.setRequestProperty("cookie", cookie);
		stringBuffer.append("ajou_usr_idno").append("=").append(studentId);
		printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "utf-8"));

		stringBuffer = new StringBuffer();
		line = new String();

		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line + "\n");
		if (intro.DEBUG)
			Log.i(intro.LOG_TAG, "내용확인2" + stringBuffer.toString());
		tagNames.clear();
		tagNames.add("input");
		tagNames.add("script");
		htmlElement = util.getHttpElementsMapFromString(
				stringBuffer.toString(), tagNames);
		listElement = htmlElement.get("input");

		stringBuffer = new StringBuffer();
		for (int i = 0; i < listElement.size(); ++i) {
			CharSequence cs = listElement.get(i);
			String temp1 = cs.toString();
			stringBuffer.append(temp1);
		}
		String temp = stringBuffer.toString();
		result = temp.substring(temp.indexOf("value=") + 7,
				temp.indexOf("\"/>"));
		Log.i(intro.LOG_TAG, "파싱확인 result = " + result);

		stringBuffer = new StringBuffer();
		listElement = htmlElement.get("script");
		for (int i = 0; i < listElement.size(); ++i) {
			CharSequence cs = listElement.get(i);
			String temp1 = cs.toString();
			stringBuffer.append(temp1);
		}
		temp = stringBuffer.toString();
		jsession = temp
				.substring(temp.indexOf("jsession"), temp.indexOf("\">"));

		url = new URL("http://u-campus.ajou.ac.kr/ltms/usrextend/vew.usrextend");
		urlConnection = url.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("Accept",
				"text/html, application/xhtml+xml, */*");
		httpUrlConnection.setRequestProperty("Referer",
				"http://u-campus.ajou.ac.kr/ltms/usrextend/udt.usrextend");
		httpUrlConnection.setRequestProperty("Accept-Language", "ko-KR");
		httpUrlConnection
				.setRequestProperty("User-Agent",
						"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; MASN)");
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");
		httpUrlConnection
				.setRequestProperty("Accept-Encoding", "gzip, deflate");
		httpUrlConnection.setRequestProperty("Pragma", "no-cache");
		httpUrlConnection.setRequestProperty("cookie", cookie);
		httpUrlConnection.setRequestProperty("cookie", jsession);
		
		stringBuffer = new StringBuffer();
		stringBuffer.append("result=").append(result)/* .append("&") */;

		printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "utf-8"));

		stringBuffer = new StringBuffer();
		line = new String();

		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line + "\n");

		if (intro.DEBUG)
			Log.i(intro.LOG_TAG, "내용확인3" + stringBuffer.toString());
		tagNames.clear();
		tagNames.add("font size");
		htmlElement = util.getHttpElementsMapFromString(
				stringBuffer.toString(), tagNames);
		listElement = htmlElement.get("font size");

		stringBuffer = new StringBuffer();

		for (int i = 0; i < listElement.size(); ++i) {
			CharSequence cs = listElement.get(i);
			String temp1 = cs.toString();
			stringBuffer.append(temp1);
		}
		if (intro.DEBUG)
			Log.i(intro.LOG_TAG, "파싱확인" + stringBuffer.toString());
		String temp1 = stringBuffer.toString().replaceAll("\n", "").replaceAll("\t", "").replaceAll("\r", "").replaceAll(" ", "");

		 message = temp1.substring(temp1.indexOf("</b></font>")+28, temp1.indexOf("</b>", temp1.indexOf("</font>")));
		 if (intro.DEBUG)
				Log.i(intro.LOG_TAG, "파싱확인 message = " + message);
	}

	public String getTemp() {
		return temp;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public String getSeatTime() {
		return seatTime;
	}

	public String getMessage() {
		return message;
	}

}
