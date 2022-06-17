package com.ssd.gingermarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication()
@EnableJpaAuditing
public class GingerMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(GingerMarketApplication.class, args);
	}
	
}
