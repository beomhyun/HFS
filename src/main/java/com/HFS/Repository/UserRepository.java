package com.HFS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HFS.Entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
