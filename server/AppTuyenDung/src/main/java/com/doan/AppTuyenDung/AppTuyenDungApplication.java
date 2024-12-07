package com.doan.AppTuyenDung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableJpaRepositories(basePackages = "com.doan.AppTuyenDung")
public class AppTuyenDungApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppTuyenDungApplication.class, args);
	}
}
//lay dia chi ip thay vo lai