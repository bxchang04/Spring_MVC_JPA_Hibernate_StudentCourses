<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Update Student</title>
<style>
    .error {
	color: red;
    }
</style>
</head>
<body>
    <h1>Update Course Form</h1>
    <%@ include file="Navigation.html" %>
    <form:form action="${pageContext.request.contextPath }/courses/updateCourse" 
	method="post" modelAttribute="course">
	<fieldset>
	    <legend>Update Student</legend>
	    <form:input type="hidden" path="courseId" />
	    <div>
		<label for="code">Course Code</label>
		<div>
		    <form:input path="code" />
		    <p><form:errors path="code" class="error" /></p>
		</div>
	    </div>
	    <div>
		<label for="name">Course Name</label>
		<div>
		    <form:input path="name" />
		    <p><form:errors path="name" class="error" /></p>
		</div>
	    </div>
	    <div>
		<label for="maxSize">Max Size</label>
		<div>
		    <form:input path="maxSize" />
		    <p><form:errors path="maxSize" class="error" /></p>
		</div>
	    </div>
	    <input type="submit" value="Update Course">
	</fieldset>
    </form:form>
    <p style="color: red;">${errorMessage}</p>
    <a href="${pageContext.request.contextPath}/courses/allCourses">Cancel</a>
</body>
</html>