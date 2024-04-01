package com.stock.stockdataminer.model;

import java.time.LocalDate;
import java.util.UUID;

public class DailyStockData {
	
	private UUID dailyStockDataId;
	private String symbol;
	private LocalDate currStockDate;
	private double dayOpen;
	private double dayHigh;
	private double dayLow;
	private double dayClose;
	private double dayVolume;
	private double dividendAmount;
	
	
	
	
	
	
	public UUID getDailyStockDataId() {
		return dailyStockDataId;
	}
	public void setDailyStockDataId(UUID dailyStockDataId) {
		this.dailyStockDataId = dailyStockDataId;
	}
	public LocalDate getCurrStockDate() {
		return currStockDate;
	}
	public void setCurrStockDate(LocalDate currStockDate) {
		this.currStockDate = currStockDate;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getDayOpen() {
		return dayOpen;
	}
	public void setDayOpen(double dayOpen) {
		this.dayOpen = dayOpen;
	}
	public double getDayHigh() {
		return dayHigh;
	}
	public void setDayHigh(double dayHigh) {
		this.dayHigh = dayHigh;
	}
	public double getDayLow() {
		return dayLow;
	}
	public void setDayLow(double dayLow) {
		this.dayLow = dayLow;
	}
	public double getDayClose() {
		return dayClose;
	}
	public void setDayClose(double dayClose) {
		this.dayClose = dayClose;
	}
	public double getDayVolume() {
		return dayVolume;
	}
	public void setDayVolume(double dayVolume) {
		this.dayVolume = dayVolume;
	}
	public double getDividendAmount() {
		return dividendAmount;
	}
	public void setDividendAmount(double dividendAmount) {
		this.dividendAmount = dividendAmount;
	}
	
	
	 @Override
	    public String toString() {
	        return "DailyStockData{" +
	                "dailyStockDataId='" + dailyStockDataId + '\'' +
	                ", symbol='" + symbol + '\'' +
	                ", currStockDate=" + currStockDate +
	                ", dayOpen=" + dayOpen +
	                ", dayClose=" + dayClose +
	                '}';
	    }
}
