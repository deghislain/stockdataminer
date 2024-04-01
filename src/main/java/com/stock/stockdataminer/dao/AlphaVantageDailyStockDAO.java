package com.stock.stockdataminer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.stock.stockdataminer.model.DailyStockData;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlphaVantageDailyStockDAO{
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
	
	public int saveDailyStock(DailyStockData dsd) {
		log.info("saveDailyStock Start");
		int result = 0;
		try (Connection connection = dataSource.getConnection()) {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO av_daily_stock_data (symbol,curr_stock_date,day_open,day_high,day_low,day_close,day_volume)\r\n"
					+ "							VALUES(?,?,?,?,?,?,?)");
			
			statement.setString(1, dsd.getSymbol());
			statement.setDate(2, java.sql.Date.valueOf(dsd.getCurrStockDate()));
			statement.setDouble(3, dsd.getDayOpen());
			statement.setDouble(4, dsd.getDayHigh());
			statement.setDouble(5, dsd.getDayLow());
			statement.setDouble(6, dsd.getDayClose());
			statement.setDouble(7, dsd.getDayVolume());
			
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
