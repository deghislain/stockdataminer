package com.stock.stockdataminer.processor.fund;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.fasterxml.jackson.databind.JsonNode;
import com.stock.stockdataminer.model.FundStockData;
import com.stock.stockdataminer.model.StockGenericInfo;
import com.stock.stockdataminer.utils.DataMinerUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageGenericDataRetrieval {

	private String apiKey;

	private String stockInitialDownload;

	private ConcurrentLinkedDeque<FundStockData> fundStockDataQueue;

	private Map<String, String> endPointsMap;

	public AlphaVantageGenericDataRetrieval(ConcurrentLinkedDeque<FundStockData> fdq, Map<String, String> epmap,
			String ak, String sid) {
		this.fundStockDataQueue = fdq;
		this.endPointsMap = epmap;
		this.apiKey = ak;
		this.stockInitialDownload = sid;
	}

	public void getStockEarningsData(String symbol) {
		log.debug("getStockEarningsData retrieving earnings for symbol {}", symbol);
		String jsonString = DataMinerUtility.getJsonString(this.endPointsMap.get("stockEarningsEndpoint"), symbol,
				this.apiKey);
		JsonNode earningsNode = DataMinerUtility.getJsonNode(jsonString);

		JsonNode annualEarnings = null;
		if (earningsNode.has("annualEarnings") && earningsNode.get("annualEarnings") != null) {
			annualEarnings = earningsNode.get("annualEarnings");
		} else {
			log.debug("The field annualReports is missing");
			return;
		}

		Iterator<JsonNode> nodes = annualEarnings.elements();
		int count = 0;
		while (nodes.hasNext()) {
			JsonNode nextNode = nodes.next();
			count++;
			StockGenericInfo earning = new StockGenericInfo();
			if (earningsNode.has("symbol") && earningsNode.get("symbol") != null
					&& !earningsNode.get("symbol").asText().isBlank()) {
				earning.setStockSymbol(earningsNode.get("symbol").asText());
			} else {
				log.debug("The field symbol is missing");
			}

			if (nextNode.has("fiscalDateEnding") && nextNode.get("fiscalDateEnding") != null
					&& !nextNode.get("fiscalDateEnding").asText().isBlank()) {
				earning.setStockfiscaleDate(LocalDate.parse(nextNode.get("fiscalDateEnding").asText()));
			} else {
				log.debug("The field fiscalDateEnding is missing");
			}

			if (nextNode.has("reportedEPS") && nextNode.get("reportedEPS") != null
					&& !nextNode.get("reportedEPS").asText().isBlank()) {
				String operatingCashflow = nextNode.get("reportedEPS").asText();
				earning.setReportedEPS(
						Double.parseDouble(operatingCashflow.equalsIgnoreCase("none") ? "0" : operatingCashflow));
			} else {
				log.debug("The field reportedEPS is missing");
			}
			
			this.fundStockDataQueue.add(earning);
			if(this.stockInitialDownload.equals("false") && count == 1) {
				break;
			}
		}
	}
}
