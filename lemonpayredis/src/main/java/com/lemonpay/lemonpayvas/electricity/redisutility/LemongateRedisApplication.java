package com.lemonpay.lemonpayvas.electricity.redisutility;

import com.lemonpay.lemonpayvas.electricity.redisutility.redisservice.LemonpayRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class LemongateRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(LemongateRedisApplication.class, args);
	}

	@Autowired
	private LemonpayRedisService lemonpayRedisService;




}
