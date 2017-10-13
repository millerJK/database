package com.jdbc.database;

import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

/**
 * Dbutils 工具类
 * @author wangning
 *
 */
public class DbUtilsTest {
	
	//使用了反射对对象进行赋值
	class MyResultSetHandler<T> implements ResultSetHandler<T>{
		
		private T t;
		
		 public MyResultSetHandler(Class<T> t ) {
			// TODO Auto-generated constructor stub
			 try {
				this.t = (T) t.forName(t.getName()).newInstance();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//在这一步同样要使用到反射
		@Override
		public T handle(ResultSet resultSet) throws SQLException {
			// TODO Auto-generated method stub
		
		resultSet.next();
		ResultSetMetaData metaData = resultSet.getMetaData();
		
		for(int i = 1 ; i <= metaData.getColumnCount();i++){	
			JdbcUtils.reflect(t, metaData.getColumnLabel(i), resultSet.getObject(i));
		}
	
			return t;
			
		}
		
	}
	
	/**
	 * 增加  删除 更新 操作
	 */
	@Test
	public void testUpdate() {
		
		Connection connection =  JdbcUtils.getConnection2();
		if(connection == null){
			return;
		}
		
		// 1创建QueryRunner
		QueryRunner queryRunner = new QueryRunner();
	
//		 String sql = "delete from examstudent where FlowID in(?,?)";
		 String sql = "insert into examstudent(FlowID,Type,IdCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?,?)";
//		 String sql ="update examstudent set Location = ? , StudentName = ? where FlowID = ?";
		 
		try {
			// 2.使用其update方法
//			queryRunner.update(connection, sql, 1,2);
			Student student = queryRunner.insert(connection, sql, new MyResultSetHandler<Student>(Student.class), 101,4,"21234346467234","201205980111","零","老挝",50);
			System.out.println(student.toString());
//			queryRunner.update(connection, sql,"英国","珐琅彩",3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcUtils.releaseDb(null, null, connection);
	}
	
	@Test
	public void testQuery() {
		// 1创建QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		String sql = "select * from examstudent ";
		Connection connection = null;
		connection = JdbcUtils.getConnection2();
		try {
//			Student student = (Student) queryRunner.query(connection, sql, new MyResultSetHandler(Student.class));
			Student student = (Student) queryRunner.query(connection, sql, new BeanHandler(Student.class));//BeanHandler  把结果集的第一条就转换为创建的BeanHandler对象传入的class.
//			List<Student> lists = (List<Student>) queryRunner.query(connection, sql, new BeanListHandler(Student.class));//BeanHandler 
			
//			Map<String ,Object> lists = queryRunner.query(connection, sql, new MapHandler());//返回sql 对相应的第一条记录对应的map对象
//			Set<String> keys = lists.keySet();
//			Iterator<String> iterator = keys.iterator();
//			while(iterator.hasNext()){
//				String key = iterator.next();
//				System.out.println("key :  "+key +"   value:  "+lists.get(key));
//			}
			
			
			List<Map<String,Object>> listMaps = queryRunner.query(connection, sql, new MapListHandler());//将结果转换为一个map的list，map对应查询的一条记录
			if(listMaps.size() != 0){
				for(Map<String,Object> listMap : listMaps){
					Iterator<String> iterator = listMap.keySet().iterator();
					while(iterator.hasNext()){
						String key = iterator.next();
						System.out.println("key :  "+key +"   value:  "+listMap.get(key));
					}
					
				}
			}
			
			
			sql = "select studentName from examstudent where flowid = 1";
			String studentName = queryRunner.query(connection,sql, new ScalarHandler<String>());//ScalarHandler() 把结果集转换为一个数值（可以是任意基本类型）
			System.out.println(studentName);
			

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
