package com.stock.stockdataminer.model;

public class StockIncomeStatData extends FundStockData{
	private double operatingIncome;
	private double grossProfit;
	private double totalRevenue;
	private double reschAndDev;
	private double netIncome;
	
	
	public double getOperatingIncome() {
		return operatingIncome;
	}
	public void setOperatingIncome(double operatingIncome) {
		this.operatingIncome = operatingIncome;
	}
	public double getGrossProfit() {
		return grossProfit;
	}
	public void setGrossProfit(double grossProfit) {
		this.grossProfit = grossProfit;
	}
	public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public double getReschAndDev() {
		return reschAndDev;
	}
	public void setReschAndDev(double reschAndDev) {
		this.reschAndDev = reschAndDev;
	}
	public double getNetIncome() {
		return netIncome;
	}
	public void setNetIncome(double netIncome) {
		this.netIncome = netIncome;
	}
	
	
}
