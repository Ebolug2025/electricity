package com.lemonpay.lemonpayvas.electricity.eedc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.lemonpay.lemonpayvas.electricity.redisutility"
})
public class EedcApplication {

	public static void main(String[] args) {
		SpringApplication.run(EedcApplication.class, args);
	}

}
