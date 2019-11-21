package com.HFS;

import org.h2.server.web.WebServlet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.HFS.Entity.Institute;
import com.HFS.Repository.InstituteRepository;

@SpringBootApplication
@EntityScan
@EnableJpaRepositories
@ComponentScan
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

	@Bean
    public CommandLineRunner initData(InstituteRepository repository) {
        return (args) -> {
            repository.save(new Institute("���õ��ñ��","molit"));
            repository.save(new Institute("��������","bnk3726"));
            repository.save(new Institute("�츮����","bnk1829"));
            repository.save(new Institute("��������","bnk2758"));
            repository.save(new Institute("�ѱ���Ƽ����","bnk7097"));
            repository.save(new Institute("�ϳ�����","bnk4887"));
            repository.save(new Institute("��������","bnk8621"));
            repository.save(new Institute("��ȯ����","bnk9166"));
            repository.save(new Institute("��Ÿ����","others"));
        };
    }

}
