package com.HFS.Controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	@RequestMapping(method = RequestMethod.GET, value ="/loadData")
	@ResponseBody
	public void loadData() {
		BufferedReader br = null;
		String line;
		String csvSplitBy = ",";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("src\\main\\resources\\data.csv"),"UTF-8"));
			List<Institute> banklist = instituterepository.findAll();
			
			line = br.readLine();
			
			while((line = br.readLine()) != null) {
				String[] field =line.split(csvSplitBy);
				for (int j = 0; j < banklist.size(); j++) {
					bankrepository.save(new Bank(field[0], field[1],banklist.get(j).getcode(), Integer.parseInt(field[j+2])));
				}
			}
		} catch (Exception e) {
			System.err.println("load csv file error : " + e);
		}
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
		data.put("주택금융 공급 금융기관",list);
		return data;
	}
}