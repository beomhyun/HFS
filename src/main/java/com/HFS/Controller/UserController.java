package com.HFS.Controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.HFS.Entity.User;
import com.HFS.Common.JWT.JwtService;
import com.HFS.Repository.UserRepository;

@Controller
public class UserController {
	@Autowired
	private UserRepository userrepository;
	@Autowired
	private JwtService jwtService;
	
	//singup 계정생성 api
	@RequestMapping(method = RequestMethod.POST, value ="/public/user")
	@ResponseBody
	public JSONObject signup(@RequestBody JSONObject user) {
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
			token = jwtService.makeJwt(user,true);
			System.out.println(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User signupuser = new User(user.get("USERID").toString(),pw,token);
		userrepository.save(signupuser);
		data.put("refreshToken", token);
		return data;
	}
	//signin 로그인 api
	@RequestMapping(method = RequestMethod.GET, value ="/public/signin")
	@ResponseBody
	public JSONObject signin(@RequestBody JSONObject user) {
		JSONObject data = new JSONObject();
		String token = null;
		try {
			token = jwtService.makeJwt(user, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.put("accessToken", token);
		return data;
	}
}