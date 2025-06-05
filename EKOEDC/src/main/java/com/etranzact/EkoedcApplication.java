package com.etranzact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.etranzact.vasgate.electricity.redisutility"
})
public class EkoedcApplication {

	public static void main(String[] args) {
		SpringApplication.run(EkoedcApplication.class, args);

		////postpaid query request: {"reference":"01NK52840B05903UZ7Z8","amount":0,"fee":0,"alias":"phcnppikj","action":"query","account":"12345678910","type":"6","type2":"05","ip":"172.16.10.38","date":"Mar 10, 2025 12:54:31 AM"}
		////prepaid process request: {"reference":"02USDWP99727470050h7","amount":1000,"fee":0,"alias":"phcnppikj","action":"process","account":"12345678910","mobile":"2347039232333","merchant":"73QABZ-TG5","name":"FLORENCE OKNKWO","bank":"214","type":"4","type2":"02","mac":"88da7c66e1d69a2e7619eede2075db714972e1077e5df84c17a3d6794fee31ef","ip":"172.16.10.38","client":"USSD","otherinfo":"","date":"Mar 10, 2025 1:00:23 AM"}
	}

}
