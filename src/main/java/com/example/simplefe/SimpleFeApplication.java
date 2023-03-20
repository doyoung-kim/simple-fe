package com.example.simplefe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SimpleFeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleFeApplication.class, args);
	}

}
