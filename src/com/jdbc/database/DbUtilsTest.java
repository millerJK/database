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
 * Dbutils 工具类
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
		// 1创建QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		// 2.使用其update方法
		// String sql = "delete from examstudent where FlowID in(?,?)";
		// String sql = "insert into examstudent(FlowID,Type,IdCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?,?)";
		String sql = "update examstudent set FlowID = ? , StudentName = ? where FlowID = ?";
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			// 使用update方法
			queryRunner.update(connection, sql, 24, "李夏鸥", 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JdbcUtils.releaseDb(null, null, connection);
	}

	@Test
	public void query() {
		// 1创建QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		String sql = "insert * from examstudent";
		Connection connection = null;
		connection = JdbcUtils.getConnection();
		try {
//			Student student = (Student) queryRunner.query(connection, sql, new MyResultSetHandler<Student>());
//			queryRunner.query(connection, sql, new BeanHandler(Student.class));//BeanHandler  把结果集的第一条就转换为创建的BeanHandler对象传入的class.
//			List<Student> lists = (List<Student>) queryRunner.query(connection, sql, new BeanListHandler(Student.class));//BeanHandler  
			Map<String ,Object> lists = queryRunner.query(connection, sql, new MapHandler());//返回sql 对相应的第一条记录对应的map对象
			queryRunner.query(connection, sql, new MapListHandler());//返回sql 对相应的第一条记录对应的map对象
			
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
