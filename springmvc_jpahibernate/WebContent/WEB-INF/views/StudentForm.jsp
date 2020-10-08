<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Student</title>
<style>
    .error {
	color: red;
    }
</style>
</head>
<body>
     <h1>Add Student Page</h1>
     <form:form action="${pageContext.request.contextPath }/createStudent" 
        method="post" modelAttribute="student">
	<fieldset>
	    <legend>Add a Student</legend>
	    <div>
		<label for="name">Name</label>
	        <div>
	            <form:input path="name" />
	            <p><form:errors path="name" class="error"/></p>
		</div>
	    </div>

	    <div>
		<label for="email">Email</label>
	        <div>
		    <form:input path="email" />
		    <p><form:errors path="email" class="error" /></p>
	        </div>
	    </div>
	    <input type="submit" value="Create Student">
	</fieldset>
    </form:form>
    <p style="color: red;">${errorMessage}</p>
    <a href="${pageContext.request.contextPath}/allStudents">Cancel</a>
</body>
</html>