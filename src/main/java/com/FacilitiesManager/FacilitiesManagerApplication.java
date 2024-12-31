package com.FacilitiesManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.FacilitiesManager")
public class FacilitiesManagerApplication  {

	public static void main(String[] args) {
		SpringApplication.run(FacilitiesManagerApplication.class, args);
	}

}