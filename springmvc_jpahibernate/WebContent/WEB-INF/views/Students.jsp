<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Student List</title>
</head>
<body>
    <h1>Student List</h1>
    <a href="${pageContext.request.contextPath}/studentForm">Add Student</a>
    <p style="color: red;">${errorMessage}</p>
    <table>
	<tr>
	    <th>Student ID</th>
	    <th>Name</th>
	    <th>Email</th>
	</tr>
	<c:forEach items = "${allStudents}" var = "student">
	    <tr>
		<td>${student.studentId}</td>
		<td><a href="${pageContext.request.contextPath }/studentById/${student.studentId}">
                    ${student.name}</a></td>
		<td>${student.email}</td>
		<td>
		    <a href="${pageContext.request.contextPath }/updateStudentForm
                        /${student.studentId}">Edit</a>
		    <a href="${pageContext.request.contextPath }/deleteStudent
                        /${student.studentId}">Delete</a>
		</td>
	    </tr>
	</c:forEach>
    </table>
</body>
</html>