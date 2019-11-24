package com.HFS.Common.JWT;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.HFS.Entity.User;
import com.HFS.Repository.UserRepository;
@Component
public class JwtInterceptor implements HandlerInterceptor {
	private static final String HEADER_AUTH = "Authorization";
	
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserRepository userrepository;
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
		System.out.println("interceptor call");
        final String token = request.getHeader(HEADER_AUTH);
        JSONObject data = new JSONObject();
        if(token.equals("Bearer Token")) {
        	User user = userrepository.findbyUserID(request.getHeader("ID"));
        	JSONObject tmp = new JSONObject();
        	tmp.put("ID", user.getID());
        	String newtoken = jwtService.makeJwt(tmp, true);
        	user.setToken(newtoken);
        	userrepository.deleteById(user.getID());
        	userrepository.save(user);
        	data.put("refreshToken",newtoken);
        	//represh token을 재 발행 했으나 보내주는 방법을 찾지 못함.
        	//가능성 session사용
        	return true;
        }else if(token != null && jwtService.checkJwt(token)){
            return true;
        }else{
            return false;        
        	}
    }
}
