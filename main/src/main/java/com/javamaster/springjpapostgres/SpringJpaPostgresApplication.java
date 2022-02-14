package com.javamaster.springjpapostgres;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication()
public class SpringJpaPostgresApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaPostgresApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	private void ready(){
		System.out.println("ready");
	}
}