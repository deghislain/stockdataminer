package com.stock.stockdataminer.processor.fund;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.stock.stockdataminer.model.FundStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageFundDataRetrieval{

	private AlphaVantageIncomeStatDataRetrieval stockIncomeDataRet;
	
	private AlphaVantageBalanceSheetDataRetrieval balanceDataRet;
	
	private AlphaVantageCashFlowDataRetrieval cashRetrieval;
	
	private AlphaVantageGenericDataRetrieval genericDataRetrieval;
	
	
	public AlphaVantageFundDataRetrieval(ConcurrentLinkedDeque<FundStockData> fdq,
			Map<String, String>epmap, String ak, String sid) {
		this.stockIncomeDataRet = new AlphaVantageIncomeStatDataRetrieval(fdq,epmap, ak, sid);
		this.balanceDataRet = new AlphaVantageBalanceSheetDataRetrieval(fdq, epmap, ak, sid);
		this.cashRetrieval = new AlphaVantageCashFlowDataRetrieval(fdq, epmap, ak, sid);
		this.genericDataRetrieval = new AlphaVantageGenericDataRetrieval(fdq, epmap, ak, sid);
	}
	
	public void getFundHistoricalData(String symbol) {
		log.info("getFundHistoricalData currently retrieving {}", symbol);
			this.stockIncomeDataRet.getStockIncomeData(symbol);
			this.balanceDataRet.getStocBalanceSheetData(symbol);
			this.cashRetrieval.getStocCashFlowData(symbol);
			this.genericDataRetrieval.getStockEarningsData(symbol);
		log.info("getFundHistoricalData end retrieving {}");
	}


}
