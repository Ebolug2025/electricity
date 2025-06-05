package com.etranzact.vasgate.electricity.kedc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.etranzact.vasgate.electricity.redisutility"
})
public class kedcApplication {

	public static void main(String[] args) {
		SpringApplication.run(kedcApplication.class, args);
	}

}
