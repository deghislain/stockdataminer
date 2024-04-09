package com.stock.stockdataminer.model;

public class StockCashFlowData extends FundStockData{
	private double operatingCashFlow;
	private double changeInventory;
	private double profitLoss;
	private double dividendPayout;
	private double capitalExpenditure;
	private double cashflowFromInvestment;
	private double cashflowFromFinancing;
	private double changeInOperatingLiabilities;
	private double changeInOperatingAssets;
	
	
	
	
	public double getOperatingCashFlow() {
		return operatingCashFlow;
	}
	public void setOperatingCashFlow(double operatingCashFlow) {
		this.operatingCashFlow = operatingCashFlow;
	}
	public double getChangeInventory() {
		return changeInventory;
	}
	public void setChangeInventory(double changeInventory) {
		this.changeInventory = changeInventory;
	}
	public double getProfitLoss() {
		return profitLoss;
	}
	public void setProfitLoss(double profitLoss) {
		this.profitLoss = profitLoss;
	}
	public double getDividendPayout() {
		return dividendPayout;
	}
	public void setDividendPayout(double dividendPayout) {
		this.dividendPayout = dividendPayout;
	}
	public double getCapitalExpenditure() {
		return capitalExpenditure;
	}
	public void setCapitalExpenditure(double capitalExpenditure) {
		this.capitalExpenditure = capitalExpenditure;
	}
	public double getCashflowFromInvestment() {
		return cashflowFromInvestment;
	}
	public void setCashflowFromInvestment(double cashflowFromInvestment) {
		this.cashflowFromInvestment = cashflowFromInvestment;
	}
	public double getCashflowFromFinancing() {
		return cashflowFromFinancing;
	}
	public void setCashflowFromFinancing(double cashflowFromFinancing) {
		this.cashflowFromFinancing = cashflowFromFinancing;
	}
	public double getChangeInOperatingLiabilities() {
		return changeInOperatingLiabilities;
	}
	public void setChangeInOperatingLiabilities(double changeInOperatingLiabilities) {
		this.changeInOperatingLiabilities = changeInOperatingLiabilities;
	}
	public double getChangeInOperatingAssets() {
		return changeInOperatingAssets;
	}
	public void setChangeInOperatingAssets(double changeInOperatingAssets) {
		this.changeInOperatingAssets = changeInOperatingAssets;
	}
	
	

}
