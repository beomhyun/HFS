package com.HFS.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Institute {
	
	private String institute_name;
	@Id
	private String institute_code;
	
	public Institute() {
	}
	
	public Institute(String institute_name,String institute_code) {
		this.institute_name = institute_name;
		this.institute_code = institute_code;
	}
	public String getcode() {
		return this.institute_code;
	}
	public String getname() {
		return this.institute_name;
	}
}