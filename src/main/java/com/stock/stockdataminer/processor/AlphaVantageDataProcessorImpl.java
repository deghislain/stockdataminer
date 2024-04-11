package com.stock.stockdataminer.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;

import com.stock.stockdataminer.model.CoreStockData;
import com.stock.stockdataminer.model.FundStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDataProcessorImpl implements AlphaVantageDataProcessor {
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
	
	@Value("${stock.income.endpoint}")
	private String stockIncomeEndpoint;
	
	@Value("${stock.balance.endpoint}")
	private String balanceSheetEndpoint;
	
	@Value("${stock.cash.endpoint}")
	private String cashFlowEndpoint;
	
	@Value("${stock.earnings.endpoint}")
	private String stockEarningsEndpoint;
	
	
	private Map<String, String>endPointsMap;
	

	private ConcurrentLinkedDeque<String> symbolsQueue;

	private ConcurrentLinkedDeque<CoreStockData> coreQueue;
	
	private ConcurrentLinkedDeque<FundStockData> fundQueue;
	
	private ScheduledExecutorService scheduler;
	

	public AlphaVantageDataProcessorImpl() {
		this.symbolsQueue = new ConcurrentLinkedDeque<String>();
		this.coreQueue = new ConcurrentLinkedDeque<CoreStockData>();
		this.fundQueue = new ConcurrentLinkedDeque<FundStockData>();
		this.scheduler = Executors.newScheduledThreadPool(3);
		this.endPointsMap = new HashMap<String, String>();
	}

	@Override
	public void start() {
		log.info("Stock Processing Start");
		this.symbolsQueue = getStockSymblos();
		this.endPointsMap = getStockEndPoints();
		// Schedule the job to run every Month
		this.scheduler.scheduleAtFixedRate(new AlphaVantageDataRetrievalJob(this.symbolsQueue, this.coreQueue, this.fundQueue, this.endPointsMap, this.apiKey, this.stockInitialDownload), 0,
				30, TimeUnit.DAYS);
		
		this.scheduler.scheduleAtFixedRate(new AlphaVantageDataPersistenceJob(this.coreQueue,this.fundQueue), 0,
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
	
	private Map<String, String> getStockEndPoints() {
		log.info("Stock endpoints {}", this.endPointsMap);
		this.endPointsMap.put("stockDailyEndPoint", this.stockDailyEndPoint);
		this.endPointsMap.put("stockWeeklyEndPoint", this.stockWeeklyEndPoint);
		this.endPointsMap.put("stockIncomeEndpoint", this.stockIncomeEndpoint);
		this.endPointsMap.put("balanceSheetEndpoint", this.balanceSheetEndpoint);
		this.endPointsMap.put("cashFlowEndpoint", this.cashFlowEndpoint);
		this.endPointsMap.put("stockEarningsEndpoint", this.stockEarningsEndpoint);
		return this.endPointsMap;
	}

}
