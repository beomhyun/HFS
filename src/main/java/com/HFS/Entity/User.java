package com.HFS.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	private String ID;
	
	private String PW;
	private String token;
	public User() {
	}
	
	public User(String ID, String pW, String token) {
		super();
		this.ID = ID;
		this.PW = pW;
		this.token = token;
	}
	public User(String ID, String PW) {
		this.ID = ID;
		this.PW = PW;
	}

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getPW() {
		return PW;
	}

	public void setPW(String pW) {
		this.PW = pW;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}