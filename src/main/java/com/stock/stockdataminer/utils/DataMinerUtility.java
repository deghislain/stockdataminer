package com.stock.stockdataminer.utils;

import java.io.FileReader;

import org.json.simple.parser.JSONParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataMinerUtility {
	public static String getJsonTestFile() {
		Object jsonQuiz = null;
		try {
			jsonQuiz = new JSONParser().parse(new FileReader("daily.json"));

		} catch (Exception e) {
			log.error("Unable to load the daily.json file {}", e);
		}
		return jsonQuiz.toString();
	}
}
