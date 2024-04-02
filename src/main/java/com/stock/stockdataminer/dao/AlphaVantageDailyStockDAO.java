package com.stock.stockdataminer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.stock.stockdataminer.model.CoreStockData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDailyStockDAO {
	private HikariDataSource dataSource;

	public AlphaVantageDailyStockDAO() {
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

	public int saveCoreStock(CoreStockData dsd) {
		String tableName = "";
		if (dsd.getTimeSeries().contains("Weekly")) {
			tableName = "av_weekly_stock_data";
		} else {
			tableName = "av_daily_stock_data";
		}
		log.info("saveDailyStock Start");
		int result = 0;
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + tableName
					+ " (stock_symbol,curr_stock_date,stock_open,stock_high,stock_low,stock_close,stock_volume)\r\n"
					+ "							VALUES(?,?,?,?,?,?,?)");

			statement.setString(1, dsd.getStockSymbol());
			statement.setDate(2, java.sql.Date.valueOf(dsd.getCurrStockDate()));
			statement.setDouble(3, dsd.getStockOpen());
			statement.setDouble(4, dsd.getStockHigh());
			statement.setDouble(5, dsd.getStockLow());
			statement.setDouble(6, dsd.getStockClose());
			statement.setDouble(7, dsd.getStockVolume());

			result = statement.executeUpdate();
			log.info("saveDailyStock End");
		} catch (SQLException e) {
			// Handle exceptions
			System.err.format("******************SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			log.error("Error: Unable to store daily stock data {}", e);
		} catch (Exception e) {
			// Handle exceptions
			log.error("Error: Unable to store daily stock data {}", e);
		}

		return result;
	}
}
