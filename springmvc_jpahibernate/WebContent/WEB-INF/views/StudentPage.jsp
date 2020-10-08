<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Student Page</title>
</head>
<body>
	<h1>Student Page</h1>
	<%@ include file="Navigation.html" %>
	<h2>Name: ${student.name}<span style="color:red;">${errorMessage}</span></h2>
	<c:if test="${student.email != null}">
	<h2>Email: ${student.email}</h2>
	</c:if>
	<a href="${pageContext.request.contextPath }/updateStudentForm
		/${student.studentId}">Edit Student</a>
	<h2>Courses:</h2>
	<table>
	    <tr>
		<th>Course Name</th>
	    </tr>
	    <c:forEach items="${student.courses}" var="course">
		<tr>
		    <td>${course.name}</td>
		    <td>
			<a href="${pageContext.request.contextPath}/removeCourse?
			studentId=${student.studentId}&courseId=${course.courseId}">
			Remove</a>
		    </td>
		</tr>
	    </c:forEach>
	</table>
</body>
</html>