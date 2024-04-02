package com.stock.stockdataminer.processor.core;

import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stockdataminer.model.CoreStockData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageCoreDataRetrievalJob implements Runnable {
	private ConcurrentLinkedDeque<String> symbolsQueue;

	private String stockDailyEndPoint;

	private String stockWeeklyEndPoint;

	private String apiKey;

	private String stockInitialDownload;

	private ConcurrentLinkedDeque<CoreStockData> dailyStockDataQueue;

	public AlphaVantageCoreDataRetrievalJob(ConcurrentLinkedDeque<String> sq, ConcurrentLinkedDeque<CoreStockData> hdq,
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
		getWeeklyData(symbol);
		log.info("getHistoricalData end retrieving {}");
	}

	private void getDailyData(String symbol) {
		String timeSeries = "Time Series (Daily)";
		getData(symbol, timeSeries, this.stockDailyEndPoint);
	}

	private void getWeeklyData(String symbol) {
		String timeSeries = "Weekly Time Series";
		getData(symbol, timeSeries, this.stockWeeklyEndPoint);
	}

	private void getData(String symbol, String timeSeries, String endPoint) {
		log.info("getData start {}", timeSeries);
		LocalDate refrechedDate = null;
		
		String jsonString = getJsonString(endPoint, symbol);
		
		JsonNode financialDataNode = getJsonNode(jsonString);
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
				this.dailyStockDataQueue.add(getCoreStockData(node, compSymbol, date, timeSeries));
			} else {
				LocalDate currStockDate = LocalDate.parse(date);
				if (currStockDate.getYear() == refrechedDate.getYear()
						&& currStockDate.getMonthValue() == refrechedDate.getMonthValue()) {
					this.dailyStockDataQueue.add(getCoreStockData(node, compSymbol, date, timeSeries));
				}
			}
		}
		log.info("getData End {}");
	}

	private CoreStockData getCoreStockData(JsonNode node, String compSymbol, String date, String timeSeries) {
		CoreStockData csd = new CoreStockData();
		csd.setTimeSeries(timeSeries);
		csd.setStockSymbol(compSymbol);
		csd.setCurrStockDate(LocalDate.parse(date));
		csd.setStockOpen(Double.parseDouble(node.get("1. open").asText()));
		csd.setStockHigh(Double.parseDouble(node.get("2. high").asText()));
		csd.setStockLow(Double.parseDouble(node.get("3. low").asText()));
		csd.setStockClose(Double.parseDouble(node.get("4. close").asText()));
		csd.setStockVolume(Double.parseDouble(node.get("5. volume").asText()));

		return csd;
	}

	private String getJsonString(String endPoint, String symbol) {
		String jsonString = "";
		try {
			String urlString = String.format(endPoint, symbol, this.apiKey);
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
