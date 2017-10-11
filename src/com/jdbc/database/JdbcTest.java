package com.jdbc.database;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;



public class JdbcTest {

	@Test
	public void test() {
		Connection connection = JdbcUtils.getConnection();
		if(connection == null)
			return;
		String sql = "insert into examstudent(Type,IdCard,ExamCard,StudentName,Location,Grade) values(?,?,?,?,?,?)";
		Object[] objects = {4,"348799454697895","23234","李小花","美国",634};
		try {
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			objects[3] = "李晓华"+System.currentTimeMillis();
			for(int i =0;i<6;i++){
				statement.setObject((i+1),objects[i]);
			}
			statement.executeUpdate();
			ResultSet set = statement.getGeneratedKeys();
			if(set.next()){
				System.out.println(set.getObject(1));
			}
			
			ResultSetMetaData metaData = set.getMetaData();
			for(int i = 0;i <metaData.getColumnCount();i++){
				System.out.println(metaData.getColumnName(i+1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void insertPic(){
		Connection connection = JdbcUtils.getConnection();
		if(connection == null)
			return;
		String sql = "insert into info(studentName,head) values(?,?)";
		Object[] objects = new Object[2];
		objects[0] = "李晓华"+System.currentTimeMillis();
	
		try {
			objects[1] =  new FileInputStream("flower.jpg");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		PreparedStatement statement = null;
	
		try {
			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			for(int i =0;i<objects.length;i++){
				statement.setObject((i+1),objects[i]);
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JdbcUtils.releaseDb(statement, null, connection);
	}
	
	@Test
	public void readBlob(){
		Connection connection = JdbcUtils.getConnection();
		if(connection == null)
			return;
		
		String sql = "select * from info";
		PreparedStatement statement=null;
		ResultSet set = null;
		InputStream inputStream;
		FileOutputStream outputStream = null;
		
		try {
			outputStream = new FileOutputStream("out.jpg");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			statement = connection.prepareStatement(sql);
			set = statement.executeQuery();
			
			if(set.next()){
				Blob blob = set.getBlob(3);
				inputStream = blob.getBinaryStream();
				
				byte[] bs = new byte[1024];
				while((inputStream.read(bs))!= -1){
					outputStream.write(bs,0,bs.length);
				}
				outputStream.flush();
				inputStream.close();
				outputStream.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JdbcUtils.releaseDb(statement, set, connection);
	}

}
