package com.HFS.Repository;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.HFS.Entity.Bank;

public interface BankRepository extends JpaRepository<Bank, String> {
	@Query(nativeQuery = true, value = "SELECT YEAR, SUM(AMOUNT) AS TOTAL_AMOUNT FROM BANK GROUP BY YEAR")
	List<JSONObject> getYearlyTotalAmounts();
	
	@Query(nativeQuery = true, value = "SELECT  I.INSTITUTE_NAME AS BANK, SUM(B.AMOUNT) AS AMOUNT FROM BANK AS B LEFT OUTER JOIN INSTITUTE AS I ON B.INSTITUTE_CODE = I.INSTITUTE_CODE WHERE YEAR LIKE :year GROUP BY B.YEAR, B.INSTITUTE_CODE")
	List<JSONObject> getYearlyDetailAmounts(@Param("year") String year);
	
	@Query(nativeQuery = true, value = "SELECT  B.YEAR, I.INSTITUTE_NAME AS BANK, SUM(B.AMOUNT) AS AMOUNT FROM BANK AS B LEFT OUTER JOIN INSTITUTE AS I ON B.INSTITUTE_CODE = I.INSTITUTE_CODE GROUP BY B.YEAR, B.INSTITUTE_CODE ORDER BY SUM(B.AMOUNT) DESC LIMIT 0,1")
	JSONObject getmaxamountyear();
	
	@Query(nativeQuery = true, value = "SELECT * FROM INSTITUTE WHERE INSTITUTE_NAME LIKE :bank")
	JSONObject getInstituteCode(@Param("bank") String bank);
	
	@Query(nativeQuery = true, value = "SELECT B.YEAR, AVG(B.AMOUNT) AS AMOUNT FROM BANK AS  B WHERE B.INSTITUTE_CODE = :bank_code GROUP BY B.YEAR ORDER BY AVG(B.AMOUNT) DESC LIMIT 0,1")
	JSONObject getMaxAmountYearBybankcode(@Param("bank_code") int bank_code);

	@Query(nativeQuery = true, value = "SELECT B.YEAR, AVG(B.AMOUNT) AS AMOUNT FROM BANK AS  B WHERE B.INSTITUTE_CODE = :bank_code GROUP BY B.YEAR ORDER BY AVG(B.AMOUNT) LIMIT 0,1")
	JSONObject getMinAmountYearBybankcode(@Param("bank_code") int bank_code);
	
	@Query(nativeQuery = true, value = "SELECT B.YEAR,B.MONTH, B.AMOUNT, I.INSTITUTE_CODE AS BANK FROM BANK AS B LEFT OUTER JOIN INSTITUTE AS I ON B.INSTITUTE_CODE = I.INSTITUTE_CODE WHERE I.INSTITUTE_NAME LIKE :bank_name")
	List<JSONObject> getAmountByBankName(@Param("bank_name") String bank_name);
	
}