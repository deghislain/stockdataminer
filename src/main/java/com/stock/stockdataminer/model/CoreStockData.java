package com.stock.stockdataminer.model;

import java.time.LocalDate;
import java.util.UUID;

public class CoreStockData {
	
	private UUID stockDataId;
	private String stockSymbol;
	private LocalDate currStockDate;
	private double stockOpen;
	private double stockHigh;
	private double stockLow;
	private double stockClose;
	private double stockVolume;
	private double dividendAmount;
	private String timeSeries;
	
	
	
	
	
	 public UUID getStockDataId() {
		return stockDataId;
	}

	public void setStockDataId(UUID stockDataId) {
		this.stockDataId = stockDataId;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public LocalDate getCurrStockDate() {
		return currStockDate;
	}

	public void setCurrStockDate(LocalDate currStockDate) {
		this.currStockDate = currStockDate;
	}

	public double getStockOpen() {
		return stockOpen;
	}

	public void setStockOpen(double stockOpen) {
		this.stockOpen = stockOpen;
	}

	public double getStockHigh() {
		return stockHigh;
	}

	public void setStockHigh(double stockHigh) {
		this.stockHigh = stockHigh;
	}

	public double getStockLow() {
		return stockLow;
	}

	public void setStockLow(double stockLow) {
		this.stockLow = stockLow;
	}

	public double getStockClose() {
		return stockClose;
	}

	public void setStockClose(double stockClose) {
		this.stockClose = stockClose;
	}


	public double getStockVolume() {
		return stockVolume;
	}

	public void setStockVolume(double stockVolume) {
		this.stockVolume = stockVolume;
	}

	public double getDividendAmount() {
		return dividendAmount;
	}

	public void setDividendAmount(double dividendAmount) {
		this.dividendAmount = dividendAmount;
	}

	public String getTimeSeries() {
		return timeSeries;
	}

	public void setTimeSeries(String timeSeries) {
		this.timeSeries = timeSeries;
	}





	@Override
	    public String toString() {
	        return "CoreStockData{" +
	                "stockDataId='" + stockDataId + '\'' +
	                ", stockSymbol='" + stockSymbol + '\'' +
	                ", currStockDate=" + currStockDate +
	                ", stockOpen=" + stockOpen +
	                ", stockClose=" + stockClose +
	                '}';
	    }
}
