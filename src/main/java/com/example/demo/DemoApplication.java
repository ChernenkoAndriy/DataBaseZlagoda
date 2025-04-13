package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//по суті це наш Main
@SpringBootApplication(scanBasePackages = "com.example.demo")
public class DemoApplication {

	public static void main(String[] args) {
				SpringApplication.run(DemoApplication.class, args); //запуск програми, показує першою сторінку LoginView
	}

}
