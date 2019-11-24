package com.HFS;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.HFS.Common.JWT.JwtService;
import com.HFS.Entity.User;
import com.HFS.Repository.UserRepository;

@RunWith(JUnit4.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private JwtService jwtservice;
	
	@Test
	public void test01signup() {
		System.out.println("SIGNUP 테스트");
		JSONObject user = new JSONObject();
		user.put("ID", "beomhyun");
		user.put("PW","1234");
		JSONObject data = new JSONObject();
		String pw = user.get("PW").toString();
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		    digest.reset();
		    digest.update(pw.getBytes("utf8"));
		    pw = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String token = null;
		try {
			token = jwtservice.makeJwt(user,true);
			data.put("status", "sucess");
		} catch (Exception e) {
			e.printStackTrace();
			data.put("status", "fail");
			data.put("error", e);
		}
		
		User signupuser = new User(user.get("ID").toString(),pw,token);
		userrepository.save(signupuser);
		data.put("refreshToken", token);
		
		System.out.println(data);
	}
	
	@Test
	public void test02signin() {
		System.out.println("SIGNIN 테스트");
		JSONObject user = new JSONObject();
		user.put("ID", "beomhyun");
		user.put("PW","1234");
		String pw = user.get("PW").toString();
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		    digest.reset();
		    digest.update(pw.getBytes("utf8"));
		    pw = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject logincheck = userrepository.sginin(user.get("ID").toString(), pw);
		JSONObject data = new JSONObject();
		String token = null;
		if(Integer.parseInt(logincheck.get("ISLOGIN").toString()) == 1) {
			try {
				token = jwtservice.makeJwt(user, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			data.put("accessToken", token);
			System.out.println(data);
		}else {
			data.put("status", "fail");
			System.out.println(data);
		}
	}
}
