package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import com.example.demo.resources.DestRepository;

@SpringBootApplication
public class DestinationMicroApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DestinationMicroApplication.class, args);
	}

}

//Destination Service(CRUD) manages destination information
	/*
	 * destId
	 * place
	 * country
	 * latitude
	 * longitude
	 * info
	 * image
	 */
