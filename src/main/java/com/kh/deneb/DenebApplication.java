package com.kh.deneb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.kh.deneb")
public class DenebApplication {

	public static void main(String[] args) {
		SpringApplication.run(DenebApplication.class, args);
	}

}
