<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title><c:out value="${sessionScope.heading}"/></title>
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
	 			body { 
	 				background-image: url("../image/dpl.jpg");
	 				background-repeat: no-repeat; 
	 				background-position: center; 
	 				background-size: cover;
	 				height: 100%;
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
	
				function ftsFilterFunction() {
					// Declare variables 
					var input, filter, table, tr, td, i;
					input = document.getElementById("ftsFilter");
					filter = input.value.toUpperCase();
					table = document.getElementById("ftsTable");
					tr = table.getElementsByTagName("tr");
	//alert(filter);			
					// Loop through all table rows, and hide those who don't match the search query
					for (i = 0; i < tr.length; i++) {
	 					td = tr[i].getElementsByTagName("td")[0];
						if (td) {
							if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
								tr[i].style.display = "";
							}
							else {
								tr[i].style.display = "none";
							}
						}
/*						td = tr[i].getElementsByTagName("td");
						tdtext = td[0].innerHTML.toUpperCase() + td[1].innerHTML.toUpperCase();
						if (td[0]) {
							if (tdtext.indexOf(filter) > -1) {
								tr[i].style.display = "";
							}
							else {
								tr[i].style.display = "none";
							}
						}*/
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
	    	<font size= "5"><u><c:out value="${sessionScope.heading}" /></u></font>
	    	<br>
	    	<c:if test="${sessionScope.TypeId != 'T'}">
	    	<br>
	    	Search Text: <input type="text" id="ftsFilter" onkeyup="ftsFilterFunction()" size = "30">
	    	<br>
	    	</c:if>
	    	<br>
	        <table id="ftsTable" border="1" cellpadding="5">
				<tr class="header">
	                <th>No</th>
	        		<th>Description</th>
	        		<th>Remarks</th>
	        		<th>Action</th>
	            </tr>
	 			<c:forEach var="file" items="${sessionScope.fileList}">
	                <tr>
	                	<c:if test="${sessionScope.TypeId == 'T'}">
	                		<td><c:out value="${file.getFileNo()}" /></td>
	                	</c:if>
	                	<c:if test="${sessionScope.TypeId != 'T'}">
	                    	<td><a href="ftsFileList?TypeId=T&fileNo=${file.getFileNo()}&fileName=${file.getFileDesc()}"><c:out value="${file.getFileNo()}" /></a></td>
	                    </c:if>
	                    <td><c:out value="${file.getFileDesc()}" /></td>
	                    <c:if test="${sessionScope.TypeId != 'T' && sessionScope.TypeId != 'S'}">
							<c:if test="${file.getFileCust() == 'ARCHIVE'}">
	    						<td><a href="ftsFileAction?action=${file.getFileCust()}&fileno=${file.getFileNo()}&retun=${sessionScope.TypeId}"><c:out value="${file.getFileCust()}" /></a></td>
							</c:if>
							<c:if test="${file.getFileCust() != 'ARCHIVE'}">
	    						<td><c:out value="${file.getFileCust()}" /></td>
							</c:if>
							<c:if test="${file.getFileUrl() == 'SEND'}">
	    						<td><a href="ftsFileSend?fileno=${file.getFileNo()}&retun=${sessionScope.TypeId}&fileName=${file.getFileDesc()}"><c:out value="${file.getFileUrl()}" /></a></td>
							</c:if>
							<c:if test="${file.getFileUrl() != 'SEND'}">
	    						<td><a href="ftsFileAction?action=${file.getFileUrl()}&fileno=${file.getFileNo()}&retun=${sessionScope.TypeId}"><c:out value="${file.getFileUrl()}" /></a></td>
							</c:if>
						</c:if>
						<c:if test="${sessionScope.TypeId == 'T' || sessionScope.TypeId == 'S'}">
	   						<td><c:out value="${file.getFileCust()}" /></td>
	   						<td><c:out value="${file.getFileUrl()}" /></td>
						</c:if>
	                </tr>
	            </c:forEach>
	        </table>
			<form name="menuselect" id="menuselect" method="POST" action="home" onSubmit="return validMenu()">
	        	<br>Enter Menu: <input type="text" name="menuSel" id="menuSel"> <input type="submit" value="Submit">
	        </form>
	    </div>
		<%-- <c:if test="${sessionScope.unique == null}">
		    <c:redirect url = "invalidSession.jsp"/>
		</c:if>	 --%>
	</body>
</html>