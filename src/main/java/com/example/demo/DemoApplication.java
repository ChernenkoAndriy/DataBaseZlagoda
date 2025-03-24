package com.example.demo;

//import database_manegment.database_entities.DatabaseConnectionPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//по суті це наш Main
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//DatabaseConnectionPool.initialize();    // це компонент для баз даних, проте він не готовий
		SpringApplication.run(DemoApplication.class, args); //запуск програми, показує першою сторінку MainView
	}

}
