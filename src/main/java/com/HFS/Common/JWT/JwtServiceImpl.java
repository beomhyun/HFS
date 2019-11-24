package com.HFS.Common.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtServiceImpl implements JwtService {
	private String secretKey = "mySeceretKeyforHFS";


    @Override
    public String makeJwt(JSONObject user, boolean isrefreshtoken) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date expireTime = new Date();
        if(isrefreshtoken) {
        	expireTime.setTime(expireTime.getTime() + 1000 * 3600 * 24 * 7);
        }else {
        	expireTime.setTime(expireTime.getTime() + 1000 * 1800 * 1);
        }
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("typ","JWT");
        headerMap.put("alg","HS256");

        Map<String, Object> map= new HashMap<String, Object>();

        String userid = user.get("ID").toString();

        map.put("UserID", userid);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    @Override
    public boolean checkJwt(String jwt) throws Exception {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .parseClaimsJws(jwt).getBody(); // 정상 수행된다면 해당 토큰은 정상토큰
            return true;
        } catch (ExpiredJwtException exception) {
            //토큰만료
        	return false;
        } catch (JwtException exception) {
        	//토큰변조
            return false;
        }
    }


}
