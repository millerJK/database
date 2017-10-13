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
 * Dbutils ������
 * @author wangning
 *
 */
public class DbUtilsTest {
	
	//ʹ���˷���Զ�����и�ֵ
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
		//����һ��ͬ��Ҫʹ�õ�����
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
	 * ����  ɾ�� ���� ����
	 */
	@Test
	public void testUpdate() {
		
		Connection connection =  JdbcUtils.getConnection2();
		if(connection == null){
			return;
		}
		
		// 1����QueryRunner
		QueryRunner queryRunner = new QueryRunner();
	
//		 String sql = "delete from examstudent where FlowID in(?,?)";
		 String sql = "insert into examstudent(FlowID,Type,IdCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?,?)";
//		 String sql ="update examstudent set Location = ? , StudentName = ? where FlowID = ?";
		 
		try {
			// 2.ʹ����update����
//			queryRunner.update(connection, sql, 1,2);
			Student student = queryRunner.insert(connection, sql, new MyResultSetHandler<Student>(Student.class), 101,4,"21234346467234","201205980111","��","����",50);
			System.out.println(student.toString());
//			queryRunner.update(connection, sql,"Ӣ��","���Ų�",3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcUtils.releaseDb(null, null, connection);
	}
	
	@Test
	public void testQuery() {
		// 1����QueryRunner
		QueryRunner queryRunner = new QueryRunner();
		String sql = "select * from examstudent ";
		Connection connection = null;
		connection = JdbcUtils.getConnection2();
		try {
//			Student student = (Student) queryRunner.query(connection, sql, new MyResultSetHandler(Student.class));
			Student student = (Student) queryRunner.query(connection, sql, new BeanHandler(Student.class));//BeanHandler  �ѽ�����ĵ�һ����ת��Ϊ������BeanHandler�������class.
//			List<Student> lists = (List<Student>) queryRunner.query(connection, sql, new BeanListHandler(Student.class));//BeanHandler 
			
//			Map<String ,Object> lists = queryRunner.query(connection, sql, new MapHandler());//����sql ����Ӧ�ĵ�һ����¼��Ӧ��map����
//			Set<String> keys = lists.keySet();
//			Iterator<String> iterator = keys.iterator();
//			while(iterator.hasNext()){
//				String key = iterator.next();
//				System.out.println("key :  "+key +"   value:  "+lists.get(key));
//			}
			
			
			List<Map<String,Object>> listMaps = queryRunner.query(connection, sql, new MapListHandler());//�����ת��Ϊһ��map��list��map��Ӧ��ѯ��һ����¼
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
			String studentName = queryRunner.query(connection,sql, new ScalarHandler<String>());//ScalarHandler() �ѽ����ת��Ϊһ����ֵ������������������ͣ�
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
