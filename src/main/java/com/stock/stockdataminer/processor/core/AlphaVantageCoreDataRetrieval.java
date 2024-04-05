package com.stock.stockdataminer.processor.core;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.databind.JsonNode;
import com.stock.stockdataminer.model.CoreStockData;
import com.stock.stockdataminer.utils.DataMinerUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageCoreDataRetrieval {
	private String apiKey;

	private String stockInitialDownload;

	private ConcurrentLinkedDeque<CoreStockData> coreStockDataQueue;
	
	private Map<String, String>endPointsMap;
	
	public AlphaVantageCoreDataRetrieval(ConcurrentLinkedDeque<CoreStockData> cdq,
			Map<String, String>epmap, String ak, String sid) {
		this.coreStockDataQueue = cdq;
		this.endPointsMap = epmap;
		this.apiKey = ak;
		this.stockInitialDownload = sid;
	}
	
	public void getCoreHistoricalData(String symbol) {
		log.info("getHistoricalData currently retrieving {}", symbol);
		getDailyData(symbol);
		getWeeklyData(symbol);
		log.info("getHistoricalData end retrieving {}");
	}

	private void getDailyData(String symbol) {
		String timeSeries = "Time Series (Daily)";
		getData(symbol, timeSeries, this.endPointsMap.get("stockDailyEndPoint"));
	}

	private void getWeeklyData(String symbol) {
		String timeSeries = "Weekly Time Series";
		getData(symbol, timeSeries, this.endPointsMap.get("stockWeeklyEndPoint"));
	}

	private void getData(String symbol, String timeSeries, String endPoint) {
		log.info("getData start {}", timeSeries);
		LocalDate refrechedDate = null;

		String jsonString = DataMinerUtility.getJsonString(endPoint, symbol, this.apiKey);

		JsonNode financialDataNode = DataMinerUtility.getJsonNode(jsonString);
		if(!isvalidFinantialNode(financialDataNode, timeSeries)) {
			return;
		}
		JsonNode metaDataNode = financialDataNode.get("Meta Data");
		if (this.stockInitialDownload.equalsIgnoreCase("false")) {
			refrechedDate = LocalDate.parse(metaDataNode.get("3. Last Refreshed").asText());
		}
		String compSymbol = metaDataNode.get("2. Symbol").asText();
		JsonNode dailyDataNode = financialDataNode.get(timeSeries);
		Iterator<String> stringDate = dailyDataNode.fieldNames();
		while (stringDate.hasNext()) {
			String date = stringDate.next();
			JsonNode node = dailyDataNode.get(date);
			if (refrechedDate == null) {
				this.coreStockDataQueue.add(getCoreStockData(node, compSymbol, date, timeSeries));
			} else {
				LocalDate currStockDate = LocalDate.parse(date);
				if (currStockDate.getYear() == refrechedDate.getYear()
						&& currStockDate.getMonthValue() == refrechedDate.getMonthValue()) {
					this.coreStockDataQueue.add(getCoreStockData(node, compSymbol, date, timeSeries));
				}
			}
		}
		log.info("getData End {}");
	}

	private boolean isvalidFinantialNode(JsonNode financialDataNode, String timeSeries) {
		if (!financialDataNode.has("Meta Data") || financialDataNode.get("Meta Data") == null) {
			log.debug("The field Meta Data is missing");
			return false;
		}
		if (!financialDataNode.has(timeSeries) || financialDataNode.get(timeSeries) == null) {
			log.debug("The field " + timeSeries + " is missing");
			return false;
		}
		JsonNode metaDataNode = financialDataNode.get("Meta Data");
		if (!metaDataNode.has("3. Last Refreshed") || metaDataNode.get("3. Last Refreshed") == null
				|| metaDataNode.get("3. Last Refreshed").asText().isBlank()) {
			log.debug("The field 3. Last Refreshed is missing");
			return false;
		}
		if (!metaDataNode.has("2. Symbol") || metaDataNode.get("2. Symbol") == null
				|| metaDataNode.get("2. Symbol").asText().isBlank()) {
			log.debug("The field 2. Symbol is missing");
			return false;
		}

		return true;
	}

	private CoreStockData getCoreStockData(JsonNode node, String compSymbol, String date, String timeSeries) {
		CoreStockData csd = new CoreStockData();
		csd.setTimeSeries(timeSeries);
		csd.setStockSymbol(compSymbol);
		csd.setCurrStockDate(LocalDate.parse(date));
		if(node.has("1. open") && node.get("1. open") != null && !node.get("1. open").asText().isBlank()) {
			csd.setStockOpen(Double.parseDouble(node.get("1. open").asText()));
		}else {
			log.debug("The field 1. open is missing");
		}
		if(node.has("2. high") && node.get("2. high") != null && !node.get("2. high").asText().isBlank()) {
			csd.setStockHigh(Double.parseDouble(node.get("2. high").asText()));
		}else {
			log.debug("The field 2. high is missing");
		}
		
		if(node.has("3. low") && node.get("3. low") != null && !node.get("3. low").asText().isBlank()) {
			csd.setStockLow(Double.parseDouble(node.get("3. low").asText()));
		}else {
			log.debug("The field 3. low is missing");
		}
		
		if(node.has("4. close") && node.get("4. close") != null && !node.get("4. close").asText().isBlank()) {
			csd.setStockClose(Double.parseDouble(node.get("4. close").asText()));
		}else {
			log.debug("The field 4. close is missing");
		}
		if(node.has("5. volume") && node.get("5. volume") != null && !node.get("5. volume").asText().isBlank()) {
			csd.setStockVolume(Double.parseDouble(node.get("5. volume").asText()));
		}else {
			log.debug("The field 5. volume is missing");
		}

		return csd;
	}

}
