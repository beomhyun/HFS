package com.HFS.Controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.HFS.Entity.Bank;
import com.HFS.Repository.BankRepository;

@Controller
public class BankController {
	@Autowired BankRepository bankrepository;
	
	@RequestMapping(method = RequestMethod.GET, value ="/loadData")
	@ResponseBody
	public void loadData() {
		BufferedReader br = null;
		String line;
		String csvSplitBy = ",";
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("src\\main\\resources\\data.csv")));
			int i =0;
			while((line = br.readLine()) != null) {
				if(i==0) {
					i++;
					continue;
				}
				String[] field =line.split(csvSplitBy);
				bankrepository.save(new Bank(field[0], field[1],"molit", field[2]));
				bankrepository.save(new Bank(field[0], field[1],"bnk3726", field[3]));
				bankrepository.save(new Bank(field[0], field[1],"bnk1829", field[4]));
				bankrepository.save(new Bank(field[0], field[1],"bnk2758", field[5]));
				bankrepository.save(new Bank(field[0], field[1],"bnk7097", field[6]));
				bankrepository.save(new Bank(field[0], field[1],"bnk4887", field[7]));
				bankrepository.save(new Bank(field[0], field[1],"bnk8621", field[8]));
				bankrepository.save(new Bank(field[0], field[1],"bnk9166", field[9]));
				bankrepository.save(new Bank(field[0], field[1],"others", field[10]));
			}
		} catch (Exception e) {
			System.err.println("load csv file error : " + e);
		}
	}
	
}