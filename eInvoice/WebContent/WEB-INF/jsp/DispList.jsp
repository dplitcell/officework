<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>${heading}</title>
		<script type="text/javascript" src="allJS.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
 		<script>
 			function chkRegEx() {
 				var fieldArray = ${fields};
 				var onBlurFn;
				var iLoop;
				var regExpPat;
 				for (iLoop = 0; iLoop < fieldArray.length; iLoop++) {
					if (fieldArray[iLoop].field_name.toLowerCase() == this.id.toLowerCase()) {
						onBlurFn = JSON.parse(fieldArray[iLoop].onblur_fn);
						break;
					}
				}
 				regExpPat = new RegExp(onBlurFn.patn);
 				if (!regExpPat.test(this.value)) {
					alert(onBlurFn.err_msg);
					this.focus();
 				}
 			}
 			
 			function popOther() {
 				var fieldArray = ${fields};
 				var onBlurFn;
				var iLoop;
				var toUpdFld;
				var curFld;
				var iLoopF;
				var optionArray;
				var filterArray;
				var filterText;
				var iTotalVal;
				var curFldObj;
				//var iLpFlt;
				for (iLoop = 0; iLoop < fieldArray.length; iLoop++) {
					if (fieldArray[iLoop].field_name.toLowerCase() == this.id.toLowerCase()) {
						onBlurFn = JSON.parse(fieldArray[iLoop].onblur_fn);
						break;
					}
				}
				toUpdFld = onBlurFn.flds;
				
				//{"type":"change","flds":[{"id":"PARENT_ID","filter":["P:module_id"]}]}
				//[{"id":"PARENT_ID","filter":["P:module_id"]}]
				//Loop for Fields to be Updated				
				for (iLoopF = 0; iLoopF < toUpdFld.length; iLoopF++) {
					curFld = toUpdFld[iLoopF].id.toLowerCase();
					filterArray = toUpdFld[iLoopF].filter;
					filterText = "";
					//Loop for create filter value
					for (iLoop = 0; iLoop < filterArray.length; iLoop++) {
						switch (filterArray[iLoop].substring(0, 1)) {
							case "P":
								filterText = filterText + "#" + document.getElementById(filterArray[iLoop].substring(2).toLowerCase()).value;
								//callUrl = callUrl + "&" + iLoop.toString() + "=" + document.getElementById(onBlurFn.input_param[iLoop].substring(2).toLowerCase()).value;
								break;
							case "V":
								filterText = filterText + "#" + filterArray[iLoop].substring(2);
								//callUrl = callUrl + "&" + iLoop.toString() + "=" + onBlurFn.input_param[iLoop].substring(2);
								break;
							case "A":
								//callUrl = callUrl + "&" + iLoop.toString() + "=" + ${onBlurFn.input_param[iLoop].substring(2)};
								break;
						}	
					}
					filterText = filterText.substring(1);
					//Loop to find current fields option array
					for (iLoop = 0; iLoop < fieldArray.length; iLoop++) {
						if (fieldArray[iLoop].field_name.toLowerCase() == curFld) {
							optionArray = fieldArray[iLoop].field_options;
							//onBlurFn = JSON.parse(fieldArray[iLoop].onblur_fn);
							break;
						}
					}	
					//change options by filtering
					//var iLoopOpt = 0;
					curFldObj = document.getElementById(curFld);
					iTotalVal = curFldObj.length;
					//First remove all options
					for (iLoop = 0; iLoop < iTotalVal; iLoop++) { 
						curFldObj.remove(0);
					}
					//Then add options by checking filters
					for (iLoop = 0; iLoop < optionArray.length-1; iLoop++) {
						if (optionArray[iLoop][1]==filterText) {
							var option = document.createElement("OPTION");
							option.value = optionArray[iLoop][0].substring(0, optionArray[iLoop][0].indexOf("###"));
							option.text = optionArray[iLoop][0].substring(optionArray[iLoop][0].indexOf("###")+3);
							curFldObj.add(option);
						}
					}
					//change end
				}
			}
 		
 			function callAjax() {
 				var fieldArray = ${fields};
 				var onBlurFn;
				var iLoop;
				for (iLoop = 0; iLoop < fieldArray.length; iLoop++) {
					if (fieldArray[iLoop].field_name.toLowerCase() == this.id.toLowerCase()) {
						onBlurFn = JSON.parse(fieldArray[iLoop].onblur_fn);
						break;
					}
				}
				var callUrl = "Login?query=" + onBlurFn.query + "&totParam=" + onBlurFn.input_param.length.toString();
				var inpVal;
 				for (iLoop = 0; iLoop < onBlurFn.input_param.length; iLoop++) {
					switch (onBlurFn.input_param[iLoop].substring(0, 1)) {
						case "P":
							inpVal = document.getElementById(onBlurFn.input_param[iLoop].substring(2).toLowerCase()).value;
							if (inpVal==null || inpVal.trim() == "")
								callUrl = callUrl + "&" + iLoop.toString() + "=0";
							else
								callUrl = callUrl + "&" + iLoop.toString() + "=0" + inpVal.trim();
							break;
						case "V":
							callUrl = callUrl + "&" + iLoop.toString() + "=0" + onBlurFn.input_param[iLoop].substring(2).trim();
							break;
						case "A":
//							callUrl = callUrl + "&" + iLoop.toString() + "=" + ${onBlurFn.input_param[iLoop].substring(2)};
							break;
					}
				}
 				//alert(callUrl);
				var xhttp = new XMLHttpRequest();
				xhttp.open("POST", callUrl, false);
				xhttp.send();
				var retJson =  JSON.parse(xhttp.responseText);
				for (fieldParam in retJson) {
					if (fieldParam.toLowerCase() != "is_error" && fieldParam.toLowerCase() != this.id.toLowerCase() && document.getElementById(fieldParam.toLowerCase())) 
						document.getElementById(fieldParam).value = retJson[fieldParam];
				}
				if (retJson.is_error == "Y") {
					alert(onBlurFn.err_msg);
					this.focus();
				}
 			}
	 		
 			function validMenu() {
	 			var menuList = "${menuList}";
	 			var menuId = this.value.toUpperCase();
	 			var isExistMenu = menuList.indexOf(menuId);
	 			if (isExistMenu == -1) { 
		 			alert("Enter a valid menu ID or Key");	 				
	 				this.focus();
	 				return false; // cancel submission
	 			} else
	 				return true;
	 		}
	 		
 			function updProp() {
				<c:if test="${fields != null}">
					var fieldArray = ${fields};
					for (var iLoop = 0; iLoop < fieldArray.length; iLoop++)
						document.getElementById(fieldArray[iLoop].field_name).disabled = false;
				</c:if>
			}
 			function onLoadFn() {
				var listCount = 0;
				var menu = ${menu};
				var menuJson = menu[0];
				var listTWidth   = "auto";
				var listTDWidths = null;
				var fieldParam  = null;
				var fieldTWidth   = "auto";
				var fieldTDWidths = null;
	
				if ("field_param" in menuJson) {
					var fieldParamJson = JSON.parse(menuJson.field_param);
					if("list_table" in fieldParamJson) {
						if ("table_width" in fieldParamJson.list_table)
							listTWidth = fieldParamJson.list_table.table_width;
						if ("td_width" in fieldParamJson.list_table)
							listTDWidths = fieldParamJson.list_table.td_width;
					}
	
					if("filter_text" in fieldParamJson) {
						fieldParam = fieldParamJson.filter_text;
					}
	
					if ("field_table" in fieldParamJson) {
						if ("table_width" in fieldParamJson.field_table)
							fieldTWidth = fieldParamJson.field_table.table_width;
						if ("td_width" in fieldParamJson.field_table)
							fieldTDWidths = fieldParamJson.field_table.td_width;
					}
				}
					<c:if test="${display == null}">
						document.getElementById("listTable").style.display = "none";
					</c:if>
				<c:if test="${display != null}">
					var loadArray = ${display};
					var table = document.getElementById("listTable");
					table.style.width = listTWidth;
					var iLoop = 0;
					var iCols = 0;
					for (iLoop = 0; iLoop < loadArray.length; iLoop++) {
						var row = table.insertRow(0);
						for (iCols = 0; iCols < loadArray[0].length; iCols++) {
							var cell = row.insertCell(iCols);
							if (listTDWidths != null)
								cell.style.width = listTDWidths[iCols];
							var str = loadArray[iLoop][iCols];
							if (str.indexOf("@LINK:")==-1)
								cell.innerHTML = str;
							else
								cell.innerHTML = str.substring(0, str.indexOf("@LINK:")).link(str.substring(str.indexOf("@LINK:")+6));
							
						}
					}
					if (fieldParam != null) {
							var row = table.insertRow(0);
						for (iLoop = 0; iLoop < loadArray[0].length; iLoop++) {
							var cell = row.insertCell(iLoop);
							var inp = document.createElement("INPUT");
							cell.appendChild(inp);
							var fieldName = iLoop.toString();
							inp.setAttribute("id", fieldName);
							inp.setAttribute("name", fieldName);
							for (paramObj in fieldParam) {
								inp.setAttribute(paramObj, fieldParam[paramObj]);	  
							}
							$(inp).on('keyup', filterList);
						}
					}
				</c:if>
					<c:if test="${fields == null}">
					document.getElementById("fieldsTable").style.display = "none";
				</c:if>
				<c:if test="${fields != null}">
					var fieldArray = ${fields};
					var initFlag = 0;
					<c:if test="${initials != null}">
						var loadArray = ${initials};
						var loadJson = loadArray[0];
						initFlag = 1;
					</c:if>
					var table = document.getElementById("fieldsTable");
					table.style.width = fieldTWidth;
					var iLoop = 0;
					for (iLoop = 0; iLoop < fieldArray.length; iLoop++) {
						var row = table.insertRow(0);
						var cell1 = row.insertCell(0);
						var cell2 = row.insertCell(1);
						if (fieldTDWidths != null) {
							cell1.style.width = fieldTDWidths[0];
							cell2.style.width = fieldTDWidths[1];
						}
						cell1.innerHTML = fieldArray[iLoop].field_desc;
						if (fieldArray[iLoop].obj_type=="T")
							var inp = document.createElement("INPUT");
						if (fieldArray[iLoop].obj_type=="S") {
							var inp = document.createElement("SELECT");
							var optionArray = fieldArray[iLoop].field_options;
							
							var iLoopOpt = 0;
 							for (iLoopOpt = 0; iLoopOpt < optionArray.length-1; iLoopOpt++) {
								var option = document.createElement("OPTION");
								option.value = optionArray[iLoopOpt][0].substring(0, optionArray[iLoopOpt][0].indexOf("###"));
								option.text = optionArray[iLoopOpt][0].substring(optionArray[iLoopOpt][0].indexOf("###")+3);
								inp.add(option);
							} 
						}
						if (fieldArray[iLoop].disabled=="0")
							inp.disabled = false;
						else
							inp.disabled = true;
						cell2.appendChild(inp);
						if (fieldArray[iLoop].focus=="1")
							inp.focus();
						var param = JSON.parse(fieldArray[iLoop].field_param);
						for (paramObj in param) {
							inp.setAttribute(paramObj, param[paramObj]);	  
						}
						var fieldName = fieldArray[iLoop].field_name.toLowerCase();
						inp.setAttribute("id", fieldName);
						inp.setAttribute("name", fieldName);
						if (initFlag == 1) {
							if (fieldName.toLowerCase() in loadJson) 
								//inp.setAttribute("value", loadJson[fieldName.toLowerCase()]);
								inp.value = loadJson[fieldName.toLowerCase()];
						}
						if ("onblur_fn" in fieldArray[iLoop]) {
							var onBlurFn = JSON.parse(fieldArray[iLoop].onblur_fn);
							switch (onBlurFn.type) {
								case "js_fn":
									$(inp).on('blur', eval(onBlurFn.fn_name));
									break;
								case "ajax":
									$(inp).on('blur', callAjax);
									break;
								case "change":
									$(inp).on('change', popOther);
									$(inp).on('blur', popOther);
									break;
								case "reg_ex":
									$(inp).on('blur', chkRegEx);
									break;
							}
						}
				 	}
				</c:if>
				if (!("submit_query" in menuJson))
					document.getElementById("submitTable").style.display = "none";
			}
		</script>
		<style> 
			input[type=submit] {
				width: 30%;
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
			  	/* font-size: 20px; */
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
		</style>
	</head>
	<body onload="onLoadFn()">
		<br><br>
		<table>
			<tr bgcolor = "#eeffcc">
				<th align="left"><a href="${home}">Home </a></th>
				<th align="center" colspan="2">${modulename}</th>
				<th align="center">User: ${username}</th>
				<th align="right"><a href="Login">Logout</a></th>
			</tr>
		</table>
		<br><br>
		<table border="1" style="width:100%">
			<tr>
				<td align="center"><c:out value="${heading}" /></td>
			</tr>
		</table>
		<br><br>
		<div align="center">
	    	<table id = "listTable">
			</table>
			<br>
			<form name="DispList" id="DispList" method="POST" action="DispList" onsubmit="updProp()">
				<table id = "fieldsTable">
				</table>
				<br>
				<table id = "submitTable">
					<tr>
						<td align="center">
							<input type="submit" name="Submit" value="Submit">
	  					</td>
	  				</tr>
	  			</table>
			</form>
 		</div>	
	</body>
</html>