package com.etranzact.vasgate.electricity.jedc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.etranzact.vasgate.electricity.redisutility"
})
public class JedcApplication {

	public static void main(String[] args) {
		SpringApplication.run(JedcApplication.class, args);
	}

}
