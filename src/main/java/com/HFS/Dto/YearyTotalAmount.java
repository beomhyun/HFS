package com.HFS.Dto;

public class YearyTotalAmount {
	String year;
	int totalAmount;
	public YearyTotalAmount(String year, int totalAmount) {
		super();
		this.year = year;
		this.totalAmount = totalAmount;
	}
	public YearyTotalAmount() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}
	@Override
	public String toString() {
		return "YearyTotalAmount [year=" + year + ", totalAmount=" + totalAmount + "]";
	}
}