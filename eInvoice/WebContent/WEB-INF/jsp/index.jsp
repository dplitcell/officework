<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login</title>
		<script type="text/javascript" src="allJS.js"></script>
	</head>
	<body onload="focusIt()">
 		<br><br><br><br><br><br><br><br><br><br>
		<form name="login" id="login" method="POST" action="Login" onSubmit="return checkInput()">
			<p align="center">
			<strong>Module Selection: </strong>
    		<select id="listModule" name="listModule">
        		<c:forEach items="${listModule}" var="listModule">
         			<%--<option value="${listModule}"> ${listModule} </option>--%>
					<option value="${fn:substringBefore(listModule,'-')}"> ${fn:substringAfter(listModule,'-')} </option>
            	</c:forEach>
        	</select>
         	
			<br><br>        
			<div align="center">
				<table border="2" >
					<tr>
						<td>User Id</td>
						<td><input type="text" name="user_id" id="user_id"/></td>
					</tr>
					<tr>
						<td>Password</td>
						<td><input type="password" name="pass" id="pass"/></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input type="submit" name="Submit" value="Submit">
	  					</td>
	  				</tr>
				</table>
			</div>
		</form>
	</body>
</html>