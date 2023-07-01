package com.toolRental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.toolRental.repository") 
@ComponentScan("healthCheck")
public class ToolRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToolRentalApplication.class, args);
	}

}
