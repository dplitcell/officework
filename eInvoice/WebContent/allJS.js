function checkInput() {
	var user_id = login.user_id.value;
	var pass = login.pass.value;
	if (user_id==null || user_id.trim()=="") { 
		alert('Please enter user id');
		login.user_id.focus();
		return false; // cancel submission
	} else {
		if (pass==null || pass.trim()=="") { 
			alert('Please enter password');
			login.pass.focus();
			return false; // cancel submission
		} else 
			return true; 
	}
}
function focusIt() {
	//alert("Hi! Welcome");
	document.getElementById("user_id").focus();
}
function filterList() {
	var iLoop = this.name;
	var filter = this.value.toUpperCase();
	var table, tr, td, tdtext, i;
	table = document.getElementById("listTable");
	tr = table.getElementsByTagName("tr");
	for (i = 2; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td");
		tdtext = td[iLoop].innerHTML.toUpperCase();
		if (tdtext.indexOf(filter) > -1)
			tr[i].style.display = "";
		else 
			tr[i].style.display = "none";
	}
} 
