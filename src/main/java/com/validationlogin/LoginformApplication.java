package com.validationlogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoginformApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginformApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner demo(UserRepo userRepository) {
		return (args) -> {
			// Add sample users for teistng
			*//*
			userRepository.save(new User("user1", "password123"));
			userRepository.save(new User("user2", "password456"));
			userRepository.save(new User("user3","password789"));*//*

			if (userRepository.findByUsername("user1") == null) {
				userRepository.save(new User("user1", "password123"));
			}
			if (userRepository.findByUsername("user2") == null) {
				userRepository.save(new User("user2", "password456"));
			}


		};*/
	//}


}
