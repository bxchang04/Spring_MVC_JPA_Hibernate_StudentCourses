<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Course Enrollment</title>
</head>
<body>
    <h1>Course Enrollment</h1>
    <%@ include file="Navigation.html" %>
    <form action="${pageContext.request.contextPath}/courses/enrollInCourse" method="post">
	<div>
	    <label>Select a Student</label>
	    <select name="studentId">
		<c:forEach items="${allStudents}" var="student">
		    <option value="${student.studentId}">${student.name}</option>
		</c:forEach>
	    </select>
	</div>
	<div>
	    <label>Select a Course</label>
	    <select name="courseId">
		<c:forEach items="${allCourses}" var="course">
		    <option value="${course.courseId}">${course.name}</option>
		</c:forEach>
	    </select>
	</div>
	<input type="submit" value="Enroll" />
    </form>
    <c:forEach items="${enrollmentViolations}" var="error">
	<h2 style="color: red;">${error}</h2>
    </c:forEach>
    <h2>${successEnrollmentMessage}</h2>
</body>
</html>