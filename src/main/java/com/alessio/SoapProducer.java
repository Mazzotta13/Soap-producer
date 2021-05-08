package com.alessio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SoapProducer {
	public static void main(String[] args) {
		// go to: http://localhost:8080/services/
		SpringApplication.run(SoapProducer.class, args);
	}
}
