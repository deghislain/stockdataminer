package com.stock.stockdataminer.processor.core;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.stock.stockdataminer.dao.AlphaVantageCoreStockDAO;
import com.stock.stockdataminer.model.CoreStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageCoreDataPersistenceJob implements Runnable{
	private ConcurrentLinkedDeque<CoreStockData> dailyStockDataQueue;
	
	private AlphaVantageCoreStockDAO dao;

	public AlphaVantageCoreDataPersistenceJob(ConcurrentLinkedDeque<CoreStockData> dsdq) {
		this.dailyStockDataQueue = dsdq;
		this.dao = new AlphaVantageCoreStockDAO();
	}

	@Override
	public void run() {
		log.info("Persisisting Job Started");
		try {
			Thread.sleep(15000);
			while (this.dailyStockDataQueue.size() > 0) {
				log.info("dailyStockDataQueue {}", dailyStockDataQueue.size());
				saveAlphaVantageCoreStock(this.dailyStockDataQueue.pollFirst());
			}
			log.info("Persisisting Job Ended");
		} catch (InterruptedException e) {
			log.error("Error while persisting stock data {}", e);
		}
	}
	
	private void saveAlphaVantageCoreStock(CoreStockData dsd) {
		log.info(" saveAlphaVantageDailyStock Start {}", dsd);
	
		dao.saveCoreStock(dsd);
		
		log.info(" saveAlphaVantageDailyStock End {}");
	}

}
