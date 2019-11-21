package com.HFS.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Institute {
	
	private String institute_name;
	
	@Id
	@GeneratedValue
	private long institute_code;
	
	public Institute() {
	}
	
	public Institute(String institute_name) {
		this.institute_name = institute_name;
	}
	public long getcode() {
		return this.institute_code;
	}
	public String getname() {
		return this.institute_name;
	}
}