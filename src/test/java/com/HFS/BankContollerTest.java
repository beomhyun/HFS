package com.HFS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.HFS.Entity.Bank;
import com.HFS.Entity.Institute;
import com.HFS.Repository.BankRepository;
import com.HFS.Repository.InstituteRepository;

@RunWith(JUnit4.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BankContollerTest {
	@Autowired
	private BankRepository bankrepository;
	@Autowired
	private InstituteRepository instituterepository;
	
	
	//������� ����Ʈ ����
	@BeforeEach
	public void testAloaddata1() {
//		System.out.println("���� ���� ���� ������� ����Ʈ ����");
		BufferedReader br = null;
		String line;
		String csvSplitBy = ",";
		JSONObject data = new JSONObject();

    	try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("src\\main\\resources\\data.csv"),"UTF-8"));
			line = br.readLine();
			String[] field = line.split(csvSplitBy);
			int tmp = 1001;
			for (int j = 2; j < field.length; j++) {
				// 0, 1  �� year, month
				Institute tmpist = new Institute(field[j].replaceAll("\\(���\\)",""),"bnk"+(tmp++));
				instituterepository.save(tmpist);
//				System.out.println(tmpist);
			}
			data.put("status", "sucess");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			data.put("status", "fail");
			data.put("error", e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			data.put("status", "fail");
			data.put("error", e);
		} catch (IOException e) {
			e.printStackTrace();
			data.put("status", "fail"+e);
			data.put("error", e);
		}
//    	System.out.println(data);
	}
	//������ ����
	@BeforeEach
	public void testBloaddata2() {
//		System.out.println("���ñ��� ���� ������Ȳ ������ ����");

		BufferedReader br = null;
		String line;
		String csvSplitBy = ",";
		JSONObject data = new JSONObject();
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("src\\main\\resources\\data.csv"),"UTF-8"));
			List<Institute> banklist = instituterepository.findAll();
			
			line = br.readLine();
			
			while((line = br.readLine()) != null) {
				String[] field =line.split(csvSplitBy);
				for (int j = 0; j < banklist.size(); j++) {
					//numberformatexception�� �߻�(""5") �Ͽ�  "�� ����
					Bank bnk = new Bank(field[0], field[1],banklist.get(j).getcode(), Integer.parseInt(field[j+2].replaceAll("\"", "")));
//					System.out.println(bnk);
					bankrepository.save(bnk);
				}
			}
			data.put("status", "sucess");
		} catch (Exception e) {
			System.err.println("load csv file error : " + e);
			data.clear();
			data.put("status", "fail");
			data.put("error", e);
		}
//		System.out.println(data);
	}
	
	//���ñ��� ���� �������(����) ����� ���
	@Test
	public void testCgetbanks() {
		System.out.println("���ñ��� ���� �������(����) ����� ���");
		List<String> list = new ArrayList<String>();
		for(Institute tmp : instituterepository.getInstitute()) {
			list.add(tmp.getname());
		}
		JSONObject data = new JSONObject();
		data.put("BankList",list);
		System.out.println(data);
	}
	
	//�⵵�� �� ��������� �����ݾ� �հ踦 ���
	@Test
	public void testDgetYearlyAmounts() {
		System.out.println("�⵵�� �� ��������� �����ݾ� �հ踦 ���");
		JSONObject data = new JSONObject();
		JSONArray list = new JSONArray();
		List<JSONObject> years = bankrepository.getYearlyTotalAmounts();
		for (int i = 0; i < years.size(); i++) {
			JSONObject obj = years.get(i);
			String year = obj.get("YEAR").toString();
			List<JSONObject> detailamounts = bankrepository.getYearlyDetailAmounts(year);
			List<JSONObject> addbanks = new ArrayList<JSONObject>();
			JSONObject detail_amount = new JSONObject();
			for (int j = 0; j < detailamounts.size(); j++) {
				JSONObject da = detailamounts.get(j);
				detail_amount.put(da.get("BANK"), da.get("AMOUNT"));
			}
			obj.put("detail_amount", detail_amount);
			list.add(obj);
		}
		data.put("YealyStatus", list);
		data.put("name", "���ñ��� ������Ȳ");
		System.out.println(data);
	}
	//�� �⵵�� �� ����� ��ü �����ݾ� �߿��� ���� ū �ݾ��� ������� ���
	@Test
	public void testEgetYearyMaxAmountBank() {
		System.out.println("�⵵�� �� ��������� �����ݾ� �հ踦 ���");
		JSONObject data = new JSONObject();
		JSONObject maxbank = bankrepository.getmaxamountyear();
		data.put("year",maxbank.get("YEAR"));
		data.put("bank",maxbank.get("BANK"));
		System.out.println(data);
	}
	
	//��ü �⵵(2005~2016)���� ��ȯ������ �����ݾ� ��� �߿��� ���� ���� �ݾװ� ū �ݾ��� ���
	@Test
	public void testFgetmaxminyear() {
		System.out.println("��ü �⵵(2005~2016)���� ��ȯ������ �����ݾ� ��� �߿��� ���� ���� �ݾװ� ū �ݾ��� ���");
		JSONObject data = new JSONObject();
		String searchBankName = "��ȯ����";
		JSONObject bankcode = bankrepository.getInstituteCode(searchBankName);
		String code = bankcode.get("INSTITUTE_CODE").toString();
		JSONArray list = new  JSONArray();
		JSONObject maxyear = bankrepository.getMaxAmountYearBybankcode(code);
		list.add(maxyear);
		JSONObject minyear = bankrepository.getMinAmountYearBybankcode(code);
		list.add(minyear);
		data.put("bank", searchBankName);
		data.put("support_amount", list);
		System.out.println(data);
	}
	
	//Ư�� ������ Ư�� �޿� ���ؼ� 2018�⵵ �ش� �޿� �������� �ݾ��� ����
	@Test
	public void testGpredictAmount() {
		System.out.println("Ư�� ������ Ư�� �޿� ���ؼ� 2018�⵵ �ش� �޿� �������� �ݾ��� ����");
		JSONObject input = new JSONObject();
		input.put("bank", "��������");
		input.put("month",2);
		System.out.println(input);
		int year = 2018;
		JSONObject data = new JSONObject();
		SimpleRegression simpleregression = new SimpleRegression(true);
		List<JSONObject> list = bankrepository.getAmountByBankName(input.get("bank").toString());
		double[][] inputdata = new double[list.size()][2];
		for (int i = 0; i < list.size(); i++) {
			inputdata[i][0] = Double.parseDouble(list.get(i).get("YEAR").toString())*12 + Double.parseDouble(list.get(i).get("MONTH").toString());
			inputdata[i][1] = Double.parseDouble(list.get(i).get("AMOUNT").toString());
		}
		simpleregression.addData(inputdata);
		int amount = (int) simpleregression.predict(year*12 + Double.parseDouble(input.get("month").toString()));
		data.put("bank",list.get(0).get("BANK"));
		data.put("year",year);
		data.put("month",input.get("month"));
		data.put("amount",amount);
		
		System.out.println(data);
	}
	
}
