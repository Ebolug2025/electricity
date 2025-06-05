package com.etranzact.vasgate.electricity.ikedc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.etranzact.vasgate.electricity.redisutility"
})
public class IkedcApplication {

	public static void main(String[] args) {
		SpringApplication.run(IkedcApplication.class, args);
	}

}