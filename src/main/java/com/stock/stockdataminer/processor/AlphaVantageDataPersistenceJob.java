package com.stock.stockdataminer.processor;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.stock.stockdataminer.dao.AlphaVantageDailyStockDAO;
import com.stock.stockdataminer.model.DailyStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDataPersistenceJob implements Runnable{
	private ConcurrentLinkedDeque<DailyStockData> dailyStockDataQueue;
	
	private AlphaVantageDailyStockDAO dao;

	public AlphaVantageDataPersistenceJob(ConcurrentLinkedDeque<DailyStockData> dsdq) {
		this.dailyStockDataQueue = dsdq;
		this.dao = new AlphaVantageDailyStockDAO();
	}

	@Override
	public void run() {
		log.info("Persisisting Job Started");
		try {
			Thread.sleep(15000);
			while (this.dailyStockDataQueue.size() > 0) {
				log.info("dailyStockDataQueue {}", dailyStockDataQueue.size());
				saveAlphaVantageDailyStock(this.dailyStockDataQueue.pollFirst());
			}
			log.info("Persisisting Job Ended");
		} catch (InterruptedException e) {
			log.error("Error while persisting stock data {}", e);
		}
	}
	
	private void saveAlphaVantageDailyStock(DailyStockData dsd) {
		log.info(" saveAlphaVantageDailyStock Start {}", dsd);
	
		dao.saveDailyStock(dsd);
		
		log.info(" saveAlphaVantageDailyStock End {}");
	}

}
