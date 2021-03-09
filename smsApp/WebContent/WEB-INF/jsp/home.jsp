<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Home</title>
		<script type="text/javascript">
			function focusOnMyInputBox()
			{
    			document.getElementById("menuSel").focus();
			}
			function validMenu() {
				var listSelMenu = "${listSelMenu}";
				listSelMenu = listSelMenu.toUpperCase();
				var menuId = menuselect.menuSel.value;
				menuId = menuId.toUpperCase();
				var isExistMenu = listSelMenu.indexOf(menuId);
				if (isExistMenu == -1) { 
					alert('Please Enter a Valid Menu ID');
					menuselect.menuSel.focus();
					return false; // cancel submission
				}
				else {
					return true;
				}
			}
		</script>
		<style>
/* 			body { */
/* 				background-image: url("dpl.jpg"); */
/* 				background-repeat: no-repeat; */
/* 				background-position: center; */
/* 				background-size: cover; */
/*  				height: 100%;  */
/* 			} */
			input[type=submit] {
				width: 10%;
  				font-size: 20px;
  				background-color: white ;
  				border-color: gray;
  				border: 1px solid black;
			}
			input[type=text] {
				width: 10%;
			  	font-size: 20px;
			  	background-color: white ;
			  	border-color: gray;
			  	border: 1px solid black;
			}
			select {
			  	width: 100%;
			  	font-size: 20px;
			  	background-color: white ;
			  	border-color: gray;
			  	border: 1px solid black;
			}
			table { 
			  	font-size: 20px;
/* 			  	width: 100%; */
			  	display: table;
			  	border-collapse: separate;
			  	border-spacing: 0px;
			  	border-color: gray;
			  	border: 1px solid black;
  			  	background-color:#F0FFFF 
			}
			th {
			  	border: 1px solid black;
			  	padding: 10px;
			}

			td {
			  	border: 1px solid black;
/* 			  	width: 25%; */
			  	padding: 10px;
			}
		</style>
		
	</head>
	<body onload="focusOnMyInputBox()">
		<br><br>
		<table border="1" cellpadding="5" style="width:100%">
 			<tr bgcolor = "#eeffcc">
				<td align="left"><a href="Login">Home </a></td>
				<td align="center"><c:out value="${sessionScope.modulename}" /></td>
				<td align="center">User: <c:out value="${sessionScope.username}" /></td>
				<td align="right"><a href="home">Logout</a></td>
			</tr>
		</table>
		<br>
		<br>
		<div align="center">
	    	<font size = "5"><u>Menu</u></font>
	    	<br>
	    	<br>
	        <table border="1" cellpadding="5">
				<tr>
	                <td>Menu ID</td>
	        		<td>Menu Name</td>
	        		<td>Menu Short</td>
	            </tr>
	            <c:forEach var="menu" items="${sessionScope.menuList}">
		            <tr>
						<td><c:out value="${menu.getMenuID()}" /></td>
	                    <td><a href="${menu.getMenuURL()}"><c:out value="${menu.getMenuDesc()}" /></a></td>
	                    <td><c:out value="${menu.getMenuShrt()}" /></td>
	                </tr>
	            </c:forEach>
			</table>
			<!-- <form name="menuselect" id="menuselect" method="POST" action="CheckMenu" onSubmit="return validMenu()"> -->
			<form name="menuselect" id="menuselect" method="POST" action="home" onSubmit="return validMenu()">
	        	<br>Enter Menu: <input type="text" name="menuSel" id="menuSel">
	        	<input type="submit" value="Submit">
	        </form>
		</div>
	</body>
</html>