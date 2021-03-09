<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<!--  PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> -->
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" href="/resources/demos/style.css">
		<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<title>File Send</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" >
 		<script>
			$(document).ready(function(){
			  $("#ftsEmpSel").blur(function(){
				  var selVal = document.getElementById("ftsEmpSel").value;
				  var dispName = selVal.substring(0, selVal.indexOf(" [")) + "," + selVal.substring(selVal.indexOf("] ")+1,selVal.indexOf(","));
				  document.getElementById("ftsEmpSel").value = dispName;
			  });
			});
			$( function() {
			    var ftsOfficers =${ftsOfficers}; 
			    document.getElementById("ftsEmpSel").focus();
			    $( "#ftsEmpSel" ).autocomplete({
					source: ftsOfficers,
					select: showResult
				}) ;
			    function showResult(event, ui) { 
					var selVal = ui.item.label;
			        var empNo = selVal.substring(selVal.indexOf(" [")+2,selVal.indexOf("] "));
			        document.getElementById("ftsEmp").value = empNo;
			        document.getElementById("ftsActivity").focus();
				} 
			});
		</script>
	</head>
	<body >
		<br><br><br><br><br>
		<table>
			<tr bgcolor = "#eeffcc">
				<th align="left"><a href="Login">Home </a></th>
				<th align="center" colspan="2"><c:out value="${sessionScope.modulename}" /></th>
				<th align="center">User: <c:out value="${sessionScope.username}" /></th>
				<th align="right"><a href="home">Logout</a></th>
			</tr>
		</table>
		<br>
		<br>
		<form name="ftsSend" id="ftsSend" method="POST" action="ftsFileSend">
		<div align="center">
			<table>
				<tr> 
					<td>File No: </td>
					<td><c:out value="${sessionScope.ftsFileNo}" /></td>
				</tr>

				<tr> 
					<td>File Name: </td>
					<td><c:out value="${sessionScope.ftsFileName}" /></td>
				</tr>

				<tr>
			 		<td>Select Send to Officer: </td>
			 		<td><input type="text" id="ftsEmpSel" placeholder="Type Officer Name or Designation or Department to Select from List"></td> 
			 	</tr>
				<tr> 
					<td>File Activity: </td>
					<td>
						<select id="ftsActivity" name="ftsActivity">
			        		<c:forEach items="${ftsActivityList}" var="ftsActivityList">
 			   	         		<option value="${fn:substringBefore(ftsActivityList,'-')}"> ${fn:substringAfter(ftsActivityList,'-')} </option>
			            	</c:forEach>
			            </select>
					</td>
				</tr>
				
				<tr>
					<td colspan="2" align="center">
						<input type="submit" name="Submit" value="Submit">
  					</td>
			  	</tr>
			</table>
		</div>
		<input type="hidden" id="ftsEmp" name="ftsEmp">
		<br><br><br><br><br>
		</form>
	</body>
</html>