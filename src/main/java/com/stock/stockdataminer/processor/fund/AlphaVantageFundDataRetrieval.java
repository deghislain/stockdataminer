package com.stock.stockdataminer.processor.fund;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.stock.stockdataminer.model.FundStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageFundDataRetrieval{

	private AlphaVantageIncomeStatDataRetrieval stockIncomeDataRet;
	
	
	
	public AlphaVantageFundDataRetrieval(ConcurrentLinkedDeque<FundStockData> fdq,
			Map<String, String>epmap, String ak, String sid) {
		this.stockIncomeDataRet = new AlphaVantageIncomeStatDataRetrieval(fdq,epmap, ak, sid);
	}
	
	public void getFundHistoricalData(String symbol) {
		log.info("getFundHistoricalData currently retrieving {}", symbol);
			this.stockIncomeDataRet.getStockIncomeData(symbol);
		log.info("getFundHistoricalData end retrieving {}");
	}


}
