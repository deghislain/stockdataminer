package com.stock.stockdataminer.processor.fund;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.databind.JsonNode;
import com.stock.stockdataminer.model.FundStockData;
import com.stock.stockdataminer.model.StockCashFlowData;
import com.stock.stockdataminer.utils.DataMinerUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageCashFlowDataRetrieval {
	private String apiKey;

	private String stockInitialDownload;

	private ConcurrentLinkedDeque<FundStockData> fundStockDataQueue;

	private Map<String, String> endPointsMap;

	public AlphaVantageCashFlowDataRetrieval(ConcurrentLinkedDeque<FundStockData> fdq, Map<String, String> epmap,
			String ak, String sid) {
		this.fundStockDataQueue = fdq;
		this.endPointsMap = epmap;
		this.apiKey = ak;
		this.stockInitialDownload = sid;
	}

	public void getStocCashFlowData(String symbol) {
		log.debug("getStocCashFlowData retrieving cash flow for symbol {}", symbol);
		String jsonString = DataMinerUtility.getJsonString(this.endPointsMap.get("cashFlowEndpoint"), symbol,
				this.apiKey);
		JsonNode cashDataNode = DataMinerUtility.getJsonNode(jsonString);
		JsonNode annualReports = null;

		if (cashDataNode.has("annualReports") && cashDataNode.get("annualReports") != null) {
			annualReports = cashDataNode.get("annualReports");
		} else {
			log.debug("The field annualReports is missing");
			return;
		}
		Iterator<JsonNode> nodes = annualReports.elements();
		int count = 0;
		while (nodes.hasNext()) {
			JsonNode nextNode = nodes.next();
			count ++;
			StockCashFlowData cashData = new StockCashFlowData();
			if(cashDataNode.has("symbol") && cashDataNode.get("symbol") != null && !cashDataNode.get("symbol").asText().isBlank()) {
				cashData.setStockSymbol(cashDataNode.get("symbol").asText());
			}else {
				log.debug("The field symbol is missing");
			}
			
			if(nextNode.has("fiscalDateEnding") && nextNode.get("fiscalDateEnding") != null && !nextNode.get("fiscalDateEnding").asText().isBlank()) {
				cashData.setStockfiscaleDate( LocalDate.parse(nextNode.get("fiscalDateEnding").asText()));
			}else {
				log.debug("The field fiscalDateEnding is missing");
			}
			
			if(nextNode.has("operatingCashflow") && nextNode.get("operatingCashflow") != null && !nextNode.get("operatingCashflow").asText().isBlank()) {
				String operatingCashflow = nextNode.get("operatingCashflow").asText();
				cashData.setOperatingCashFlow(Double.parseDouble(operatingCashflow.equalsIgnoreCase("none")? "0" : operatingCashflow ));
			}else {
				log.debug("The field operatingCashflow is missing");
			}
			if(nextNode.has("changeInInventory") && nextNode.get("changeInInventory") != null && !nextNode.get("changeInInventory").asText().isBlank()) {
				String changeInInventory = nextNode.get("changeInInventory").asText();
				cashData.setChangeInventory(Double.parseDouble(changeInInventory.equalsIgnoreCase("none")? "0" : changeInInventory ));
			}else {
				log.debug("The field changeInInventory is missing");
			}
			if(nextNode.has("profitLoss") && nextNode.get("profitLoss") != null && !nextNode.get("profitLoss").asText().isBlank()) {
				String profitLoss = nextNode.get("profitLoss").asText();
				cashData.setProfitLoss (Double.parseDouble(profitLoss.equalsIgnoreCase("none")? "0" : profitLoss ));
			}else {
				log.debug("The field profitLoss is missing");
			}
			if(nextNode.has("dividendPayout") && nextNode.get("dividendPayout") != null && !nextNode.get("dividendPayout").asText().isBlank()) {
				String dividendPayout = nextNode.get("dividendPayout").asText();
				cashData.setDividendPayout(Double.parseDouble(dividendPayout.equalsIgnoreCase("none")? "0" : dividendPayout ));
			}else {
				log.debug("The field dividendPayout is missing");
			}
			if(nextNode.has("capitalExpenditures") && nextNode.get("capitalExpenditures") != null && !nextNode.get("capitalExpenditures").asText().isBlank()) {
				String capitalExpenditures = nextNode.get("capitalExpenditures").asText();
				cashData.setCapitalExpenditure(Double.parseDouble(capitalExpenditures.equalsIgnoreCase("none")? "0" : capitalExpenditures ));
			}else {
				log.debug("The field capitalExpenditures is missing");
			}
			if(nextNode.has("cashflowFromInvestment") && nextNode.get("cashflowFromInvestment") != null && !nextNode.get("cashflowFromInvestment").asText().isBlank()) {
				String cashflowFromInvestment = nextNode.get("cashflowFromInvestment").asText();
				cashData.setCashflowFromInvestment(Double.parseDouble(cashflowFromInvestment.equalsIgnoreCase("none")? "0" : cashflowFromInvestment ));
			}else {
				log.debug("The field cashflowFromInvestment is missing");
			}
			if(nextNode.has("cashflowFromFinancing") && nextNode.get("cashflowFromFinancing") != null && !nextNode.get("cashflowFromFinancing").asText().isBlank()) {
				String cashflowFromFinancing = nextNode.get("cashflowFromFinancing").asText();
				cashData.setCashflowFromFinancing(Double.parseDouble(cashflowFromFinancing.equalsIgnoreCase("none")? "0" : cashflowFromFinancing ));
			}else {
				log.debug("The field cashflowFromFinancing is missing");
			}
			if(nextNode.has("changeInOperatingLiabilities") && nextNode.get("changeInOperatingLiabilities") != null && !nextNode.get("changeInOperatingLiabilities").asText().isBlank()) {
				String changeInOperatingLiabilities = nextNode.get("changeInOperatingLiabilities").asText();
				cashData.setChangeInOperatingLiabilities(Double.parseDouble(changeInOperatingLiabilities.equalsIgnoreCase("none")? "0" : changeInOperatingLiabilities ));
			}else {
				log.debug("The field changeInOperatingLiabilities is missing");
			}
			if(nextNode.has("changeInOperatingAssets") && nextNode.get("changeInOperatingAssets") != null && !nextNode.get("changeInOperatingAssets").asText().isBlank()) {
				String changeInOperatingAssets = nextNode.get("changeInOperatingAssets").asText();
				cashData.setChangeInOperatingAssets(Double.parseDouble(changeInOperatingAssets.equalsIgnoreCase("none")? "0" : changeInOperatingAssets ));
			}else {
				log.debug("The field changeInOperatingAssets is missing");
			}
			this.fundStockDataQueue.add(cashData);
			if(this.stockInitialDownload.equals("false") && count == 1) {
				break;
			}
			
		}
	}

}
