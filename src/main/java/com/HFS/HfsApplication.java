package com.HFS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.HFS.Common.JWT.JwtInterceptor;
import com.HFS.Entity.Institute;
import com.HFS.Repository.InstituteRepository;

@SpringBootApplication
@EntityScan
@EnableJpaRepositories
@ComponentScan
@Configuration
public class HfsApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(HfsApplication.class, args);
	}
	
	@Bean
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/console/*");
	    return registration;
	}
	
//	@Bean
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(jwtInterceptor)
//				.addPathPatterns("/*")
//				.excludePathPatterns("/user/*");
//	}	
	
	@Bean
    public CommandLineRunner initData(InstituteRepository repositry) {
        return (args) -> {
    		BufferedReader br = null;
    		String line;
    		String csvSplitBy = ",";
        	br = new BufferedReader(new InputStreamReader(new FileInputStream("src\\main\\resources\\data.csv"),"UTF-8"));
			
			line = br.readLine();
			String[] field = line.split(csvSplitBy);
			int tmp = 1001;
			for (int j = 2; j < field.length; j++) {
				// 0, 1  Àº year, month
				repositry.save(new Institute(field[j].replaceAll("\\(¾ï¿ø\\)",""),"bnk"+(tmp++)));
			}
        };
    }

}
