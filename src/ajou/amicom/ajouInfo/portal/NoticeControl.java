/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

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
import ajou.amicom.ajouInfo.main.*;
import android.util.Log;

public class NoticeControl {
	private String id = null;
	private String pw = null;
	private String studentId = null;
	Vector<NoticeVO> noticeVector = new Vector<NoticeVO>();
	
	public NoticeControl(String id, String pw, String studentId) throws Exception{
		this.id = id;
		this.pw = pw;
		this.studentId = studentId;
		getNotice();
	}

	public void getNotice() throws Exception {
		
		URL url = null;
		StringBuffer stringBuffer = new StringBuffer();
		PrintWriter printWriter = null;
		List<String> value = null;
		Map<String, List<String>> headers = null;
		String cookie = null;
		HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
		Vector<String> tagNames = new Vector<String>();
		List<Element> listElement = null;
		Util util = new Util();
		/**
		 *  기본 쿠키 가져오기
		 */
		url = new URL(StringURL.login_url);
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
		stringBuffer.append("j_password").append("=").append(pw);

		printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
				.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		new BufferedReader(new InputStreamReader(httpUrlConnection
				.getInputStream(), "EUC-KR"));
		headers = httpUrlConnection.getHeaderFields();
		value = headers.get("location");
		cookie = value.get(0).substring(value.get(0).lastIndexOf(";") + 1);
		
		headers = httpUrlConnection.getHeaderFields();
		value = headers.get("set-cookie");
		String ssoportal = value.get(0);
		String ssotoken = value.get(1);
		ssoportal = ssoportal.substring(0, ssoportal.indexOf(";"));
		ssotoken = ssotoken.substring(0, ssotoken.indexOf(";"));
		cookie = cookie + ";" + ssoportal;
		cookie = cookie + ";" + ssotoken + ";";
			/* 기본 쿠키 부분  끝*/
		
			/*공지사항 가져 오기*/
		String stringURL = StringURL.notice01+id+StringURL.notice02+studentId;
		
		url = new URL(stringURL);
		
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
		stringBuffer.append("j_password").append("=").append(pw);

		printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
				.getOutputStream(), "EUC-KR"));
		printWriter.write(stringBuffer.toString());
		printWriter.flush();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection
				.getInputStream(), "EUC-KR"));
		
		stringBuffer = new StringBuffer();
		String line = new String();
		
		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);
		
		tagNames.clear();
		tagNames.add("div");
		htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
		listElement = htmlElement.get("div");
		
		CharSequence cs = listElement.get(0);
		
		tagNames.clear();
		tagNames.add("a href");
		htmlElement = util.getHttpElementsMapFromString(cs.toString(), tagNames);
		listElement = htmlElement.get("a href");
		
		stringBuffer = new StringBuffer();
		Log.i("Test", ""+listElement.size());
		for(int i=1;i<listElement.size();++i){
			cs = listElement.get(i);
			String temp2 = cs.toString();
			NoticeVO vo = parsing(temp2);
			noticeVector.add(vo);
		} //1부터 해야함 notice의 list를 불러오는 코드를 삭제함
	}
	
	public NoticeVO parsing(String str){
		NoticeVO vo = new NoticeVO();
		vo.setTitle(str.substring(str.indexOf("\">")+2, str.indexOf("</a>")));
		
		str= str.substring(str.indexOf("onClick"), str.indexOf("class"));
		vo.setNtc_seq(str.substring(str.indexOf("('")+2, str.indexOf("',", str.indexOf("('"))));
		str = str.substring(str.indexOf(",'")+1);
		vo.setFile_cnt(str.substring(str.indexOf("'")+1, str.indexOf("',")));
		if(vo.getFile_cnt() == null)
			vo.setFile_cnt("0");
		str = str.substring(str.indexOf(",'")+1);
		vo.setPage(str.substring(str.indexOf("'")+1, str.indexOf("',")));
		str = str.substring(str.indexOf(",'")+1);
		vo.setPa_no(str.substring(str.indexOf("'")+1, str.indexOf("')")));
		return vo;
	}

	public Vector<NoticeVO> getNoticeVector() {
		return noticeVector;
	}
}
