package com.HFS.Controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.HFS.Entity.Bank;
import com.HFS.Entity.Institute;
import com.HFS.Repository.BankRepository;
import com.HFS.Repository.InstituteRepository;


@Controller
public class BankController {
	@Autowired BankRepository bankrepository;
	@Autowired InstituteRepository instituterepository;
	
	//csv파일의 데이터를 읽어 데이터베이스(h2)에 저장
	@RequestMapping(method = RequestMethod.GET, value ="/public/loadData")
	@ResponseBody
	public JSONObject loadData() {
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
					//numberformatexception이 발생(""5") 하여  "를 제거
					bankrepository.save(new Bank(field[0], field[1],banklist.get(j).getcode(), Integer.parseInt(field[j+2].replaceAll("\"", ""))));
				}
			}
			data.put("status", "sucess");
		} catch (Exception e) {
			System.err.println("load csv file error : " + e);
			data.clear();
			data.put("status", "fail");
		}
		return data;
	}
	
	//주택금융 공급 금융기관(은행) 목록을 출력
	@RequestMapping(method = RequestMethod.GET, value ="/banks")
	@ResponseBody
	public JSONObject getbanks(){
		List<String> list = new ArrayList<String>();
		for(Institute tmp : instituterepository.getInstitute()) {
			list.add(tmp.getname());
		}
		JSONObject data = new JSONObject();
		data.put("BankList",list);
		return data;
	}
	
	//년도별 각 금융기관의 지원금액 합계를 출력
		@RequestMapping(method = RequestMethod.GET, value ="/amounts/banks/years")
		@ResponseBody
		public JSONObject getYearlyAmounts(){
			JSONObject data = new JSONObject();
			JSONArray list = new JSONArray();
			List<JSONObject> years = bankrepository.getYearlyTotalAmounts();
			for (int i = 0; i < years.size(); i++) {
				JSONObject obj = years.get(i);
				String year = obj.get("YEAR").toString();
				System.out.println(bankrepository.getYearlyDetailAmounts(year));
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
			data.put("name", "주택금융 공급현황");
			return data;
		}
		
		//년도별 각 금융기관의 지원금액 합계를 출력
		@RequestMapping(method = RequestMethod.GET, value ="/bank/yealymaxamount")
		@ResponseBody
		public JSONObject getYearyMaxAmountBank(){
			JSONObject data = new JSONObject();
			JSONObject maxbank = bankrepository.getmaxamountyear();
			data.put("year",maxbank.get("YEAR"));
			data.put("bank",maxbank.get("BANK"));
			return data;
		}
		
		//전체 년도(2005~2016)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력
		@RequestMapping(method = RequestMethod.GET, value ="/bank/maxminyear")
		@ResponseBody
		public JSONObject getmaxminyear(){
			JSONObject data = new JSONObject();
			String searchBankName = "외환은행";
			JSONObject bankcode = bankrepository.getInstituteCode(searchBankName);
			int code = Integer.parseInt(bankcode.get("INSTITUTE_CODE").toString());
			JSONArray list = new  JSONArray();
			JSONObject maxyear = bankrepository.getMaxAmountYearBybankcode(code);
			list.add(maxyear);
			JSONObject minyear = bankrepository.getMinAmountYearBybankcode(code);
			list.add(minyear);
			data.put("bank", searchBankName);
			data.put("support_amount", list);
			return data;
		}
		
		//특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측
		@RequestMapping(method = RequestMethod.GET, value ="/predict")
		@ResponseBody
		public JSONObject predictAmount(@RequestBody JSONObject input){
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
			
			return data;
		}
}