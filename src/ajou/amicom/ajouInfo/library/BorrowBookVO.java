/*******************************************************************************
 * Copyright (c) 2011 A-MiCøM. All rights reserved.
 *******************************************************************************/
package ajou.amicom.ajouInfo.library;

public class BorrowBookVO {
	private String bookName = null;
	private String startDate = null;
	private String endDate = null;
	private String overDueDate = null;
	private String overDueFee = null;
	private String extendURL = null;
	private String overDueCount = null;
	
	public String getOverDueCount() {
		return overDueCount;
	}
	public void setOverDueCount(String overDueCount) {
		this.overDueCount = overDueCount;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOverDueDate() {
		return overDueDate;
	}
	public void setOverDueDate(String overDueDate) {
		this.overDueDate = overDueDate;
	}
	public String getOverDueFee() {
		return overDueFee;
	}
	public void setOverDueFee(String overDueFee) {
		this.overDueFee = overDueFee;
	}
	public String getExtendURL() {
		return extendURL;
	}
	public void setExtendURL(String extendURL) {
		this.extendURL = extendURL;
	}
	
}
