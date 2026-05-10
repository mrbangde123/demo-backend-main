package com.hehui.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hehui.main.mapper")
public class DemoBackendMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoBackendMainApplication.class, args);
	}

}
