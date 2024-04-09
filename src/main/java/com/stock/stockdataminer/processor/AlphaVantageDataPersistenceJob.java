package com.stock.stockdataminer.processor;

import java.util.concurrent.ConcurrentLinkedDeque;

import com.stock.stockdataminer.dao.AlphaVantageCoreStockDAO;
import com.stock.stockdataminer.dao.AlphaVantageFundStockDAO;
import com.stock.stockdataminer.model.CoreStockData;
import com.stock.stockdataminer.model.FundStockData;
import com.stock.stockdataminer.model.StockBalanceSheetData;
import com.stock.stockdataminer.model.StockCashFlowData;
import com.stock.stockdataminer.model.StockIncomeStatData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDataPersistenceJob implements Runnable{
	private ConcurrentLinkedDeque<CoreStockData> coreStockDataQueue;
	private ConcurrentLinkedDeque<FundStockData> fundStockDataQueue;
	
	private AlphaVantageCoreStockDAO coreStockDao;
	private AlphaVantageFundStockDAO fundStockDao;

	public AlphaVantageDataPersistenceJob(ConcurrentLinkedDeque<CoreStockData> cq, ConcurrentLinkedDeque<FundStockData> fq) {
		this.coreStockDataQueue = cq;
		this.fundStockDataQueue = fq;
		this.coreStockDao = new AlphaVantageCoreStockDAO();
		this.fundStockDao = new AlphaVantageFundStockDAO();
	}

	@Override
	public void run() {
		log.info("Persisisting Job Started"); 
		try {
			Thread.sleep(15000);
			while (this.coreStockDataQueue.size() > 0 || this.fundStockDataQueue.size() > 0) {
				log.info("dailyStockDataQueue {}", coreStockDataQueue.size());
				log.info("fundStockDataQueue {}", fundStockDataQueue.size());
				saveAlphaVantageCoreStock(this.coreStockDataQueue.pollFirst());
				saveAlphaVantageFundStock(this.fundStockDataQueue.pollFirst());
			}
			log.info("Persisisting Job Ended");
		} catch (InterruptedException e) {
			log.error("Error while persisting stock data {}", e);
		}
	}
	
	private void saveAlphaVantageCoreStock(CoreStockData csd) {
		log.info(" saveAlphaVantageCoreStock Start {}", csd);
		if (csd != null) {
			this.coreStockDao.saveCoreStock(csd);
		}else {
			log.info("coreStockDataQueue is empty");
		}
		log.info(" saveAlphaVantageCoreStock End {}");
	}
	
	private void saveAlphaVantageFundStock(FundStockData fsd) {
		log.info(" saveAlphaVantageFundStock Start {}", fsd);
		StockIncomeStatData statData = null;
		if(fsd != null && fsd instanceof StockIncomeStatData) {
			statData = (StockIncomeStatData)fsd;
			 this.fundStockDao.saveIncomeStockData(statData);
		}
		StockBalanceSheetData balanceData = null;
		if(fsd != null && fsd instanceof StockBalanceSheetData) {
			balanceData = (StockBalanceSheetData)fsd;
			 this.fundStockDao.saveBalanceSheetStockData(balanceData);
		}
		StockCashFlowData cashData = null;
		if(fsd != null && fsd instanceof StockCashFlowData) {
			cashData = (StockCashFlowData)fsd;
			 this.fundStockDao.saveCashFlowStockData(cashData);
		}
		log.info(" saveAlphaVantageFundStock End {}");
	}

}
