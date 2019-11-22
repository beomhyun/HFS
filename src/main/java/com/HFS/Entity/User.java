package com.HFS.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	private String UserID;
	
	private String PW;
	
	public User() {
	}
	
	public User(String ID, String PW) {
		this.UserID = ID;
		this.PW = PW;
	}
}