package com.etranzact.vasgate.electricity.eedc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.etranzact.vasgate.electricity.redisutility"
})
public class EedcApplication {

	public static void main(String[] args) {
		SpringApplication.run(EedcApplication.class, args);
	}

}
