package com.project.fooddeliverysystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodDeliverySystemApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.location","application.properties");
		SpringApplication.run(FoodDeliverySystemApplication.class, args);
	}

}
