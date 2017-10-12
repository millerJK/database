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
 * ���ݿ����ӳز�������
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
			
			//ָ�����ݿ����ӳ��г�ʼ�����Ӹ���
			dataSource.setInitialSize(10);
			//���ݿ����ӳ����������������
			dataSource.setMaxIdle(10);
			//���ݿ����ӳ���С����������
			dataSource.setMinIdle(5);
			//�������ݿ������������ ����IDEL��USING �����������ݸ�������������������
			dataSource.setMaxTotal(20);
			//�ȴ����ݿ�������ӵ��ʱ�䣬��λ�Ǻ��룬������ʱ�仹δ�������׳��쳣
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
