package com.hackathon.Journail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hackathon.Journail.Repository", "com.hackathon.Journail.Controller", "com.hackathon.Journail.Service", "com.hackathon.Journail.Model"})
public class JournailApplication {

	public static void main(String[] args) {

		SpringApplication.run(JournailApplication.class, args);
	}

}
