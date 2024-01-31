package com.charlie.challenge.urbieta.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

import com.charlie.challenge.urbieta.ServiceApplication;

@TestConfiguration(proxyBeanMethods = false)
public class TestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ServiceApplication::main).with(TestServiceApplication.class).run(args);
	}

}
