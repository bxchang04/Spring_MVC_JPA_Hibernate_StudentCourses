<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Course Page</title>
</head>
<body>
    <h1>Course Page</h1>
    <%@ include file="Navigation.html" %>
    <h2>Course Code: ${course.code}<span style="color:red;">${errorMessage}</span></h2>
    <c:if test="${course.code != null}">
	<h2>Course Name: ${course.name}</h2>
	<h2>Max Class Size: ${course.maxSize}</h2>
    </c:if>
    <a href="${pageContext.request.contextPath }/courses/updateCourseForm
	/${course.courseId}">Edit Course</a>
    <h2>Students Registered:</h2>
    <table>
	<tr>
	    <th>Student Name</th>
	</tr>
	<c:forEach items="${course.roster}" var="student">
	    <tr>
		<td>${student.name}</td>
		<td>
		    <a href="${pageContext.request.contextPath}/courses
		    /removeStudent?courseId=${course.courseId}&studentId=
		    ${student.studentId}">Remove</a>
		</td>
	    </tr>
	</c:forEach>
    </table>
</body>
</html>