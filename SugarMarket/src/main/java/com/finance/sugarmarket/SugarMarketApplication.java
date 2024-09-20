package com.finance.sugarmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SugarMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SugarMarketApplication.class, args);
	}

}
