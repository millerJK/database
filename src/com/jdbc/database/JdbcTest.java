package com.jdbc.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.junit.Test;



public class JdbcTest {
	
	@Test
	public void update(){
		
		Student student = getDataFromConsole();
		
		String sql = "INSERT INTO examstudent values("
				+student.getFlowID()
				+","
				+student.getType()
				+",'"
				+student.getIdCard()
				+"','"
				+student.getExamId()
				+"','"
				+student.getStudentName()
				+"','"
				+student.getLocation()
				+"',"
				+student.getGrade()
				+")";
		
		JdbcUtils.update(sql);
		
	}
	
	
	@Test
	public void update2(){
		Student student = new Student(9,6,"234234","112323","æ≤œ„","»’±æ",234);
		
		HashMap<String, Object> values = new HashMap<String,Object>();
		values.put("FlowID", student.getFlowID());
		values.put("Type", student.getType());
		values.put("IdCard", student.getIdCard());
		values.put("ExamCard", student.getExamId());
		values.put("StudentName", student.getStudentName());
		values.put("Grade", student.getGrade());                                                                       
		
		JdbcUtils.add2("examstudent",values);
		
	}
	
	@Test
	public void query(){
		
		String sql = "SELECT * FROM examstudent WHERE FlowID = '12'";
		
		Connection connection = JdbcUtils.getConnection();
		if(connection == null)
			return;
		try {
//			Statement statement = connection.createStatement();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeQuery();
			ResultSet set = statement.executeQuery(sql);
			while(set.next()){
				if(set.getInt("FlowID") == 12){
					Student student = new Student(set.getInt(1)
							,set.getInt(2)
							,set.getString(3)
							,set.getString("ExamCard")
							,set.getString(5)
							,set.getString(6)
							,set.getInt(7));
					System.out.println(student.toString());
				}
					
			}
			JdbcUtils.releaseDb(null, set, connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	private Student getDataFromConsole() {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		Student student = new Student();
		System.out.print("FlowId:");
		student.setFlowID(scanner.nextInt());
		System.out.print("type:");
		student.setType(scanner.nextInt());
		System.out.print("idCard:");
		student.setIdCard(scanner.next());
		System.out.print("examCard:");
		student.setExamId(scanner.next());
		System.out.print("studentName:");
		student.setStudentName(scanner.next());
		System.out.print("location:");
		student.setLocation(scanner.next());
		System.out.print("Grade:");
		student.setGrade(scanner.nextInt());
		return student;
	}
}
