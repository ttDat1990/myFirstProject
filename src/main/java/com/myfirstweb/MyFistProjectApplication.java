package com.myfirstweb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.myfirstweb.config.StorageProperties;
import com.myfirstweb.service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MyFistProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyFistProjectApplication.class, args);
	}
	
	@Bean
	CommandLineRunner init(StorageService storageService) { // gọi init của storage service để tạo thư mục
		return (args->{
			storageService.init();
		});
	}
}
