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

public class BorrowBookControl {
	private String id = null;
	private String password = null;
	private String cookie = null;
	private Vector<BorrowBookVO> borrowBookVOVector = new Vector<BorrowBookVO>();
	private boolean hasBorrowBook = false;
	public BorrowBookControl(String id, String password) throws Exception{
		this.id = id;
		this.password = password;
		SearchBorrowBook();
	}
	
	public void SearchBorrowBook() throws Exception{
		HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
		Vector<String> tagNames = new Vector<String>();
		List<Element> listElement = null;
		Map<String, List<String>> setCookie = null;
		List<String> setCookieList = null;
		Util util = new Util();
		
		StringBuffer stringBuffer = new StringBuffer();
		PrintWriter printWriter = null;
		URL url = new URL("http://library.ajou.ac.kr/ksign/login_proc_20100609.asp");
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");

		stringBuffer.append("UID").append("=").append(id).append("&");
		stringBuffer.append("UPW").append("=").append(password);

		printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "EUC-KR"));
		
		stringBuffer = new StringBuffer();
		String line = new String();
		
		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);
		
		stringBuffer = new StringBuffer();
		setCookie = httpUrlConnection.getHeaderFields();
		setCookieList = setCookie.get("set-cookie");
		for(int i=0;i<setCookieList.size();++i){
			stringBuffer.append(setCookieList.get(i).substring(0, setCookieList.get(i).indexOf(";"))).append(";");
		}
		
		cookie = stringBuffer.toString();
		
		stringBuffer = new StringBuffer();
		printWriter = null;
		url = new URL("http://library.ajou.ac.kr/cache/myinfo.asp?subs=2");
		urlConnection = url.openConnection();
		httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");
		httpUrlConnection.setRequestProperty("cookie", cookie);
		
		printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "EUC-KR"));
		
		stringBuffer = new StringBuffer();
		line = new String();
		
		boolean wantedInformation = false;
		while ((line = bufferedReader.readLine()) != null){
			if(line.indexOf("대출 정보") != -1)
				wantedInformation = true;
			if(wantedInformation)
				stringBuffer.append(line);
		}
		tagNames.clear();
		tagNames.add("tr bgcolor");
		htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
		listElement = htmlElement.get("tr bgcolor");
		
		if(listElement.isEmpty()){
			hasBorrowBook = false;
			return;
		}
		else{
			hasBorrowBook = true;
		}
		
		stringBuffer = new StringBuffer();
		int size = listElement.size()-1;
		String[] text = new String[size];
		
		for(int i=1;i<listElement.size();++i){
			CharSequence cs = listElement.get(i);
			String temp1 = cs.toString();
			text[i-1] = temp1;
			stringBuffer.append(temp1).append("\n\n");
		}
	
		BorrowBookVO borrowBookVO = null;
		for(int i=0;i<size;++i){
			borrowBookVO = new BorrowBookVO();
			borrowBookVO.setBookName(text[i].substring(text[i].indexOf("\">", text[i].indexOf("a href"))+2, text[i].indexOf("</a>", text[i].indexOf("a href"))));
			borrowBookVO.setBookName(borrowBookVO.getBookName().substring(0, borrowBookVO.getBookName().indexOf("&nbsp")));
			text[i] = text[i].substring(text[i].indexOf(borrowBookVO.getBookName()));
			text[i] = text[i].substring(text[i].indexOf("font size"));
			text[i] = text[i].substring(text[i].indexOf("font size", text[i].indexOf("font size")+1));
			borrowBookVO.setStartDate(text[i].substring(text[i].indexOf("\">")+2, text[i].indexOf("</")));
			text[i] = text[i].substring(text[i].indexOf("반납예정일"));
			text[i] = text[i].substring(text[i].indexOf("font size"));
			borrowBookVO.setEndDate(text[i].substring(text[i].indexOf("\">")+2, text[i].indexOf("</")));
			text[i] = text[i].substring(text[i].indexOf("연체일"));
			text[i] = text[i].substring(text[i].indexOf("<p align"));
			borrowBookVO.setOverDueDate(text[i].substring(text[i].indexOf("\">")+2, text[i].indexOf("</")));
			text[i] = text[i].substring(text[i].indexOf("<p align", text[i].indexOf("연체료")));
			borrowBookVO.setOverDueFee(text[i].substring(text[i].indexOf("\">")+2, text[i].indexOf("</")));
			text[i] = text[i].substring(text[i].indexOf("<p align", text[i].indexOf("<p align")+1));
			borrowBookVO.setOverDueCount(text[i].substring(text[i].indexOf("\">")+2, text[i].indexOf("</")));
			borrowBookVO.setExtendURL("http://library.ajou.ac.kr/cache/"+text[i].substring(text[i].indexOf("<a href")+9, text[i].indexOf("\">", text[i].indexOf("<a href"))));
			borrowBookVOVector.add(borrowBookVO);
		}
	}
	
	public boolean isHasBorrowBook() {
		return hasBorrowBook;
	}

	public Vector<BorrowBookVO> getBorrowBookVOVector() {
		return borrowBookVOVector;
	}
	
	public String bookExtend(String strURL) throws Exception {
		HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
		Vector<String> tagNames = new Vector<String>();
		List<Element> listElement = null;
		Map<String, List<String>> setCookie = null;
		List<String> setCookieList = null;
		Util util = new Util();
		
		StringBuffer stringBuffer = new StringBuffer();
		PrintWriter printWriter = null;
		URL url = new URL(strURL);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");
		httpUrlConnection.setRequestProperty("cookie", cookie);

		printWriter = new PrintWriter(new OutputStreamWriter(
				httpUrlConnection.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
				httpUrlConnection.getInputStream(), "EUC-KR"));
		
		stringBuffer = new StringBuffer();
		String line = new String();
		
		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);
		
		String result = stringBuffer.toString();
		result = result.substring(result.indexOf("strmsg=")+7, result.lastIndexOf("찾을 수 있습니다."));
		Log.i(intro.LOG_TAG, "result = " + result);
		
		Map<String, List<String>> headers = httpUrlConnection.getHeaderFields();
		List<String> value = headers.get("location");
		
		if (intro.DEBUG)
			Log.i(intro.LOG_TAG, "location = " + value.get(0));
		
		return result;
	}
}
