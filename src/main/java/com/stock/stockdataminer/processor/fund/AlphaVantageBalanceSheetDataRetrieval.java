package com.stock.stockdataminer.processor.fund;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.databind.JsonNode;
import com.stock.stockdataminer.model.FundStockData;
import com.stock.stockdataminer.model.StockBalanceSheetData;
import com.stock.stockdataminer.utils.DataMinerUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageBalanceSheetDataRetrieval {
	private String apiKey;

	private String stockInitialDownload;

	private ConcurrentLinkedDeque<FundStockData> fundStockDataQueue;
	
	private Map<String, String>endPointsMap;
	
	public AlphaVantageBalanceSheetDataRetrieval(ConcurrentLinkedDeque<FundStockData> fdq,
			Map<String, String>epmap, String ak, String sid) {
		this.fundStockDataQueue = fdq;
		this.endPointsMap = epmap;
		this.apiKey = ak;
		this.stockInitialDownload = sid;
	}
	
	public void getStocBalanceSheetData(String symbol) {
		log.debug("getStocBalanceSheetData retrieving balance for symbol {}", symbol);
		String jsonString = DataMinerUtility.getJsonString(this.endPointsMap.get("balanceSheetEndpoint"), symbol, this.apiKey);
		JsonNode balanceDataNode = DataMinerUtility.getJsonNode(jsonString);
		JsonNode annualReports = null;
		
		if(balanceDataNode.has("annualReports") && balanceDataNode.get("annualReports") != null) {
			annualReports = balanceDataNode.get("annualReports");
		}else {
			log.debug("The field annualReports is missing");
			return;
		}
		Iterator<JsonNode> nodes = annualReports.elements();
		int count = 0;
		while (nodes.hasNext()) {
			JsonNode nextNode = nodes.next();
			count ++;
			StockBalanceSheetData balanceData = new StockBalanceSheetData();
			if(balanceDataNode.has("symbol") && balanceDataNode.get("symbol") != null && !balanceDataNode.get("symbol").asText().isBlank()) {
				balanceData.setStockSymbol(balanceDataNode.get("symbol").asText());
			}else {
				log.debug("The field symbol is missing");
			}
			
			if(nextNode.has("fiscalDateEnding") && nextNode.get("fiscalDateEnding") != null && !nextNode.get("fiscalDateEnding").asText().isBlank()) {
				balanceData.setStockfiscaleDate( LocalDate.parse(nextNode.get("fiscalDateEnding").asText()));
			}else {
				log.debug("The field fiscalDateEnding is missing");
			}
			
			if(nextNode.has("totalShareholderEquity") && nextNode.get("totalShareholderEquity") != null && !nextNode.get("totalShareholderEquity").asText().isBlank()) {
				String totalShareholderEquity = nextNode.get("totalShareholderEquity").asText();
				balanceData.setTotalShareholderEquity(Double.parseDouble(totalShareholderEquity.equalsIgnoreCase("none")? "0" : totalShareholderEquity ));
			}else {
				log.debug("The field totalShareholderEquity is missing");
			}
			
			if(nextNode.has("totalAssets") && nextNode.get("totalAssets") != null && !nextNode.get("totalAssets").asText().isBlank()) {
				String totalAssets = nextNode.get("totalAssets").asText();
				balanceData.setTotalAssets(Double.parseDouble(totalAssets.equalsIgnoreCase("none")? "0" : totalAssets ));
			}else {
				log.debug("The field totalAssets is missing");
			}
			
			if(nextNode.has("cashAndShortTermInvestments") && nextNode.get("cashAndShortTermInvestments") != null && !nextNode.get("cashAndShortTermInvestments").asText().isBlank()) {
				String cashAndShortTermInvestments = nextNode.get("cashAndShortTermInvestments").asText();
				balanceData.setCashAndShortTermInvestments(Double.parseDouble(cashAndShortTermInvestments.equalsIgnoreCase("none")? "0" : cashAndShortTermInvestments ));
			}else {
				log.debug("The field cashAndShortTermInvestments is missing");
			}
			
			if(nextNode.has("inventory") && nextNode.get("inventory") != null && !nextNode.get("inventory").asText().isBlank()) {
				String inventory = nextNode.get("inventory").asText();
				balanceData.setInventory(Double.parseDouble(inventory.equalsIgnoreCase("none")? "0" : inventory ));
			}else {
				log.debug("The field inventory is missing");
			}
			if(nextNode.has("investments") && nextNode.get("investments") != null && !nextNode.get("investments").asText().isBlank()) {
				String investments = nextNode.get("investments").asText();
				balanceData.setInvestments(Double.parseDouble(investments.equalsIgnoreCase("none")? "0" : investments ));
			}else {
				log.debug("The field investments is missing");
			}
			if(nextNode.has("longTermInvestments") && nextNode.get("longTermInvestments") != null && !nextNode.get("longTermInvestments").asText().isBlank()) {
				String longTermInvestments = nextNode.get("longTermInvestments").asText();
				balanceData.setLongTermInvestments(Double.parseDouble(longTermInvestments.equalsIgnoreCase("none")? "0" : longTermInvestments ));
			}else {
				log.debug("The field longTermInvestments is missing");
			}
			if(nextNode.has("totalLiabilities") && nextNode.get("totalLiabilities") != null && !nextNode.get("totalLiabilities").asText().isBlank()) {
				String totalLiabilities = nextNode.get("totalLiabilities").asText();
				balanceData.setTotalLiabilities(Double.parseDouble(totalLiabilities.equalsIgnoreCase("none")? "0" : totalLiabilities ));
			}else {
				log.debug("The field totalLiabilities is missing");
			}
			if(nextNode.has("currentDebt") && nextNode.get("currentDebt") != null && !nextNode.get("currentDebt").asText().isBlank()) {
				String currentDebt = nextNode.get("currentDebt").asText();
				balanceData.setCurrentDebt(Double.parseDouble(currentDebt.equalsIgnoreCase("none")? "0" : currentDebt ));
			}else {
				log.debug("The field currentDebt is missing");
			}
			this.fundStockDataQueue.add(balanceData);
			if(this.stockInitialDownload.equals("false") && count == 1) {
				break;
			}
		}
		log.info("getStocBalanceSheetData");
	}
}
