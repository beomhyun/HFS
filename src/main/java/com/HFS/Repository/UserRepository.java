package com.HFS.Repository;

import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.HFS.Entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	@Query(nativeQuery = true, value = "SELECT * FROM USER  WHERE ID  LIKE :ID")
	User findbyUserID(@Param("ID") String ID);
	
	@Query(nativeQuery = true, value = "SELECT COUNT(*) AS ISLOGIN FROM USER WHERE ID LIKE :ID AND  PW LIKE :PW")
	JSONObject sginin(@Param("ID") String ID,@Param("PW") String PW);
}
