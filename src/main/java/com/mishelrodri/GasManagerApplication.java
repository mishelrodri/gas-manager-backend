package com.mishelrodri;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GasManagerApplication {


	public static void main(String[] args) {
		SpringApplication.run(GasManagerApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(PasswordEncoder passwordEncoder){
//		return args -> {
//			System.out.println(passwordEncoder.encode("123"));
//		};
//    }

}
