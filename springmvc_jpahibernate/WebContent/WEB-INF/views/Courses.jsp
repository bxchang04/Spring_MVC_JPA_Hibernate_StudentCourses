<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Course List</title>
</head>
<body>
    <h1>Course List</h1>
    <%@ include file="Navigation.html" %>
    <a href="${pageContext.request.contextPath}/courses/courseForm">Add Course</a>
    <p style="color: red;">${errorMessage}</p>
    <table>
	<tr>
	    <th>Course ID</th>
	    <th>Course Code</th>
	    <th>Course Name</th>
	    <th>Maximum Class Size</th>
	</tr>
	<c:forEach items = "${allCourses}" var = "course">
	    <tr>
		<td>${course.courseId}</td>
		<td><a href="${pageContext.request.contextPath }/courses/courseById
		    /${course.courseId}">${course.code}</a></td>
		<td>${course.name}</td>
		<td>${course.maxSize}</td>
		<td>
		    <a href="${pageContext.request.contextPath }/courses
			/updateCourseForm/${course.courseId}">Edit</a>
		    <a href="${pageContext.request.contextPath }/courses
			/deleteCourse/${course.courseId}">Delete</a>
		</td>
	    </tr>
	</c:forEach>
    </table>
</body>
</html>