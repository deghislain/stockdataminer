package com.stock.stockdataminer.processor;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.stock.stockdataminer.dao.AlphaVantageCoreStockDAO;
import com.stock.stockdataminer.model.CoreStockData;
import com.stock.stockdataminer.model.FundStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDataPersistenceJob implements Runnable{
	private ConcurrentLinkedDeque<CoreStockData> coreStockDataQueue;
	private ConcurrentLinkedDeque<FundStockData> fundStockDataQueue;
	
	private AlphaVantageCoreStockDAO dao;

	public AlphaVantageDataPersistenceJob(ConcurrentLinkedDeque<CoreStockData> cq, ConcurrentLinkedDeque<FundStockData> fq) {
		this.coreStockDataQueue = cq;
		this.fundStockDataQueue = fq;
		this.dao = new AlphaVantageCoreStockDAO();
	}

	@Override
	public void run() {
		log.info("Persisisting Job Started"); 
		try {
			Thread.sleep(15000);
			while (this.coreStockDataQueue.size() > 0) {
				log.info("dailyStockDataQueue {}", coreStockDataQueue.size());
				saveAlphaVantageCoreStock(this.coreStockDataQueue.pollFirst());
				saveAlphaVantageFundStock(this.fundStockDataQueue.peekFirst());
			}
			log.info("Persisisting Job Ended");
		} catch (InterruptedException e) {
			log.error("Error while persisting stock data {}", e);
		}
	}
	
	private void saveAlphaVantageCoreStock(CoreStockData csd) {
		log.info(" saveAlphaVantageCoreStock Start {}", csd);
	
		dao.saveCoreStock(csd);
		
		log.info(" saveAlphaVantageCoreStock End {}");
	}
	
	private void saveAlphaVantageFundStock(FundStockData fsd) {
		log.info(" saveAlphaVantageFundStock Start {}", fsd);
	
		
		
		log.info(" saveAlphaVantageFundStock End {}");
	}

}
