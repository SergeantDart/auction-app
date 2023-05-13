package com.dumitrascuantonio.auctionapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.dumitrascuantonio.auctionapp"})
@EntityScan("com.dumitrascuantonio.auctionapp.entity")
@EnableJpaRepositories("com.dumitrascuantonio.auctionapp.repository")
public class AuctionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionAppApplication.class, args);
	}

}
