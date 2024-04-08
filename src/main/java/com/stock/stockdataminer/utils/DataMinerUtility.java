package com.stock.stockdataminer.utils;

import java.io.FileReader;
import java.net.URL;

import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataMinerUtility {
	public static String getJsonTestFile(String file) {
		Object jsonStock = null;
		try {
			jsonStock = new JSONParser().parse(new FileReader(file));
			log.info("jsonQuiz {}", jsonStock);

		} catch (Exception e) {
			log.error("Unable to load the daily.json file {}", e);
		}
		return jsonStock.toString();
	}
	
	
	public static String getJsonString(String endPoint, String symbol, String apiKey) {
		String jsonString = "";
		try {
			String urlString = String.format(endPoint, symbol, apiKey);
			log.info("***************{}", urlString);
			URL url = new URL(urlString);
			byte[] bytes = new URL(urlString).openStream().readAllBytes();
			jsonString = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
			//jsonString = DataMinerUtility.getJsonTestFile("balance.json");//only for local testing

			log.info("----getJsonString  {}", jsonString);
		} catch (Exception e) {
			log.info("Error while retrieving stock data {}", e);
		}
		return jsonString;
	}

	public static JsonNode getJsonNode(String jsonString) {
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
