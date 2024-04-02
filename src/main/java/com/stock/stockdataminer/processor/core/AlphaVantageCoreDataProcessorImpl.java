package com.stock.stockdataminer.processor.core;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

import com.stock.stockdataminer.model.CoreStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageCoreDataProcessorImpl implements AlphaVantageCoreDataProcessor {
	@Value("${stock.symbols}")
	private String stockSymbols;
	
	@Value("${stock.daily.endpoint}")
	private String stockDailyEndPoint;
	
	@Value("${stock.weekly.endpoint}")
	private String stockWeeklyEndPoint;
	
	@Value("${stock.api.key}")
	private String apiKey;
	
	@Value("${stock.initial.download}")
	private String stockInitialDownload;
	

	private ConcurrentLinkedDeque<String> symbolsQueue;

	private ConcurrentLinkedDeque<CoreStockData> dsd;
	
	private ScheduledExecutorService scheduler;
	

	public AlphaVantageCoreDataProcessorImpl() {
		this.symbolsQueue = new ConcurrentLinkedDeque<String>();
		this.dsd = new ConcurrentLinkedDeque<CoreStockData>();
		this.scheduler = Executors.newScheduledThreadPool(2);
	}

	@Override
	public void start() {
		log.info("Stock Processing Start");
		this.symbolsQueue = getStockSymblos();
		// Schedule the job to run every Month
		this.scheduler.scheduleAtFixedRate(new AlphaVantageCoreDataRetrievalJob(this.symbolsQueue, this.dsd, this.stockDailyEndPoint, this.stockWeeklyEndPoint, this.apiKey, this.stockInitialDownload), 0,
				30, TimeUnit.DAYS);
		this.scheduler.scheduleAtFixedRate(new AlphaVantageCoreDataPersistenceJob(this.dsd), 0,
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
