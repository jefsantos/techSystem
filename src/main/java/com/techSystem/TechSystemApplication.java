package com.techSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication

public class TechSystemApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(TechSystemApplication.class, args);
	}

}
