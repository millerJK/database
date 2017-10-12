package com.jdbc.database;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.dbcp2.DataSourceConnectionFactory;
import org.junit.Test;

/**
 * 数据库连接池测试用例
 * 
 * DBCP
 * c3p0
 * 
 * @author wangning
 *
 */
public class DatabasePoolTest {

	@Test
	public void dbcpTest() {
		
		BasicDataSource dataSource  = new BasicDataSource();
		
		InputStream inStream = getClass().getResourceAsStream(
				"jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(inStream);
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			String driver = properties.getProperty("driver");
			String jdbcUrl = properties.getProperty("jdbcUrl");
			
			dataSource.setDriverClassName(driver);
			dataSource.setUrl(jdbcUrl);
			dataSource.setUsername(user);
			dataSource.setPassword(password);
			
			//指定数据库连接池中初始化连接个数
			dataSource.setInitialSize(10);
			//数据库连接池中最大闲置连接数
			dataSource.setMaxIdle(10);
			//数据库连接池最小闲置连接数
			dataSource.setMinIdle(5);
			//设置数据库中最大连接数 包括IDEL和USING 连接数，传递负数代码无限制连接数
			dataSource.setMaxTotal(20);
			//等待数据库分配连接的最长时间，单位是毫秒，超出改时间还未连接则抛出异常
			dataSource.setMaxWaitMillis(5*1000);
			
			Connection connection = dataSource.getConnection();
			System.out.println(connection);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void dbcpFactory(){
	
		try {
			InputStream inStream = getClass().getResourceAsStream("dbcp.properties");
			Properties properties = new Properties();
			properties.load(inStream);
			DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
			System.out.println(dataSource.getConnection());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void getConnection(){
		Connection connection = JdbcUtils.getConnection2();
		System.out.println(connection);
	}

}
