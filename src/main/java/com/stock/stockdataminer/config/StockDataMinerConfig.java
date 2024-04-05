package com.stock.stockdataminer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stock.stockdataminer.processor.AlphaVantageDataProcessor;
import com.stock.stockdataminer.processor.AlphaVantageDataProcessorImpl;

@Configuration
public class StockDataMinerConfig {

	@Bean
    public AlphaVantageDataProcessor alphaVantageDataProcessor() {
        return new AlphaVantageDataProcessorImpl();
    }
}
