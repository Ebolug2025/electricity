package com.lemonpay.lemonpayvas.electricity.redisutility;

import com.lemonpay.lemonpayvas.electricity.redisutility.redisservice.NewVasgateRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NewvasgateredisApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewvasgateredisApplication.class, args);
	}

	@Autowired
	private NewVasgateRedisService newVasgateRedisService;




}
