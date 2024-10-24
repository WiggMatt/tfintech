package ru.matthew;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ru.matthew.dao.repository")
@EntityScan(basePackages = "ru.matthew.dao.model")
public class SpringApplication {
	public static void main(String[] args) {
		run(SpringApplication.class, args);
	}
}
