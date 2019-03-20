package com.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

		private DataSource datasource;
		
		public StudentDbUtil(DataSource thedatasource) {
			datasource = thedatasource;
		}
		
		public List<Student> getStudents() throws Exception{
			List<Student> students = new ArrayList<>();
			Connection myConn = null;
			Statement myStmt = null;
			ResultSet myRs = null;
			
			try {
				//get a connection
				myConn = datasource.getConnection();
				//create query
				String sql = "Select * from student order by last_name";
				myStmt = myConn.createStatement();
				
				// execute query
				myRs = myStmt.executeQuery(sql);
				
				///process query
				while(myRs.next()) {
					//retrieve data from result set row
					int id = myRs.getInt("id");
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String email = myRs.getString("email");
					
					//create new student object
					Student tempStudent = new Student(id, firstName, lastName, email);
					
					
					//add it to list
					students.add(tempStudent);
					
					
				}
				
				return students;
				//close jdbc object
			}finally {
				//close jdbc object
				close(myConn,myStmt, myRs);
			}
			
			
			
			
		}

		private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
			
			try {
			if(myConn!=null) {
				myConn.close();
			}
			if(myRs !=null) {
				myRs.close();
			}
			if(myStmt !=null) {
				myStmt.close();
			}
			}catch(Exception exc) {
				exc.printStackTrace();
			}
		}

		public void addStudent(Student theStudent) throws Exception {

			Connection myConn = null;
			PreparedStatement myStmt = null;
			
			try {
				// get db connection
				myConn = datasource.getConnection();
				
				// create sql for insert
				String sql = "insert into student "
						   + "(first_name, last_name, email) "
						   + "values (?, ?, ?)";
				
				myStmt = myConn.prepareStatement(sql);
				
				// set the param values for the student
				myStmt.setString(1, theStudent.getFirstName());
				myStmt.setString(2, theStudent.getLastName());
				myStmt.setString(3, theStudent.getEmail());
				
				// execute sql insert
				myStmt.execute();
			}
			finally {
				// clean up JDBC objects
				close(myConn, myStmt, null);
			}
		}

		public Student getStudents(String theStudentId) throws Exception{
			Student theStudent = null;
			
			Connection myConn = null;
			PreparedStatement myStmt = null;
			ResultSet myRs = null;
			int studentId;
			
			try {
				
				//convert student id to int
				studentId = Integer.parseInt(theStudentId);
				
				//get connection
				myConn = datasource.getConnection();
				
				//create sql
				String sql = "select * from student where id=?";
				
				//create prepared statement
				myStmt = myConn.prepareStatement(sql);
				
				//set params
				myStmt.setInt(1, studentId);
				
				//excute sql
				myRs = myStmt.executeQuery();
				
				//retrieve data from result set now
				if(myRs.next()) {
					String firstName = myRs.getString("first_name");
					String lastName = myRs.getString("last_name");
					String email = myRs.getString("email");
					
					theStudent = new Student(studentId, firstName, lastName, email);
					
					
				}
				else {
					throw new Exception("Could not find the Student with ID : " +studentId);
				}
				
				
				return theStudent;	
				
			}finally {
				
				//clean jdbc object
				close(myConn, myStmt, myRs);
			}
			
			
		}

		public void updateStudent(Student theStudent) throws Exception {
			
			Connection myConn = null;
			PreparedStatement myStmt = null;
			
			try {
			//get conn
			myConn = datasource.getConnection();
			
			//create sql
			String sql = "update student " + "set first_name=?, last_name=?, email=?" + "where id=?";
			
			//prepare statement
			myStmt = myConn.prepareStatement(sql);
			
			//set param
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			
			//excute query
			myStmt.execute();
			}finally {
				close(myConn, myStmt, null);
			}
			
					
		}

		public void deleteStudent(String theStudentId) throws Exception {
			
			Connection myConn = null;
			PreparedStatement myStmt = null;
			
			try {
				//convert student id to integer
				int studentId = Integer.parseInt(theStudentId);
				
				//get connection to database
				myConn = datasource.getConnection();
				
				//create sql to delete student
				String sql = "delete from student where id=?";
				
				//prepare statement
				myStmt = myConn.prepareStatement(sql);
				
				//set params
				myStmt.setInt(1, studentId);
				
				// excute sql query
				myStmt.execute();
				
			}finally {
				
				//clean up
				close(myConn, myStmt, null);
				
			}
			
			
		}
}
