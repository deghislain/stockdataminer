package com.stock.stockdataminer.utils;

import java.io.FileReader;

import org.json.simple.parser.JSONParser;

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
}
