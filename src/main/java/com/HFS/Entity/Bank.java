package com.HFS.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bank {
	
	@Id
	@GeneratedValue
	private int id;
	private String year;
	private String month;
	private String institute_code;
	private String amount;
	
	public Bank() {
	}
	
	public Bank(String year, String month, String institute_code, String amount) {
		this.year = year;
		this.month = month;
		this.institute_code = institute_code;
		this.amount = amount;
	}
}