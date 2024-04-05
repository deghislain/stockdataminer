package com.stock.stockdataminer.model;

import java.time.LocalDate;
import java.util.UUID;

public class FundStockData {
	private UUID fundStockDataId;
	private String stockSymbol;
	private LocalDate stockfiscaleDate;
	
	
	public UUID getFundStockDataId() {
		return fundStockDataId;
	}
	public void setFundStockDataId(UUID fundStockDataId) {
		this.fundStockDataId = fundStockDataId;
	}
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public LocalDate getStockfiscaleDate() {
		return stockfiscaleDate;
	}
	public void setStockfiscaleDate(LocalDate stockfiscaleDate) {
		this.stockfiscaleDate = stockfiscaleDate;
	}
	
	
}
