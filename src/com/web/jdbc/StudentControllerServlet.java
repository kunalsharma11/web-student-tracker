package com.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private StudentDbUtil studentDbUtil;
	@Resource(name="jdbc/web_student_tracker")
	private DataSource datasource;
	
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		
		//create student and pass in conn pool/datasource
		try {
		studentDbUtil = new StudentDbUtil(datasource);
		}catch(Exception exc) {
			throw new ServletException(exc);
		}
	}


	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// list the students in MVC
		try {
			
			//read the command
			String theCommand = request.getParameter("command");
			
			if(theCommand==null) {
				theCommand = "LIST";
			}
			//route it to appropriate method
			switch(theCommand) {
			case "LIST":
				listStudent(request, response);
				break;
				
			case "ADD":
				addStudent(request, response);
				break;
				
			case "LOAD":
				loadStudent(request, response);
				break;
			
			case "UPDATE":
				updateStudent(request, response);
				break;
				
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudent(request, response);
			
			
			}
			
			
			//list student
		
		}catch(Exception ex) {
			throw new ServletException(ex);
		}
	}


	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//read the student id from the data
		String theStudentId = request.getParameter("studentId");
		
		//delete student from data base
		studentDbUtil.deleteStudent(theStudentId);
		
		//send back to list student page
		listStudent(request, response);
		
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read the student info from the form date
		int id= Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		//create the student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		//perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		//send them back to list student page
		listStudent(request, response);
	}


	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		//read student id from data
		String theStudentId = request.getParameter("studentId");
		
		//get student from the data base
		Student theStudent = studentDbUtil.getStudents(theStudentId);
		
		//place student in the request attribute
		request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page:update-student-form.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
	}


	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");		
		
		// create a new student object
		Student theStudent = new Student(firstName, lastName, email);
		
		// add the student to the database
		studentDbUtil.addStudent(theStudent);
				
		// send back to main page (the student list)
		listStudent(request, response);
	}


	private void listStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//get the student from db util
		List<Student> students = studentDbUtil.getStudents();
		
		//add students to the request
		request.setAttribute("STUDENT_LIST", students);
		
		//send to jsp page
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
	}

	

}
