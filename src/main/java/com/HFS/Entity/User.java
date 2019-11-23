package com.HFS.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	private String UserID;
	
	private String PW;
	private String token;
	public User() {
	}
	
	public User(String userID, String pW, String token) {
		super();
		this.UserID = userID;
		this.PW = pW;
		this.token = token;
	}
	public User(String ID, String PW) {
		this.UserID = ID;
		this.PW = PW;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getPW() {
		return PW;
	}

	public void setPW(String pW) {
		PW = pW;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}