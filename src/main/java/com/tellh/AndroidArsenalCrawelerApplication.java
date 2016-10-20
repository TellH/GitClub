package com.tellh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ImportResource("DatabaseAppContext.xml")
public class AndroidArsenalCrawelerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AndroidArsenalCrawelerApplication.class, args);
	}
}
