package com.kh.pickpack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.kh.pickpack")
public class PickpackApplication {

	public static void main(String[] args) {
		SpringApplication.run(PickpackApplication.class, args);
	}

}
