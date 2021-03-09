<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Display Service Request</title>
		<style> 
			input[type=submit] {
				width: 20%;
  				font-size: 20px;
  				background-color: white ;
  				border-color: gray;
  				border: 1px solid black;
			}
			input[type=text] {
				width: 60%;
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
			  	width: 100%;
			  	display: table;
			  	border-collapse: separate;
			  	border-spacing: 0px;
			  	border-color: gray;
			  	border: 1px solid black;
			  	background-color:#F0FFFF
			}
			th, td {
			  	border: 1px solid black;
			  	padding: 10px;
			  	/* white-space:pre; */
			  	word-wrap: normal;
			}
		</style>
		<script type="text/javascript">
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
		
			function focusOnMyInputBox(){
			    document.getElementById("menuSel").focus();
			}
		
			function smsFilterFunction() {
				// Declare variables 
				var input, filter, table, tr, td, tdtext, i;
				input = document.getElementById("smsFilter");
				filter = input.value.toUpperCase();
				table = document.getElementById("smsTable");
				tr = table.getElementsByTagName("tr");
				// Loop through all table rows, and hide those who don't match the search query
				for (i = 1; i < tr.length; i++) {
					td = tr[i].getElementsByTagName("td");
					tdtext = td[0].innerHTML.toUpperCase() + td[1].innerHTML.toUpperCase() + td[2].innerHTML.toUpperCase();
					if (td[0]) {
						if (tdtext.indexOf(filter) > -1) {
							tr[i].style.display = "";
						}
						else {
							tr[i].style.display = "none";
						}
					}
				}
			}
		</script>
	</head>
	<body onload="focusOnMyInputBox()">
		<br>
	    <table  border="1" cellpadding="5" style="width:100%">
			<tr bgcolor = "#eeffcc">
	        	<td align="left"><a href="Login">Home </a></td>
	            <td align="center"><c:out value="${sessionScope.modulename}" /></td>
	        	<td align="center">User: <c:out value="${sessionScope.username}" /></td>
	        	<td align="right"><a href="home">Logout</a></td>
	        </tr>
		</table>
		<br>
	    <div align="center">
	    	<font size= "5"><u>${sessionScope.heading}</u></font>
	    	<br>
	    	<br>
	    	Search Service Request: <input type="text" id="smsFilter" onkeyup="smsFilterFunction()">
	    	<br>
	    	<br>
			<table  id="smsTable" border="1">
	 			<c:forEach var="sr" items="${sessionScope.srList}">
	                <tr>
	                	<c:if test="${sr.getsrNoURL() == ' '}">
	                		<td>${sr.getserialNo()}</td>
	                	</c:if>
	                	<c:if test="${sr.getsrNoURL() != ' '}">
							<td><a href="${sr.getsrNoURL()}">${sr.getserialNo()}</a></td>
	                    </c:if>
						<td>${sr.getsrDesc()}</td>
						<td>${sr.getsrRem()}</td>
	                	<c:if test="${sr.getsrActionURL() == ' '}">
	                		<td>${sr.getsrAction()}</td>
	                	</c:if>
	                	<c:if test="${sr.getsrActionURL() != ' '}">
	                    	<td><a href="${sr.getsrActionURL()}">${sr.getsrAction()}</a></td>
	                    </c:if>
	                </tr>
	            </c:forEach>
	        </table>
	        <form name="menuselect" id="menuselect" method="POST" action="home" onSubmit="return validMenu()">
	        <br>
	        <br>
	        Enter Menu: <input type="text" name="menuSel" id="menuSel"> 
	        <input type="submit" value="Submit">
        </form>
		</div>
	</body>
</html>