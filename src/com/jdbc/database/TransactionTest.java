package com.jdbc.database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

/**
 * ÊÂÎñ
 * @author wangning
 *
 */
public class TransactionTest {

	@Test
	public void test() {
		Connection connection = JdbcUtils.getConnection();
		if(connection == null)
			return;
		JdbcUtils.beginTx(connection);
		try {
			String sql = "UPDATE settle SET money = money - 500 WHERE id = ?";
			update(connection,sql,1);
			int i = 10/0;
			sql = "UPDATE settle SET money = money + 500 WHERE id = ?";
			update(connection,sql,2);
			JdbcUtils.commit(connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JdbcUtils.rollBack(connection);
		}
		JdbcUtils.commit(connection);
		JdbcUtils.releaseDb(null, null, connection);
	}
	
	
	
	private void update(Connection connection,String sql, Object... params) throws SQLException {

		PreparedStatement statement = null;

		if (connection == null)
			return;

			statement = connection.prepareStatement(sql);
			int length = params.length;
			for (int i = 0; i < length; i++) {
				statement.setObject(i + 1, params[i]);
			}
			statement.executeUpdate();
	
	}

}
