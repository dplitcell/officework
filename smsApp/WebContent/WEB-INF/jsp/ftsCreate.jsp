<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" href="/resources/demos/style.css">
		<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<title>Create File</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" >
		<script type="text/javascript">
			function focusOnMyInputBox() {
			    document.getElementById("ftsFileCat").focus();
			}
			function checkFileName() {
				var sFileName = document.getElementById("ftsFileName").value.trim();
				if (sFileName == null || sFileName == "") {
					alert ("Enter a valid File Name");
					document.getElementById("ftsFileName").focus();
				}
			}
			$(document).ready(function(){
			  $("#ftsEmpSel").blur(function(){
				  var selVal = document.getElementById("ftsEmpSel").value;
				  var dispName = selVal.substring(0, selVal.indexOf(","));
				  document.getElementById("ftsEmpSel").value = dispName;
			  });
			});
			$( function() {
			    var ftsOfficers =${ftsOfficers}; 
			    $( "#ftsEmpSel" ).autocomplete({
					source: ftsOfficers,
					select: showResult
				}) ;
			    function showResult(event, ui) { 
					var selVal = ui.item.label;
			        var empNo = selVal.substring(selVal.indexOf(" [")+2,selVal.indexOf("] "));
			        document.getElementById("ftsEmp").value = empNo;
			        document.getElementById("Submit").focus();
				} 
			});
		</script>
 	</head>
	<body onload="focusOnMyInputBox()">
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
	<form name="ftsCreateFile" id="ftsCreateFile" method="POST" action="ftsCreate" onSubmit="return validate()">

		<div align="center">
			<table>
				<tr>
			 		<td>File Type Selection: </td>
			 		<td>
			 			<select id="ftsFileCat" name="ftsFileCat">
			            	<option value="G">General</option>
			            	<option value="C">Confidential</option>
			        	</select>
			 		</td>
			 	</tr>
			
				<tr> 
					<td>File Description: </td>
					<td><input type="text" name="ftsFileName" id="ftsFileName" size = "100" onblur="checkFileName()"></td>
				</tr>

				<tr> 
					<td>File No (Deptartmental): </td>
					<td><input type="text" name="ftsFileNo" id="ftsFileNo" size = "100"></td>
				</tr>
				
				<tr> 
					<td>File Function: </td>
					<td>
						<select id="ftsFunction" name="ftsFunction">
			        		<c:forEach items="${ftsFunctionList}" var="ftsFunction">
 			   	         		<option value="${fn:substring(ftsFunction,1,4)}"> ${fn:substringAfter(ftsFunction,'-')} </option>
<%--								<option value="${ftsFunction}"> ${ftsFunction}</option>--%>
			            	</c:forEach>
			            </select>
					</td>
				</tr>
				
				<tr>
			 		<td>Select file initiator Officer: </td>
			 		<td><input type="text" id="ftsEmpSel" placeholder="Type Officer Name or Designation or Department to Select from List"></td> 
			 	</tr>
				
				<tr>
					<td colspan="2" align="center">
						<input type="submit" name="Submit" value="Submit" id="Submit">
  					</td>
			  	</tr>
			</table>
			<br><br><br><br><br>
			<input type="hidden" id="ftsEmp" name="ftsEmp">
		</div>
	</form>
	</body>
</html>