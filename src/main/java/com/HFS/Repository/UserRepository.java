package com.HFS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.HFS.Entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	@Query(nativeQuery = true, value = "SELECT * FROM USER  WHERE USERID  LIKE :userid")
	User findbyUserID(@Param("userid") String userid);
}
