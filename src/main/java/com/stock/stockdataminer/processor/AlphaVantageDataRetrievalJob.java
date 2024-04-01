package com.stock.stockdataminer.processor;

import java.io.FileReader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stockdataminer.model.DailyStockData;
import com.stock.stockdataminer.utils.DataMinerUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDataRetrievalJob implements Runnable {
	private ConcurrentLinkedDeque<String> symbolsQueue;

	private String stockEndPoint;

	private String apiKey;

	private ConcurrentLinkedDeque<DailyStockData> dailyStockDataQueue;

	public AlphaVantageDataRetrievalJob(ConcurrentLinkedDeque<String> sq, ConcurrentLinkedDeque<DailyStockData> hdq,
			String sep, String ak) {
		this.symbolsQueue = sq;
		this.dailyStockDataQueue = hdq;
		this.stockEndPoint = sep;
		this.apiKey = ak;
	}

	@Override
	public void run() {
		log.info("Alpha Vantage Data Retrieval Job started");
		
		getHistoricalData(symbolsQueue.pollFirst(), this.stockEndPoint, this.apiKey);
		
		log.info("Alpha vantage Data Retrieval Job Ended");
	}

	private void getHistoricalData(String symbol, String endpoint, String apiKey) {
		log.info("getHistoricalData currently retrieving {}", symbol);
		try {
			String urlString = String.format(endpoint, symbol, apiKey);
			log.info("***************{}", urlString);
			URL url = new URL(urlString);
			byte[] bytes = new URL(urlString).openStream().readAllBytes();
			//String jsonString = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
			String jsonString = DataMinerUtility.getJsonTestFile();//only for local testing
			JsonNode financialDataNode = getJsonNode(jsonString);
			log.info("getHistoricalData  {}", jsonString);
			getDailyData(financialDataNode);
		} catch (Exception e) {
			log.info("Error while retrieving stock data {}", e);
		}
		log.info("getHistoricalData end retrieving {}");
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
	
	private void getDailyData(JsonNode financialDataNode){
		JsonNode metaDataNode = financialDataNode.get("Meta Data");
		
		String symbol = metaDataNode.get("2. Symbol").asText();
		JsonNode dailyDataNode  = financialDataNode.get("Time Series (Daily)");
		Iterator<String> stringDate = dailyDataNode.fieldNames();
		while(stringDate.hasNext()) {
			DailyStockData dsd = new DailyStockData();
			dsd.setSymbol(symbol);
			String test = stringDate.next();
			JsonNode node = dailyDataNode.get(test);
			dsd.setCurrStockDate(LocalDate.parse(test));
			dsd.setDayOpen(Double.parseDouble(node.get("1. open").asText()));
			dsd.setDayHigh(Double.parseDouble(node.get("2. high").asText()));
			dsd.setDayLow(Double.parseDouble(node.get("3. low").asText()));
			dsd.setDayClose(Double.parseDouble(node.get("4. close").asText()));
			dsd.setDayVolume(Double.parseDouble(node.get("5. volume").asText()));
			this.dailyStockDataQueue.add(dsd);
		}
	}
}
