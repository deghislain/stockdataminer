package com.stock.stockdataminer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.stock.stockdataminer.processor.core.AlphaVantageCoreDataProcessor;
import com.stock.stockdataminer.processor.core.AlphaVantageCoreDataProcessorImpl;

@Configuration
public class StockDataMinerConfig {

	@Bean
    public AlphaVantageCoreDataProcessor alphaVantageDataProcessor() {
        return new AlphaVantageCoreDataProcessorImpl();
    }
}
