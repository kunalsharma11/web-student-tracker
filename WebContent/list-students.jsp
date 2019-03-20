<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<!Doctype HTML>

<html>
<head><title>Student Tracker App</title></head>
<link type = "text/css" rel = "stylesheet" href = "css/style.css" />

<body>

<div id = "wrapper">
<div id = "header">
<h2>Foobar University</h2>

</div>


</div>

<div id ="container">
	<div id = "content">
	
	<input type = "button" value = "Add Student" onclick = "window.location.href= 'add-student-form.jsp' ; return false;"
	class = "add-student-button"/>
	
	<table>
	<tr>
	<th>First Name</th>
	<th>Last Name</th>
	<th>Email</th>
	<th>Action</th>
	</tr>
	
	<c:forEach var= "temp" items="${STUDENT_LIST}">
	<!-- set up a link for update student in action column -->
		<c:url var="tempLink" value="StudentControllerServlet">
			<c:param name="command" value="LOAD"/>
			<c:param name="studentId" value="${temp.id}"/>
		</c:url>
		
	<!-- set up a delete link -->
		<c:url var="deleteLink" value="StudentControllerServlet">
			<c:param name="command" value="DELETE"/>
			<c:param name="studentId" value="${temp.id}"/>
		</c:url>
		
		<tr>
		<td>${temp.firstName}</td>
		<td>${temp.lastName}</td>
		<td>${temp.email}</td>
		<td><a href = "${tempLink}">Update</a>
			|<a href = "${deleteLink}" onclick = "if(!(confirm('Are you sure you want to delete this student?'))) return false">Delete</a>
		</td>
		</tr>
	</c:forEach>
	</table>
	
	
	</div>

</div>

</body>

</html>