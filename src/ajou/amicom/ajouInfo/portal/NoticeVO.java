/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.portal;

public class NoticeVO {
	private String url = null;
	private String title = null;
	private String ntc_seq = null;
	private String file_cnt = null;
	private String page = null;
	private String pa_no = null;
	
	public String getNtc_seq() {
		return ntc_seq;
	}
	public void setNtc_seq(String ntcSeq) {
		ntc_seq = ntcSeq;
	}
	public String getFile_cnt() {
		return file_cnt;
	}
	public void setFile_cnt(String fileCnt) {
		file_cnt = fileCnt;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPa_no() {
		return pa_no;
	}
	public void setPa_no(String paNo) {
		pa_no = paNo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
