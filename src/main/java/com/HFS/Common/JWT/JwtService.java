package com.HFS.Common.JWT;

import org.json.simple.JSONObject;

public interface JwtService {

	boolean checkJwt(String jwt) throws Exception;
	String makeJwt(JSONObject user, boolean isrefreshtoken) throws Exception;
	
}
