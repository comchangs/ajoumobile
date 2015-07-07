/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import net.htmlparser.jericho.Element;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ajou.amicom.ajouInfo.*;
import ajou.amicom.ajouInfo.R.*;
import ajou.amicom.ajouInfo.main.*;

public class ReadingRoomConditionControl {
	private ReadingRoomVO b1 = new ReadingRoomVO();
	private ReadingRoomVO c1 = new ReadingRoomVO();
	private ReadingRoomVO c2 = new ReadingRoomVO();
	private ReadingRoomVO d1 = new ReadingRoomVO();
	
	public ReadingRoomConditionControl() throws Exception{
		
		StringBuffer stringBuffer = new StringBuffer();
		Vector<String> tagNames = new Vector<String>();
		List<Element> listElement = null;
		HashMap<String, List<Element>> htmlElement = new HashMap<String, List<Element>>();
		Util util = new Util();
		
		URL url = new URL(StringURL.readingRoom01);
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;

		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setDefaultUseCaches(false);
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setInstanceFollowRedirects(false);
		httpUrlConnection.setRequestProperty("content-type",
				"application/x-www-form-urlencoded");
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection
				.getInputStream(), "EUC-KR"));
		
		stringBuffer = new StringBuffer();
		String line = new String();
		
		while ((line = bufferedReader.readLine()) != null)
			stringBuffer.append(line);
		
		tagNames.clear();
		tagNames.add("option value");
		htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
		listElement = htmlElement.get("option value");
		
		stringBuffer = new StringBuffer();
		
		for(int i=1;i<listElement.size();++i){
			CharSequence cs = listElement.get(i);
			String temp2 = cs.toString();
			stringBuffer.append(temp2+"\n");
		}
		String temp = stringBuffer.toString();
		
		if(temp.indexOf("B1") != -1){
			b1.setOpen(true);
			url = new URL(StringURL.readingRoom02);
			urlConnection = url.openConnection();
			httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setDefaultUseCaches(false);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setInstanceFollowRedirects(false);
			httpUrlConnection.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			stringBuffer = new StringBuffer();
			
			stringBuffer.append("bd_code=JL&rm_code=JL0B1");
			
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
					.getOutputStream(), "EUC-KR"));
			printWriter.write(stringBuffer.toString());
			printWriter.flush();

			bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection
					.getInputStream(), "EUC-KR"));
			
			stringBuffer = new StringBuffer();
			line = new String();
			
			while ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);
			
			
			tagNames.clear();
			tagNames.add("font color");
			htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
			listElement = htmlElement.get("font color");
			
			CharSequence cs = listElement.get(1);
			String number = cs.toString();
			number = number.substring(number.indexOf("\">")+2, number.indexOf("</"));
			b1.setRemain(number);
		}
		
		if(temp.indexOf("B1") != -1){
			b1.setOpen(true);
			url = new URL(StringURL.readingRoom02);
			urlConnection = url.openConnection();
			httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setDefaultUseCaches(false);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setInstanceFollowRedirects(false);
			httpUrlConnection.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			stringBuffer = new StringBuffer();
			
			stringBuffer.append("bd_code=JL&rm_code=JL0B1");
			
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
					.getOutputStream(), "EUC-KR"));
			printWriter.write(stringBuffer.toString());
			printWriter.flush();

			bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection
					.getInputStream(), "EUC-KR"));
			
			stringBuffer = new StringBuffer();
			line = new String();
			
			while ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);
			
			
			tagNames.clear();
			tagNames.add("font color");
			htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
			listElement = htmlElement.get("font color");
			
			CharSequence cs = listElement.get(1);
			String number = cs.toString();
			number = number.substring(number.indexOf("\">")+2, number.indexOf("</"));
			b1.setRemain(number);
		}
		
		if(temp.indexOf("D1") != -1){
			d1.setOpen(true);
			url = new URL(StringURL.readingRoom02);
			urlConnection = url.openConnection();
			httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setDefaultUseCaches(false);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setInstanceFollowRedirects(false);
			httpUrlConnection.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			stringBuffer = new StringBuffer();
			
			stringBuffer.append("bd_code=JL&rm_code=JL0D1");
			
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
					.getOutputStream(), "EUC-KR"));
			printWriter.write(stringBuffer.toString());
			printWriter.flush();

			bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection
					.getInputStream(), "EUC-KR"));
			
			stringBuffer = new StringBuffer();
			line = new String();
			
			while ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);
			
			
			tagNames.clear();
			tagNames.add("font color");
			htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
			listElement = htmlElement.get("font color");
			
			CharSequence cs = listElement.get(1);
			String number = cs.toString();
			number = number.substring(number.indexOf("\">")+2, number.indexOf("</"));
			d1.setRemain(number);
		}
		
		if(temp.indexOf("C1") != -1){
			c1.setOpen(true);
			url = new URL(StringURL.readingRoom02);
			urlConnection = url.openConnection();
			httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setDefaultUseCaches(false);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setInstanceFollowRedirects(false);
			httpUrlConnection.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			stringBuffer = new StringBuffer();
			
			stringBuffer.append("bd_code=JL&rm_code=JL0C1");
			
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
					.getOutputStream(), "EUC-KR"));
			printWriter.write(stringBuffer.toString());
			printWriter.flush();

			bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection
					.getInputStream(), "EUC-KR"));
			
			stringBuffer = new StringBuffer();
			line = new String();
			
			while ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);
			
			
			tagNames.clear();
			tagNames.add("font color");
			htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
			listElement = htmlElement.get("font color");
			
			CharSequence cs = listElement.get(1);
			String number = cs.toString();
			number = number.substring(number.indexOf("\">")+2, number.indexOf("</"));
			c1.setRemain(number);
		}
		
		if(temp.indexOf("C2") != -1){
			c2.setOpen(true);
			url = new URL(StringURL.readingRoom02);
			urlConnection = url.openConnection();
			httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setDefaultUseCaches(false);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setInstanceFollowRedirects(false);
			httpUrlConnection.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");
			stringBuffer = new StringBuffer();
			
			stringBuffer.append("bd_code=JL&rm_code=JL0C2");
			
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(httpUrlConnection
					.getOutputStream(), "EUC-KR"));
			printWriter.write(stringBuffer.toString());
			printWriter.flush();

			bufferedReader = new BufferedReader(new InputStreamReader(httpUrlConnection
					.getInputStream(), "EUC-KR"));
			
			stringBuffer = new StringBuffer();
			line = new String();
			
			while ((line = bufferedReader.readLine()) != null)
				stringBuffer.append(line);
			
			
			tagNames.clear();
			tagNames.add("font color");
			htmlElement = util.getHttpElementsMapFromString(stringBuffer.toString(), tagNames);
			listElement = htmlElement.get("font color");
			
			CharSequence cs = listElement.get(1);
			String number = cs.toString();
			number = number.substring(number.indexOf("\">")+2, number.indexOf("</"));
			c2.setRemain(number);
		}
	}
	
	public Bitmap getURLImage(String stringURL){
		Bitmap bm = null;
		try{
			URL imageURL = new URL(stringURL);
			HttpURLConnection conn = (HttpURLConnection)imageURL.openConnection();             
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 10240);
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
		}catch(Exception e){}
		return bm;
	}
	public ReadingRoomVO getB1() {
		return b1;
	}

	public void setB1(ReadingRoomVO b1) {
		this.b1 = b1;
	}

	public ReadingRoomVO getC1() {
		return c1;
	}

	public void setC1(ReadingRoomVO c1) {
		this.c1 = c1;
	}

	public ReadingRoomVO getC2() {
		return c2;
	}

	public void setC2(ReadingRoomVO c2) {
		this.c2 = c2;
	}

	public ReadingRoomVO getD1() {
		return d1;
	}

	public void setD1(ReadingRoomVO d1) {
		this.d1 = d1;
	}
}
