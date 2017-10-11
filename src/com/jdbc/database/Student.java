package com.jdbc.database;

public class Student {
	
		private int flowID;
		private int type;
		private String idCard;
		private String examId;
		private String studentName;
		private String location;
		private int grade;
		
		
		
		public Student() {
			
		}

		public Student(int flowID, int type, String idCard, String examId,
				String studentName, String location, int grade) {
	
			this.flowID = flowID;
			this.type = type;
			this.idCard = idCard;
			this.examId = examId;
			this.studentName = studentName;
			this.location = location;
			this.grade = grade;
		}
		
		public int getFlowID() {
			return flowID;
		}
		public void setFlowID(int flowID) {
			this.flowID = flowID;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getIdCard() {
			return idCard;
		}
		public void setIdCard(String idCard) {
			this.idCard = idCard;
		}
		public String getExamId() {
			return examId;
		}
		public void setExamId(String examId) {
			this.examId = examId;
		}
		public String getStudentName() {
			return studentName;
		}
		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}
		public String getLocation() {
			return location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		public int getGrade() {
			return grade;
		}
		public void setGrade(int grade) {
			this.grade = grade;
		}

		@Override
		public String toString() {
			return "Student [flowID=" + flowID + ", type=" + type + ", idCard="
					+ idCard + ", examId=" + examId + ", studentName="
					+ studentName + ", location=" + location + ", grade="
					+ grade + "]";
		}
		
		
		
		
}
