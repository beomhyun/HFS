package com.HFS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.HFS.Entity.Institute;

public interface InstituteRepository extends JpaRepository<Institute, String> {
	@Query(nativeQuery = true, value = "SELECT * FROM INSTITUTE")
	List<Institute> getInstitute();
}
