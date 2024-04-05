package com.stock.stockdataminer.processor.fund;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.databind.JsonNode;
import com.stock.stockdataminer.model.FundStockData;
import com.stock.stockdataminer.model.StockIncomeStatData;
import com.stock.stockdataminer.utils.DataMinerUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageIncomeStatDataRetrieval {
	private String apiKey;

	private String stockInitialDownload;

	private ConcurrentLinkedDeque<FundStockData> fundStockDataQueue;
	
	private Map<String, String>endPointsMap;
	
	public AlphaVantageIncomeStatDataRetrieval(ConcurrentLinkedDeque<FundStockData> fdq,
			Map<String, String>epmap, String ak, String sid) {
		this.fundStockDataQueue = fdq;
		this.endPointsMap = epmap;
		this.apiKey = ak;
		this.stockInitialDownload = sid;
	}
	
	public void getStockIncomeData(String symbol) {
		log.debug("getStockIncomeData retrieving income for symbol {}", symbol);
		String jsonString = DataMinerUtility.getJsonString(this.endPointsMap.get("stockIncomeEndpoint"), symbol, this.apiKey);
		JsonNode incomeDataNode = DataMinerUtility.getJsonNode(jsonString);
		JsonNode annualReports = null;
		
		if(incomeDataNode.has("annualReports") && incomeDataNode.get("annualReports") != null) {
			annualReports = incomeDataNode.get("annualReports");
		}else {
			log.debug("The field annualReports is missing");
			return;
		}
		Iterator<JsonNode> nodes = annualReports.elements();
		int count = 0;
		while (nodes.hasNext()) {
			JsonNode nextNode = nodes.next();
			count ++;
			StockIncomeStatData siData = new StockIncomeStatData();
			if(incomeDataNode.has("symbol") && incomeDataNode.get("symbol") != null && !incomeDataNode.get("symbol").asText().isBlank()) {
				siData.setStockSymbol(incomeDataNode.get("symbol").asText());
			}else {
				log.debug("The field symbol is missing");
			}
			if(nextNode.has("fiscalDateEnding") && nextNode.get("fiscalDateEnding") != null && !nextNode.get("fiscalDateEnding").asText().isBlank()) {
				siData.setStockfiscaleDate( LocalDate.parse(nextNode.get("fiscalDateEnding").asText()));
			}else {
				log.debug("The field fiscalDateEnding is missing");
			}
			
			if(nextNode.has("totalRevenue") && nextNode.get("totalRevenue") != null && !nextNode.get("totalRevenue").asText().isBlank()) {
				siData.setTotalRevenue(Double.parseDouble(nextNode.get("totalRevenue").asText()));
			}else {
				log.debug("The field totalRevenue is missing");
			}
			if(nextNode.has("grossProfit") && nextNode.get("grossProfit") != null && !nextNode.get("grossProfit").asText().isBlank()) {
				siData.setGrossProfit(Double.parseDouble(nextNode.get("grossProfit").asText()));
			}else {
				log.debug("The field grossProfit is missing");
			}
			
			if(nextNode.has("netIncome") && nextNode.get("netIncome") != null && !nextNode.get("netIncome").asText().isBlank()) {
				siData.setNetIncome(Double.parseDouble(nextNode.get("netIncome").asText()));
			}else {
				log.debug("The field netIncome is missing");
			}
			
			if(nextNode.has("operatingIncome") && nextNode.get("operatingIncome") != null && !nextNode.get("operatingIncome").asText().isBlank()) {
				siData.setOperatingIncome(Double.parseDouble(nextNode.get("operatingIncome").asText()));
			}else {
				log.debug("The field operatingIncome is missing");
			}
			
			if(nextNode.has("researchAndDevelopment") && nextNode.get("researchAndDevelopment") != null && !nextNode.get("researchAndDevelopment").asText().isBlank()) {
				siData.setReschAndDev(Double.parseDouble(nextNode.get("researchAndDevelopment").asText()));
			}else {
				log.debug("The field researchAndDevelopment is missing");
			}
			this.fundStockDataQueue.add(siData);
			
			if(this.stockInitialDownload.equals("false") && count == 1) {
				break;
			}
		}
		
		log.info("getStockIncomeData");
	}

}
