package com.jdbc.database;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.print.attribute.standard.PresentationDirection;




import org.apache.commons.beanutils.BeanUtils;

import com.mysql.jdbc.StringUtils;

public class JdbcUtils {
	
	
	public static void beginTx(Connection connection){
		if(connection != null){
			try {
				connection.setAutoCommit(false);
				connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void commit(Connection connection){
		if(connection != null){
			try {
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void rollBack(Connection connection){
		
		if(connection != null){
			try {
				connection.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static Connection getConnection() {

		Connection connection = null;
		InputStream inputStream = JdbcUtils.class
				.getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		try {
			properties.load(inputStream);
			String driver = properties.getProperty("driver");
			String user = properties.getProperty("user");
			String password = properties.getProperty("password");
			String jdbcUrl = properties.getProperty("jdbcUrl");
			Class.forName(driver);
			connection = DriverManager.getConnection(jdbcUrl, user, password);
			return connection;
		} catch (IOException | ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 增加 删除 修改 ，不能进行查询
	 * 不推荐使用，Statement有sql注入的风险,请使用{@link #update(String, Object...)}}
	 * @param sql
	 */
	@Deprecated
	public static void update(String sql) {

		Connection connection = getConnection();
		Statement statement = null;

		if (connection == null)
			return;

		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			releaseDb(statement, null, connection);
		}

	}

	/**
	 * 适合 增加 删除 修改 ，不能进行查询 sql : insert into table values(?,?,?,?,?);
	 * 
	 * @param sql
	 * @param params
	 */
	public static void update(String sql, Object... params) {

		Connection connection = getConnection();
		PreparedStatement statement = null;

		if (connection == null)
			return;

		try {
			statement = connection.prepareStatement(sql);
			int length = params.length;
			for (int i = 0; i < length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			releaseDb(statement, null, connection);
		}
	}

	private static boolean isNullOrEmpty(String toTest) {
		return (toTest == null || toTest.length() == 0);
	}

	/**
	 * tableName 保存全部数据 针对 sql :insert into table values(?,?,?,?,?);
	 * 
	 * @param
	 * @param params
	 */
	public static void insert(String tableName, Object... params) {

		int length = params.length;
		if (length == 0 || isNullOrEmpty(tableName))
			throw new IllegalArgumentException(
					"tableName or params can't be null");

		Connection connection = getConnection();
		PreparedStatement statement = null;

		if (connection == null)
			return;

		String sql = "INSERT INTO " + tableName + " VALUES(";

		StringBuilder builder = new StringBuilder(sql);
		for (int i = 0; i < length; i++) {
			builder.append("?,");
		}
		builder.deleteCharAt(builder.toString().length() - 1).append(")");
		sql = builder.toString();
		try {
			statement = connection.prepareStatement(sql);
			for (int i = 0; i < length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			releaseDb(statement, null, connection);
		}
	}

	/**
	 * 只是保存部分数据 针对sql : insert into table(?,?) values(?,?);
	 * 
	 * @param tableName
	 * @param params
	 */
	public static void add2(String tableName, HashMap<String, Object> params) {

		int length = params.entrySet().size();

		if (isNullOrEmpty(tableName) || params == null)
			throw new IllegalArgumentException(
					"tableName and map can't be null");

		Connection connection = getConnection();
		PreparedStatement statement = null;

		if (connection == null)
			return;

		String sql = "INSERT INTO " + tableName + "(";
		StringBuilder builder = new StringBuilder(sql);

		Object[] keys = params.keySet().toArray();

		for (int i = 0; i < length; i++) {
			builder.append(keys[i] + ",");
		}

		builder.deleteCharAt(builder.toString().length() - 1).append(")")
				.append(" VALUES(");

		for (int i = 0; i < length; i++) {
			builder.append("?,");
		}
		builder.deleteCharAt(builder.toString().length() - 1).append(")");
		sql = builder.toString();

		try {
			statement = connection.prepareStatement(sql);
			Iterator<Entry<String, Object>> iterator = params.entrySet()
					.iterator();
			int i = 1;
			while (iterator.hasNext()) {
				Entry<String, Object> entry = iterator.next();
				statement.setObject(i, entry.getValue());
				i++;
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			releaseDb(statement, null, connection);
		}
	}

	/**
	 * 查询
	 * 
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	public static <T> List<T> query(Class<T> clazz, String sql,
			Object... params) {

		Connection connection = getConnection();
		if (connection == null)
			return null;

		List<T> lists = new ArrayList();
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSetMetaData resultSetMetaData = null;

		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				preparedStatement.setObject((i + 1), params[i]);
			}

			resultSet = preparedStatement.executeQuery();
			resultSetMetaData = resultSet.getMetaData();
			int colunmLenght = resultSetMetaData.getColumnCount();
			
			while (resultSet.next()) {
				T t = (T) clazz.forName(clazz.getName()).newInstance();
				for (int i = 1; i <= colunmLenght; i++) {
					String label = resultSetMetaData.getColumnLabel(i);
					Object value = resultSet.getObject(i);
//					t = reflect(t, label, value); 使用反射也是可以的。
					BeanUtils.setProperty(t, label, value);
				}
				lists.add(t);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		releaseDb(preparedStatement, resultSet, connection);
		return lists;

	}

	/**
	 * 利用反射进行赋值
	 * 
	 * @param object
	 *            被赋值对象
	 * @param label
	 * @param value
	 */
	private static <T> T reflect(T t, String label, Object value) {

		Class<?> obj = t.getClass();
		Field[] fields = obj.getDeclaredFields();// 获取类中定义的成员变量即使修饰符是private也是可以获取的到
		int lenght = fields.length;
		for (int i = 0; i < lenght; i++) {
			fields[i].setAccessible(true);
			if (fields[i].getName().equalsIgnoreCase(label)) {
				try {
					fields[i].set(t, value);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return t;

	}

	public static void releaseDb(Statement statement, ResultSet resultSet,
			Connection connection) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
