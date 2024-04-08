package com.stock.stockdataminer.model;

public class StockBalanceSheetData extends FundStockData{
	private double totalShareholderEquity;
	private double totalAssets;
	private double cashAndShortTermInvestments;
	private double inventory;
	private double investments;
	private double longTermInvestments;
	private double totalLiabilities;
	private double currentDebt;
	
	
	public double getTotalShareholderEquity() {
		return totalShareholderEquity;
	}
	public void setTotalShareholderEquity(double totalShareholderEquity) {
		this.totalShareholderEquity = totalShareholderEquity;
	}
	public double getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(double totalAssets) {
		this.totalAssets = totalAssets;
	}
	public double getCashAndShortTermInvestments() {
		return cashAndShortTermInvestments;
	}
	public void setCashAndShortTermInvestments(double cashAndShortTermInvestments) {
		this.cashAndShortTermInvestments = cashAndShortTermInvestments;
	}
	public double getInventory() {
		return inventory;
	}
	public void setInventory(double inventory) {
		this.inventory = inventory;
	}
	public double getInvestments() {
		return investments;
	}
	public void setInvestments(double investments) {
		this.investments = investments;
	}
	public double getLongTermInvestments() {
		return longTermInvestments;
	}
	public void setLongTermInvestments(double longTermInvestments) {
		this.longTermInvestments = longTermInvestments;
	}
	public double getTotalLiabilities() {
		return totalLiabilities;
	}
	public void setTotalLiabilities(double totalLiabilities) {
		this.totalLiabilities = totalLiabilities;
	}
	public double getCurrentDebt() {
		return currentDebt;
	}
	public void setCurrentDebt(double currentDebt) {
		this.currentDebt = currentDebt;
	}
	
}
