<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!-- <!DOCTYPE html SYSTEM "about:legacy-compat"> -->
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>
			${fn:substringAfter(heading,'#')}
		</title>
		<script>
			function onLoadFn() {
				//alert("Page is loaded");
				var catValue = "${sessionScope.sr.getcatType()}";
				var areaValue = catValue + ':' + "${sessionScope.sr.getareaCode()}";
				var subAreaValue = areaValue + '#' + "${sessionScope.sr.getsubArea()}";
				populateCat(catValue, areaValue, subAreaValue);
				var HeadVal = "${heading}";
				var oprnType = HeadVal.substring(0, 1);
				//alert("Page is loaded with oprnType: " + oprnType);
				if (oprnType=="C"){
					//alert("Page is loaded with oprnType: " + oprnType);
					document.getElementById("contNGS").disabled = false;
					document.getElementById("contMob").disabled = false;
					document.getElementById("contEmail").disabled = false;
					document.getElementById("smsLocation").disabled = false;
					document.getElementById("smsCat").disabled = false;
					document.getElementById("smsArea").disabled = false;
					document.getElementById("smsSubArea").disabled = false;
					document.getElementById("smsStatus").disabled = true;
					document.getElementById("contNGS").focus();
				}
				if (oprnType=="U"){
					//alert("Page is loaded with oprnType: " + oprnType);
					document.getElementById("contNGS").disabled = true;
					document.getElementById("contMob").disabled = true;
					document.getElementById("contEmail").disabled = true;
					document.getElementById("smsLocation").disabled = true;
					document.getElementById("smsCat").disabled = true;
					document.getElementById("smsArea").disabled = true;
					document.getElementById("smsSubArea").disabled = true;
					document.getElementById("smsStatus").disabled = false;
					document.getElementById("srDesc").focus();
				}
			}
			
			function checkEmail() {
				var regex=/^[A-Za-z]+[A-Za-z0-9._]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,3}$/;
				var emailID = document.getElementById("contEmail").value.trim();
				if(!regex.test(emailID) && (emailID !== ""))
				{
					alert('Please enter valid Email ID or BLANK');
					document.getElementById("contEmail").focus();
                    document.getElementById("contEmail").value = " ";
                    return false;
				}
				return true;
			}
			
			function checkMob() {
				//alert('checking===Please enter valid Mobile No or 0 for BLANK');
				var regex=/^[789]+[0-9]{9}$/;
				var MobNo = document.getElementById("contMob").value.trim();
				if(!(regex.test(MobNo) && (MobNo.length == 10)) && (MobNo !== "0"))
				{
					alert('Please enter valid Mobile No or 0 for BLANK');
					document.getElementById("contMob").focus();
                    document.getElementById("contMob").value = "0";
                    return false;
				}
				return true;
			}

			function validate() {
				var HeadVal = "${heading}";
				var oprnType = HeadVal.substring(0, 1);
				if (oprnType=="C"){
					var validVal = document.getElementById("smsLocation").value.trim();
					if (validVal == ""){
						alert("Enter the Location of Service Requirement");
						document.getElementById("smsLocation").focus();
						return false;
					}
					//var validVal = document.getElementById("contMob").value.trim();
					//if (validVal == "0" || validVal.length != 10){
						//alert("Enter a valid Mobile No of the Contact Person");
						//document.getElementById("contMob").focus();
						//return false;
					//}
					
					checkMob();
					
					checkEmail();
					
					var validVal = document.getElementById("srDesc").value.trim();
					if (validVal == ""){
						alert("Enter the Service Requirement Details");
						document.getElementById("srDesc").focus();
						return false;
					}
					document.getElementById("smsStatus").disabled = false;
					var validVal = document.getElementById("smsStatus").value.trim();
					if (validVal == ""){
						alert("Enter the Service Requirement Status");
						document.getElementById("smsStatus").focus();
						return false;
					}
					
				}
				if (oprnType=="U"){
					document.getElementById("smsCat").disabled = false;
					document.getElementById("smsArea").disabled = false;
					document.getElementById("smsSubArea").disabled = false;
					var validVal = document.getElementById("srDesc").value.trim();
					if (validVal == ""){
						alert("Enter the Service Status Update Details");
						document.getElementById("srDesc").focus();
						return false;
					}
				}
			}
			function populateCat(catValue, areaValue, subAreaValue) {
				// Delete all values
				var smsCatObj = document.getElementById("smsCat");
				var iLoop;
				var iTotalVal = smsCatObj.length;
				for (iLoop = 0; iLoop < iTotalVal; iLoop++) { 
					smsCatObj.remove(0);
				}
				// Add all values
				<c:forEach items="${CatList}" var="cat">
					var option = document.createElement("option");
					option.value = "${fn:substringBefore(cat,':')}";
					option.text = "${fn:substringAfter(cat,':')}";
					smsCatObj.add(option);
				</c:forEach>
				if (catValue !== undefined){
					smsCatObj.value = catValue;
				}
				populateArea(areaValue, subAreaValue);
				return true;
			}
			function populateArea(areaValue, subAreaValue){
				// Delete all values
				var smsCat = document.getElementById("smsCat").value;
				var smsAreaObj = document.getElementById("smsArea");
				var iLoop;
				var iTotalVal = smsAreaObj.length;
				for (iLoop = 0; iLoop < iTotalVal; iLoop++) { 
					smsAreaObj.remove(0);
				}
				//Add all values
				<c:forEach items="${AreaList}" var="area">
					var smsCatVal = "${fn:substringBefore(area,':')}";
					if (smsCatVal == smsCat){
						var option = document.createElement("option");
						option.value = "${fn:substringBefore(area,'#')}";
						option.text = "${fn:substringAfter(area,'#')}";
						smsAreaObj.add(option);
					}
				</c:forEach>
				if (areaValue !== undefined){
					smsAreaObj.value = areaValue;
				}
				populateSubArea(subAreaValue);
				return true;
			}
			
			function populateSubArea(subAreaValue) {
				// Delete all values
				var smsArea = document.getElementById("smsArea").value;
				var smsSubObj = document.getElementById("smsSubArea");
				var iLoop;
				var iTotalVal = smsSubObj.length;
				for (iLoop = 0; iLoop < iTotalVal; iLoop++) { 
					smsSubObj.remove(0);
				}
				//Add all values
				<c:forEach items="${SubAreaList}" var="sub">
					var smsAreaVal = "${fn:substringBefore(sub,'#')}";
					if (smsAreaVal == smsArea){
						var option = document.createElement("option");
						option.value = "${fn:substringBefore(sub,'$')}";
						option.text = "${fn:substringAfter(sub,'$')}";
						smsSubObj.add(option);
					}
				</c:forEach>
				if (subAreaValue !== undefined){
					smsSubObj.value = subAreaValue;
				}
				return true;
			}
			function checkContNGS(inputTag) {
				var empNo = "0"+inputTag.value.trim();
				var xhttp = new XMLHttpRequest();
				//xhttp.open("GET", "empInfo?empNo="+empNo, false);
				xhttp.open("POST", "showSR?empNo="+empNo, false);
				xhttp.send();
				var retVal =  xhttp.responseText;
				if (retVal == " @ # $ ^ : ") {
					//alert ("Null Employee No: not allowed");
					document.getElementById("contName").value = " ";
					document.getElementById("contDesig").value = " ";
					document.getElementById("groupDesc").value = " ";
					document.getElementById("subGroupDesc").value = " ";
					document.getElementById("contMob").value = " ";
					document.getElementById("contEmail").value = " ";
					document.getElementById("smsLocation").value = " ";
					inputTag.focus();
				} else {
					var selVal = retVal.substring(0, retVal.indexOf("@"));
					document.getElementById("contName").value = selVal;
					selVal = retVal.substring(retVal.indexOf("@")+1, retVal.indexOf("#"));
					document.getElementById("contDesig").value = selVal;
					selVal = retVal.substring(retVal.indexOf("#")+1, retVal.indexOf("$"));
					document.getElementById("groupDesc").value = selVal;
					selVal = retVal.substring(retVal.indexOf("$")+1, retVal.indexOf("^"));
					document.getElementById("subGroupDesc").value = selVal;
					selVal = retVal.substring(retVal.indexOf("^")+1, retVal.indexOf(":"));
					document.getElementById("contMob").value = selVal;
					selVal = retVal.substring(retVal.indexOf(":")+1);
					document.getElementById("contEmail").value = selVal;
					document.getElementById("smsLocation").value = " ";
						//document.getElementById("smsLocation").focus();
				}
			}
		</script>
		<style> 
			input[type=submit] {
				width: 20%;
  				font-size: 20px;
  				background-color: white ;
  				border-color: gray;
  				border: 1px solid black;
			}
			input[type=text] {
				width: 100%;
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
			th {
			  	border: 1px solid black;
			  	padding: 10px;
			}

			td {
			  	border: 1px solid black;
			  	width: 25%;
			  	padding: 10px;
			}
/* 			body { 
 				background-image: url('/WEB-INF/image/dpl.jpg');
 				 "/smsApp/WebContent/WEB-INF/image/dpl.jpg" 
 				background-repeat: no-repeat; 
 				background-position: center; 
 				background-size: cover;
 				height: 100%;
 			}*/
		</style>

	</head>
	<body onload="onLoadFn()">
		<br><br>
			<table>
<!-- 		<table border="1" style="{ border-collapse: separate; border-spacing: 5px; width: 100% }"> -->
<!-- 		<table border="1" cellpadding="5" style="width:100%" border-collapse: separate; border-spacing: 5px;> -->
<!-- 		table { border-collapse: separate; border-spacing: 5px; width: 100% } -->
			<tr bgcolor = "#eeffcc">
				<th align="left"><a href="Login">Home </a></th>
				<th align="center" colspan="2"><c:out value="${sessionScope.modulename}" /></th>
<%-- 				<th align="center"><c:out value="${sessionScope.modulename}" /></th> --%>
				<th align="center">User: <c:out value="${sessionScope.username}" /></th>
				<th align="right"><a href="home">Logout</a></th>
			</tr>
		</table>
		<br>
		<br>
		<table border="1" style="width:100%">
			<tr>
				<td align="center"><c:out value="${fn:substringAfter(heading,'#')}" /></td>
			</tr>
		</table>
		<form name="smsCreateForm" id="smsCreateForm" method="POST" action="smsCreate" onSubmit="return validate()">
		<br>

		<br>
			<div align="center">
				<table>
					<tr>
						<td>Employee No: </td>
				  		<td><input type="text" name="contNGS" id="contNGS" value= "${sessionScope.sr.getcontNgs()}" onblur="checkContNGS(this)"></td>
				  		<td>Name: </td>
			      		<td><input type="text" name="contName" id="contName" value= "${sessionScope.sr.getcontPers()}" disabled></td>
					</tr>
					<tr>
				  		<td>Designation: </td>
				  		<td><input type="text" name="contDesig" id="contDesig" value= "${sessionScope.sr.getcontDesig()}" disabled></td>
				  		<td>Unit: </td>
				  		<td><input type="text" name="groupDesc" id="groupDesc" value= "${sessionScope.sr.getgroupDesc()}" disabled></td>
					</tr>
					<tr>
				  		<td>Head: </td>
				  		<td><input type="text" name="subGroupDesc" id="subGroupDesc" value= "${sessionScope.sr.getsubGroupDesc()}" disabled></td>
			      		<td>Location: </td>
				  		<td><input type="text" name="smsLocation" id="smsLocation" value= "${sessionScope.sr.getlocation()}" ></td>
					</tr>
					<tr>
				  		<td>Mobile No: </td>
				  		<td><input type="text" name="contMob" id="contMob" value= "${sessionScope.sr.getcontMob()}" onblur="checkMob()" ></td>
			      		<td>Email ID: </td>
				  		<td><input type="text" name="contEmail" id="contEmail" value= "${sessionScope.sr.getcontEmail()}" onblur="checkEmail()"></td>
					</tr>
					<tr>
				  		<td>Service Category: </td>
				  		<td>
				    		<select id="smsCat" name="smsCat" onchange = "populateArea()">
					  			<option>Service Category</option>
				   			</select>
				  		</td>
			      		<td>
							<select id="smsArea" name="smsArea" onchange = "populateSubArea()">
					  			<option>Service Area</option>
			 				</select>
				  		</td>
				  		<td>
							<select id="smsSubArea" name="smsSubArea">
					  			<option>Service Sub Area</option>
			 				</select>
				  		</td>
					</tr>
					<tr>
				  		<td>Request Details: </td>
				  		<td><input type="text" name="srDesc" id="srDesc"></td>
			      		<td>Action: </td>
				  		<td>
				  			<select id="smsStatus" name="smsStatus">
								<c:forEach items="${statusList}" var="status">
  			   	    	     		<option value=${fn:substringBefore(status,':')}> ${fn:substringAfter(status,':')} </option> 
				            	</c:forEach>
			 				</select>
				  		</td>
					</tr>
					<tr>
						<td colspan="4" align="center">
							<input type="submit" name="Submit" value="Submit">
	  					</td>
	  				</tr>
			  </table>
			</div>
		</form>
	</body>
</html>