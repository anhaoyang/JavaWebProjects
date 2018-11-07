package com.anhaoyang.demo.ssm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SsmDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsmDemoApplication.class, args);
	}
}
