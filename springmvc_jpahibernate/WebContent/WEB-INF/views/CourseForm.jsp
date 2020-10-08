<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Course</title>
<style>
    .error {
	color: red;
    }
</style>
</head>
<body>
    <h1>Add Course Page</h1>
    <%@ include file="Navigation.html" %>
    <form:form action="${pageContext.request.contextPath }/courses/createCourse" 
	method="post" modelAttribute="course">
	<fieldset>
	    <legend>Add a Course</legend>
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
		<label for="maxSize">Maximum Class Size</label>
		<div>
		    <form:input path="maxSize"/>
		    <p><form:errors path="maxSize" class="error" /></p>
		</div>
	    </div>
	    <input type="submit" value="Create Course">
	</fieldset>
    </form:form>
</body>
</html>