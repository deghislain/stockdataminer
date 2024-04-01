package com.stock.stockdataminer.processor;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

import com.stock.stockdataminer.model.DailyStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDataProcessorImpl implements AlphaVantageDataProcessor {
	@Value("${stock.symbols}")
	private String stockSymbols;
	
	@Value("${stock.endpoint}")
	private String stockEndPoint;
	
	@Value("${stock.api.key}")
	private String apiKey;

	private ConcurrentLinkedDeque<String> symbolsQueue;

	private ConcurrentLinkedDeque<DailyStockData> dsd;
	
	private ScheduledExecutorService scheduler;
	

	public AlphaVantageDataProcessorImpl() {
		this.symbolsQueue = new ConcurrentLinkedDeque<String>();
		this.dsd = new ConcurrentLinkedDeque<DailyStockData>();
		this.scheduler = Executors.newScheduledThreadPool(2);
	}

	@Override
	public void start() {
		log.info("Stock Processing Start");
		this.symbolsQueue = getStockSymblos();
		// Schedule the job to run every Month
		this.scheduler.scheduleAtFixedRate(new AlphaVantageDataRetrievalJob(this.symbolsQueue, this.dsd, this.stockEndPoint, this.apiKey), 0,
				30, TimeUnit.DAYS);
		this.scheduler.scheduleAtFixedRate(new AlphaVantageDataPersistenceJob(this.dsd), 0,
				30, TimeUnit.DAYS);
	}

	private ConcurrentLinkedDeque<String> getStockSymblos() {
		log.info("Stock Symbols {}", stockSymbols);
		if (this.stockSymbols != null) {
			String[] tokens = this.stockSymbols.split(":");

			for (String s : tokens) {
				this.symbolsQueue.add(s);
			}
		}
		return this.symbolsQueue;
	}

}
