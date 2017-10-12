package com.jdbc.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Driver;

/**
 * 批量处理
 * 
 * @author wangning
 *
 */
public class BatchTest {

	@Test
	public void batchTest() {

		Connection connection = null;
		PreparedStatement statement = null;

		try {
			InputStream inStream = getClass().getResourceAsStream(
					"jdbc.properties");
			Properties properties = new Properties();
			properties.load(inStream);
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			String driver = properties.getProperty("driver");
			String jdbcUrl = properties.getProperty("jdbcUrl");

			Class.forName(driver);
			connection = DriverManager.getConnection(jdbcUrl, user, password);
			String sql = "insert into settle(studentName,money) values(?,?)";
			statement = connection.prepareStatement(sql);
			long start = System.currentTimeMillis();
			for (int i = 0; i < 1000; i++) {
				statement.setObject(1, "xiaoming" + i);
				statement.setObject(2, i);
				statement.addBatch();
				statement.executeUpdate();
				if ((i + 1) % 300 == 0) {
					statement.executeBatch();
					statement.clearBatch();
				}
			}
			if (1000 % 300 != 0) {
				statement.executeBatch();
				statement.clearBatch();
			}
			System.out.println(System.currentTimeMillis() - start);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		JdbcUtils.releaseDb(statement, null, connection);

	}

}
