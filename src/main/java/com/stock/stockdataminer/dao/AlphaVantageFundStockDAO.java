package com.stock.stockdataminer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.stock.stockdataminer.model.StockBalanceSheetData;
import com.stock.stockdataminer.model.StockCashFlowData;
import com.stock.stockdataminer.model.StockGenericInfo;
import com.stock.stockdataminer.model.StockIncomeStatData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageFundStockDAO {
	
	private HikariDataSource dataSource;

	public AlphaVantageFundStockDAO() {
		try {
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(System.getenv("jdbcUrl"));
			config.setUsername(System.getenv("dbUsername"));
			config.setPassword(System.getenv("dbPassword"));
			config.setDriverClassName(org.postgresql.Driver.class.getName());
			config.setMaximumPoolSize(10); // Adjust pool size as needed
			dataSource = new HikariDataSource(config);
		} catch (Exception e) {
			log.error("Error: Unable to open the application properties file");
		}
	}
	
	public int saveIncomeStockData(StockIncomeStatData fsd) {
		return this.saveIncomeStatement(fsd);
	}
	
	public int saveBalanceSheetStockData(StockBalanceSheetData fsd) {
		return this.saveBalanceSheet(fsd);
	}
	
	public int saveCashFlowStockData(StockCashFlowData fsd) {
		return this.saveCashFlowt(fsd);
	}
	
	public int saveGenericStockData(StockGenericInfo fsd) {
		return this.saveStockGenericInfo(fsd);
	}
	
	private int saveIncomeStatement(StockIncomeStatData fsd) {
		log.info("saveIncomeStatement Start");
		int result = 0;
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO income_stat_stock_data" 
					+ " (stock_symbol,stock_fiscale_date,stock_operating_income,stock_gross_profit,stock_total_revenue,stock_resh_and_dev,stock_net_income)\r\n"
					+ "							VALUES(?,?,?,?,?,?,?)");

			statement.setString(1, fsd.getStockSymbol());
			statement.setDate(2, java.sql.Date.valueOf(fsd.getStockfiscaleDate()));
			statement.setDouble(3, fsd.getOperatingIncome());
			statement.setDouble(4, fsd.getGrossProfit());
			statement.setDouble(5, fsd.getTotalRevenue());
			statement.setDouble(6, fsd.getReschAndDev());
			statement.setDouble(7, fsd.getNetIncome());

			result = statement.executeUpdate();
		} catch (SQLException e) {
			// Handle exceptions
			System.err.format("******************SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			log.error("Error: Unable to store income stock data {}", e);
		} catch (Exception e) {
			// Handle exceptions
			log.error("Error: Unable to store income stock data {}", e);
		}
		log.info("saveIncomeStatement End");
		return result;
	}
	
	private int saveBalanceSheet(StockBalanceSheetData fsd) {
		log.info("saveBalanceSheet Start");
		int result = 0;
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO balance_sheet_stock_data" 
					+ " (stock_symbol,stock_fiscale_date,total_shareholder_equity,total_assets,cash_short_term_investments,inventory,investments, long_term_investments, total_liabilities, current_debt)\r\n"
					+ "							VALUES(?,?,?,?,?,?,?,?,?,?)");

			statement.setString(1, fsd.getStockSymbol());
			statement.setDate(2, java.sql.Date.valueOf(fsd.getStockfiscaleDate()));
			statement.setDouble(3, fsd.getTotalShareholderEquity());
			statement.setDouble(4, fsd.getTotalAssets());
			statement.setDouble(5, fsd.getCashAndShortTermInvestments());
			statement.setDouble(6, fsd.getInventory());
			statement.setDouble(7, fsd.getInvestments());
			statement.setDouble(8, fsd.getLongTermInvestments());
			statement.setDouble(9, fsd.getTotalLiabilities());
			statement.setDouble(10, fsd.getCurrentDebt());

			result = statement.executeUpdate();
		} catch (SQLException e) {
			// Handle exceptions
			System.err.format("******************SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			log.error("Error: Unable to store balance stock data {}", e);
		} catch (Exception e) {
			// Handle exceptions
			log.error("Error: Unable to store balance stock data {}", e);
		}
		log.info("saveBalanceSheet End");
		return result;
	}
	
	private int saveCashFlowt(StockCashFlowData fsd) {
		log.info("saveCashFlowt Start");
		int result = 0;
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO cash_flow_stock_data" 
					+ " (stock_symbol,stock_fiscale_date,change_inventory,profit_loss,dividend_payout,capital_expenditure,cash_from_investment, cash_from_financing, change_op_liabilities, change_op_assets, operating_cash_flow)\r\n"
					+ "							VALUES(?,?,?,?,?,?,?,?,?,?,?)");

			statement.setString(1, fsd.getStockSymbol());
			statement.setDate(2, java.sql.Date.valueOf(fsd.getStockfiscaleDate()));
			statement.setDouble(3, fsd.getChangeInventory());
			statement.setDouble(4, fsd.getProfitLoss());
			statement.setDouble(5, fsd.getDividendPayout());
			statement.setDouble(6, fsd.getCapitalExpenditure());
			statement.setDouble(7, fsd.getCashflowFromInvestment());
			statement.setDouble(8, fsd.getCashflowFromFinancing());
			statement.setDouble(9, fsd.getChangeInOperatingLiabilities());
			statement.setDouble(10, fsd.getChangeInOperatingAssets());
			statement.setDouble(11, fsd.getOperatingCashFlow());

			result = statement.executeUpdate();
		} catch (SQLException e) {
			// Handle exceptions
			System.err.format("******************SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			log.error("Error: Unable to store cash flow stock data {}", e);
		} catch (Exception e) {
			// Handle exceptions
			log.error("Error: Unable to store cash flow stock data {}", e);
		}
		log.info("saveCashFlowt End");
		return result;
	}

	private int saveStockGenericInfo(StockGenericInfo fsd) {
		log.info("saveStockGenericInfo Start");
		int result = 0;
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO stock_generic_info_data" 
					+ " (stock_symbol,stock_fiscale_date,stock_reported_eps)\r\n"
					+ "							VALUES(?,?,?)");

			statement.setString(1, fsd.getStockSymbol());
			statement.setDate(2, java.sql.Date.valueOf(fsd.getStockfiscaleDate()));
			statement.setDouble(3, fsd.getReportedEPS());

			result = statement.executeUpdate();
		} catch (SQLException e) {
			// Handle exceptions
			System.err.format("******************SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			log.error("Error: Unable to store generick info data {}", e);
		} catch (Exception e) {
			// Handle exceptions
			log.error("Error: Unable to store generic info stock data {}", e);
		}
		log.info("saveStockGenericInfo End");
		return result;
	}
}
