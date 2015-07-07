/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.main;

public class StringURL {
	public static final String login_url = "http://portal.ajou.ac.kr/j_aims_security_check.action";//login
	public static final String findStudentId = "http://portal.ajou.ac.kr/portal/findPortalMenuByRole.action";//상단 메뉴 URL(마이포탈 학사(학부)....
	public static final String notice01 = "http://www.ajou.ac.kr/portal/NoticeNews.jsp?userid=";//notice 접근 url 01과 02가 합쳐져야 함
	public static final String notice02 = "&plocale=ko_KR&purl=http://portal.ajou.ac.kr/public/portal/portlet/redirectPortlet.jsp&prole=SS0001&userno=";//notice 접근 url 01과 02가 합쳐져야 함
	//notice file 세부 사항 03, 04, 05, 06이 합쳐져야 함
	public static final String notice03 = "http://www.ajou.ac.kr/servlets/ajouweb.notice.servlet.NoticeListServlet?unit=M&portal=1&mode=MasterselectView&ntc_seq_no=";
	public static final String notice04 = "&page_num=";
	public static final String notice05 = "&page_no=";
	public static final String notice06 = "&atach_file_cnt=";
	
	public static final String schedule01 = "http://byuldang.ajou.ac.kr/asc/daehakwon/d_haksa/d_haksailjung/d_haksailjung_s0.jsp?iljung_year=2011&iljung_nm=11&daehak_nm=%C7%D0%BB%E7"; //2011학년도 학사 일정
	public static final String siteMap = "http://www.ajou.ac.kr/intro/ajou/campus_map1.html";
	
	public static final String restaurant01 = "http://www.ajou.ac.kr/portal/TodayMenu.jsp?plocale=ko_KR&purl=http://portal.ajou.ac.kr/public/portal/portlet/redirectPortlet.jsp";
	
	public static final String readingRoom01 = "http://u-campus.ajou.ac.kr/ltms/rmstatus/vew.rmstatus?bd_code=JL&rm_code=undefined"; //초기 url
	public static final String readingRoom02 = "http://u-campus.ajou.ac.kr/ltms/rmstatus/vew.rmstatus"; //선택 시 url
	public static final String readingRoom03 = "http://u-campus.ajou.ac.kr/ltms/temp/231.png"; //B1 그림
	public static final String readingRoom04 = "http://u-campus.ajou.ac.kr/ltms/temp/241.png"; //C1 그림
	public static final String readingRoom05 = "http://u-campus.ajou.ac.kr/ltms/temp/251.png"; //D1 그림
	public static final String readingRoom06 = "http://u-campus.ajou.ac.kr/ltms/temp/261.png"; //D1 그림
}
