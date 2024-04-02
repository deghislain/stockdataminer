package com.stock.stockdataminer.processor.core;

import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stockdataminer.model.DailyStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageCoreDataRetrievalJob implements Runnable {
	private ConcurrentLinkedDeque<String> symbolsQueue;

	private String stockDailyEndPoint;

	private String stockWeeklyEndPoint;

	private String apiKey;

	private String stockInitialDownload;

	private ConcurrentLinkedDeque<DailyStockData> dailyStockDataQueue;

	public AlphaVantageCoreDataRetrievalJob(ConcurrentLinkedDeque<String> sq, ConcurrentLinkedDeque<DailyStockData> hdq,
			String sep, String swk, String ak, String sid) {
		this.symbolsQueue = sq;
		this.dailyStockDataQueue = hdq;
		this.stockDailyEndPoint = sep;
		this.stockWeeklyEndPoint = swk;
		this.apiKey = ak;
		this.stockInitialDownload = sid;
	}

	@Override
	public void run() {
		log.info("Alpha Vantage Data Retrieval Job started");

		getHistoricalData(symbolsQueue.pollFirst());

		log.info("Alpha vantage Data Retrieval Job Ended");
	}

	private void getHistoricalData(String symbol) {
		log.info("getHistoricalData currently retrieving {}", symbol);
		getDailyData(symbol);
		log.info("getHistoricalData end retrieving {}");
	}

	private void getDailyData(String symbol) {
		LocalDate refrechedDate = null;
		String jsonString = getJsonString(this.stockDailyEndPoint, symbol);
		JsonNode financialDataNode = getJsonNode(jsonString);
		JsonNode metaDataNode = financialDataNode.get("Meta Data");
		if (this.stockInitialDownload.equalsIgnoreCase("false")) {
			refrechedDate = LocalDate.parse(metaDataNode.get("3. Last Refreshed").asText());
		}
		String compSymbol = metaDataNode.get("2. Symbol").asText();
		JsonNode dailyDataNode = financialDataNode.get("Time Series (Daily)");
		Iterator<String> stringDate = dailyDataNode.fieldNames();
		while (stringDate.hasNext()) {
			String date = stringDate.next();
			JsonNode node = dailyDataNode.get(date);
			if (refrechedDate == null) {
				this.dailyStockDataQueue.add(getDailyStockData(node, compSymbol, date));
			}else {
				LocalDate currStockDate = LocalDate.parse(date);
				if(currStockDate.getYear() == refrechedDate.getYear() && currStockDate.getMonthValue() == refrechedDate.getMonthValue()) {
					this.dailyStockDataQueue.add(getDailyStockData(node, compSymbol, date));
				}
			}
		}
	}

	private DailyStockData getDailyStockData(JsonNode node, String compSymbol, String date) {
		DailyStockData dsd = new DailyStockData();
		dsd.setSymbol(compSymbol);
		dsd.setCurrStockDate(LocalDate.parse(date));
		dsd.setDayOpen(Double.parseDouble(node.get("1. open").asText()));
		dsd.setDayHigh(Double.parseDouble(node.get("2. high").asText()));
		dsd.setDayLow(Double.parseDouble(node.get("3. low").asText()));
		dsd.setDayClose(Double.parseDouble(node.get("4. close").asText()));
		dsd.setDayVolume(Double.parseDouble(node.get("5. volume").asText()));

		return dsd;
	}

	private String getJsonString(String endPoint, String symbol) {
		String jsonString = "";
		try {
			String urlString = String.format(stockDailyEndPoint, symbol, this.apiKey);
			log.info("***************{}", urlString);
			URL url = new URL(urlString);
			byte[] bytes = new URL(urlString).openStream().readAllBytes();
			jsonString = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
			// String jsonString = DataMinerUtility.getJsonTestFile();//only for local
			// testing

			log.info("getHistoricalData  {}", jsonString);
		} catch (Exception e) {
			log.info("Error while retrieving stock data {}", e);
		}
		return jsonString;
	}

	private JsonNode getJsonNode(String jsonString) {
		ObjectMapper obj = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = obj.readTree(jsonString);
		} catch (JsonMappingException e) {
			log.error("Error while mapping alpha vantage json file {}", e);
		} catch (JsonProcessingException e) {
			log.error("Error while Processing alpha vantage json file {}", e);
		}
		return jsonNode;
	}

}
