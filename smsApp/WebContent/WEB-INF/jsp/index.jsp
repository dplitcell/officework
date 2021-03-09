<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login</title>
		<script type="text/javascript">
			function validate() 
			{
				var user_id = login.user_id.value;
				var pass = login.pass.value;
				if (user_id==null || user_id.trim()=="")
				{ 
	    			alert('Please enter user id');
					login.user_id.focus();
					return false; // cancel submission
				}
				else
				{
					if (pass==null || pass.trim()=="")
					{ 
						alert('Please enter password');
						login.pass.focus();
						return false; // cancel submission
					}
					else
					{
						return true; 
					}
				}
			}
			function focusOnMyInputBox()
			{
				document.getElementById("user_id").focus();
			}
		</script>
	</head>
	<body onload="focusOnMyInputBox()">
 		<br><br><br><br><br><br><br><br><br><br>
		<form name="login" id="login" method="POST" action="Login" onSubmit="return validate()">
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