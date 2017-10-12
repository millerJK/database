package com.jdbc.database;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.Test;

/**
 * Dbutils ������
 * @author wangning
 *
 */
public class DbUtilsTest {
	
	
	class MyResultSetHandler<T> implements ResultSetHandler<T>{

		@Override
		public T handle(ResultSet resultSet) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	@Test
	public void test() {
		// 1����QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		// 2.ʹ����update����
		// String sql = "delete from examstudent where FlowID in(?,?)";
		// String sql = "insert into examstudent(FlowID,Type,IdCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?,?)";
		String sql = "update examstudent set FlowID = ? , StudentName = ? where FlowID = ?";
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			// ʹ��update����
			queryRunner.update(connection, sql, 24, "����Ÿ", 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JdbcUtils.releaseDb(null, null, connection);
	}

	@Test
	public void query() {
		// 1����QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert * from examstudent";
		Connection connection = null;
		connection = JdbcUtils.getConnection();
		try {
//			Student student = (Student) queryRunner.query(connection, sql, new MyResultSetHandler<Student>());
//			queryRunner.query(connection, sql, new BeanHandler(Student.class));//BeanHandler  �ѽ�����ĵ�һ����ת��Ϊ������BeanHandler�������class.
//			List<Student> lists = (List<Student>) queryRunner.query(connection, sql, new BeanListHandler(Student.class));//BeanHandler  
			Map<String ,Object> lists = queryRunner.query(connection, sql, new MapHandler());//����sql ����Ӧ�ĵ�һ����¼��Ӧ��map����
			queryRunner.query(connection, sql, new MapListHandler());//����sql ����Ӧ�ĵ�һ����¼��Ӧ��map����
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcUtils.releaseDb(null, null, connection);
		
	}
	
	@Test
	public void testBeanHandler(){
		
	}

}
