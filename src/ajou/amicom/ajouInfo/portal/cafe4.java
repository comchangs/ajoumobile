/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;

import ajou.amicom.ajouInfo.*;
import android.app.*;
import android.os.*;
import android.sax.*;
import android.view.*;
import android.widget.*;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import android.widget.TabHost.TabContentFactory;
import android.view.View;

public class cafe4 extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cafe);

		HttpGet httpget = new HttpGet(
				"http://www.ajou.ac.kr/campus_life/food/today.jsp");
		DefaultHttpClient client = new DefaultHttpClient();
		StringBuilder html = new StringBuilder();

		String menulist[] = new String[50];
		String printstr = "";

		int listnum = 0;
		int linecompare = -1;

		String cafestart = "<!-- 교직원식당 테이블 시작 -->";
		String cafeend = "<!-- 교직원식당 테이블 끝 -->";
		String cafename1 = "dummy_string";
		String cafename2 = "lineend";
		String nomenu = "등록된 식단이 없습니다.";

		String breakfast = "아침";
		String lunch = "점심";
		String dinner = "저녁";
		String snack = "분식";

		try {
			HttpResponse response = client.execute(httpget);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "EUC-KR"));
			for (;;) {
				String line = br.readLine();
				String linecapy = line;

				int check1 = linecapy.indexOf(cafestart);
				if (check1 != linecompare) {
					menulist[listnum] = cafename1;
					listnum += 1;
				}

				int check3 = linecapy.indexOf(nomenu);
				if (check3 != linecompare) {
					menulist[listnum] = nomenu;
					listnum += 1;
				}

				String time1 = "/campus_life/food/img/food_tbl_fimg01.gif";
				String time2 = "/campus_life/food/img/food_tbl_fimg02.gif";
				String time3 = "/campus_life/food/img/food_tbl_fimg03.gif";
				String time4 = "/campus_life/food/img/food_tbl_fimg04.gif";

				int check5 = linecapy.indexOf(time1);
				if (check5 != linecompare) {
					menulist[listnum] = breakfast;
					listnum += 1;
				}
				int check6 = linecapy.indexOf(time2);
				if (check6 != linecompare) {
					menulist[listnum] = lunch;
					listnum += 1;
				}
				int check7 = linecapy.indexOf(time3);
				if (check7 != linecompare) {
					menulist[listnum] = dinner;
					listnum += 1;
				}
				int check8 = linecapy.indexOf(time4);
				if (check8 != linecompare) {
					menulist[listnum] = snack;
					listnum += 1;
				}

				String startmenu = "	<td style='word-break:break-all'>";
				String endmenu = "</td>";

				int check9 = linecapy.indexOf(startmenu);
				if (check9 != linecompare) {
					String menumod1 = linecapy;
					String menumod2 = menumod1.replaceAll("amp;", "");
					String menumod3 = menumod2.replaceAll("&nbsp;", "");

					String menumod4 = menumod3.replaceFirst(startmenu, "");
					String menumod5 = menumod4.replaceFirst(endmenu, "");

					menulist[listnum] = menumod5;
					listnum += 1;
				}

				int check2 = linecapy.indexOf(cafeend);
				if (check2 != linecompare) {
					menulist[listnum] = cafename2;
					listnum += 1;
					break;
				}
				html.append(line + '\n');
			}
			br.close();
		} catch (Exception e) {
			;
		}

		int allline = 0;
		int cutline = 0;
		int divline = 0;

		String menulist1 = "";
		String menulist2 = "";
		String menulist3 = "";
		String menulist4 = "";

		String menutime1 = "";
		String menutime2 = "";
		String menutime3 = "";
		String menutime4 = "";
		String nomenulist = "";

		for (allline = 0; allline < listnum; allline++) {
			if (menulist[allline] == cafename1) {
				cutline = allline + 1;
				break;
			}
		}

		for (allline = cutline; allline < listnum; allline++) {

			if (menulist[allline] == cafename2)
				break;

			if (menulist[allline] == nomenu) {
				menutime1 += nomenu;
				break;
			}

			int divcheck1 = menulist[allline].indexOf(breakfast);
			if (divcheck1 != linecompare) {
				menutime1 = "아침";
				divline = 1;
				allline += 1;
			}

			int divcheck2 = menulist[allline].indexOf(lunch);
			if (divcheck2 != linecompare) {
				menutime2 = "점심";
				divline = 2;
				allline += 1;
			}
			int divcheck3 = menulist[allline].indexOf(dinner);
			if (divcheck3 != linecompare) {
				menutime3 = "저녁";
				divline = 3;
				allline += 1;
			}
			int divcheck4 = menulist[allline].indexOf(snack);
			if (divcheck4 != linecompare) {
				menutime4 = "분식";
				divline = 4;
				allline += 1;
			}
			if (divline == 1) {
				menulist1 += menulist[allline];
			} else if (divline == 2) {
				menulist2 += menulist[allline];
			} else if (divline == 3) {
				menulist3 += menulist[allline];
			} else if (divline == 4) {
				menulist4 += menulist[allline];
			}
		}
		//printstr = menutime1 + menulist1 + menutime2 + menulist2 + menutime3
		//		+ menulist3 + menutime4 + menulist4 + nomenulist;
		//TextView result = (TextView) findViewById(R.id.TextView01);
		//result.setText(printstr);
				
		TextView result1 = (TextView) findViewById(R.id.time1);
		result1.setText(menutime1);
		
		TextView result2 = (TextView) findViewById(R.id.menu1);
		result2.setText(menulist1);
		
		TextView result3 = (TextView) findViewById(R.id.time2);
		result3.setText(menutime2);
		
		TextView result4 = (TextView) findViewById(R.id.menu2);
		result4.setText(menulist2);
		
		TextView result5 = (TextView) findViewById(R.id.time3);
		result5.setText(menutime3);
		
		TextView result6 = (TextView) findViewById(R.id.menu3);
		result6.setText(menulist3);
		
		TextView result7 = (TextView) findViewById(R.id.time4);
		result7.setText(menutime4);
		
		TextView result8 = (TextView) findViewById(R.id.menu4);
		result8.setText(menulist4);
		
		//TextView result9 = (TextView) findViewById(R.id.menu5);
		//result9.setText(nomenulist);
	}
}