package com.stock.stockdataminer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.stock.stockdataminer.processor.AlphaVantageDataProcessor;

@SpringBootApplication
public class StockdataminerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockdataminerApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			AlphaVantageDataProcessor alphaDatarocessor = ctx.getBean(AlphaVantageDataProcessor.class);
			alphaDatarocessor.start();
		};
	}

}
