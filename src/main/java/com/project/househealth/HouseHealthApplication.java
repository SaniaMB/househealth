package com.project.househealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HouseHealthApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseHealthApplication.class, args);
	}

}
