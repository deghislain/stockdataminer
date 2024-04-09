package com.stock.stockdataminer.processor;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.stock.stockdataminer.model.CoreStockData;
import com.stock.stockdataminer.model.FundStockData;
import com.stock.stockdataminer.processor.core.AlphaVantageCoreDataRetrieval;
import com.stock.stockdataminer.processor.fund.AlphaVantageBalanceSheetDataRetrieval;
import com.stock.stockdataminer.processor.fund.AlphaVantageCashFlowDataRetrieval;
import com.stock.stockdataminer.processor.fund.AlphaVantageFundDataRetrieval;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDataRetrievalJob implements Runnable {
	private ConcurrentLinkedDeque<String> symbolsQueue;
	
	private AlphaVantageCoreDataRetrieval coreDataRetrieval;
	
	private AlphaVantageFundDataRetrieval fundDataRetrieval;
	

	
	
	
	public AlphaVantageDataRetrievalJob(ConcurrentLinkedDeque<String> sq, ConcurrentLinkedDeque<CoreStockData> cdq,ConcurrentLinkedDeque<FundStockData> fdq,
			Map<String, String>epmap, String ak, String sid) {
		this.symbolsQueue = sq;
		this.coreDataRetrieval = new AlphaVantageCoreDataRetrieval(cdq, epmap, ak, sid);
		this.fundDataRetrieval = new AlphaVantageFundDataRetrieval(fdq, epmap, ak, sid);
	}

	@Override
	public void run() {
		log.info("Alpha Vantage Data Retrieval Job started");

		while (this.symbolsQueue.size() > 0) {
			log.info("symbolsQueue {}", symbolsQueue.size());
			String symbol = this.symbolsQueue.pollFirst();
			this.coreDataRetrieval.getCoreHistoricalData(symbol);
			this.fundDataRetrieval.getFundHistoricalData(symbol);
		}

		log.info("Alpha vantage Data Retrieval Job Ended");
	}

	

}
