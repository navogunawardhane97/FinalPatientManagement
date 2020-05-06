$(document).ready(function() 
{  
	if ($("#alertSuccess").text().trim() == "")  
	{   
		$("#alertSuccess").hide();  
	} 
	$("#alertError").hide(); 
}); 

//SAVE ============================================ 
$(document).on("click", "#btnSave", function(event) 
{  
	// Clear alerts---------------------  
	$("#alertSuccess").text("");  
	$("#alertSuccess").hide();  
	$("#alertError").text("");  
	$("#alertError").hide(); 

	// Form validation-------------------  
	var status = validatepatientForm();  
	if (status != true)  
	{   
		$("#alertError").text(status);   
		$("#alertError").show();   
		return;  
	} 

	// If valid------------------------  
	var t = ($("#hidPIDSave").val() == "") ? "POST" : "PUT";
	
	$.ajax(
	{
		url : "PatientAPI",
		type : t,
		data : $("#formPatient").serialize(),
		dataType : "text",
		complete : function(response, status)
		{
			onPatientSaveComplete(response.responseText, status);
		}
	});
}); 

function onPatientSaveComplete(response, status){
	if(status == "success")
	{
		var resultSet = JSON.parse(response);
			
		if(resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Successfully Saved.");
			$("#alertSuccess").show();
					
			$("#divPatientGrid").html(resultSet.data);
	
		}else if(resultSet.status.trim() == "error"){
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	}else if(status == "error"){
		$("#alertError").text("Error While Saving.");
		$("#slertError").show();
	}else{
		$("#alertError").text("Unknown Error while Saving.");
		$("#alertError").show();
	}
	$("#hidPIDSave").val("");
	$("#formPatient")[0].reset();
}

//UPDATE========================================== 
$(document).on("click", ".btnUpdate", function(event) 
		{     
			$("#hidPIDSave").val($(this).closest("tr").find('#hidPIDUpdate').val());     
			$("#nic").val($(this).closest("tr").find('td:eq(0)').text());     
			$("#name").val($(this).closest("tr").find('td:eq(1)').text());     
			$("#age").val($(this).closest("tr").find('td:eq(2)').text());     
			$("#address").val($(this).closest("tr").find('td:eq(3)').text());
			$("#phoneNo").val($(this).closest("tr").find('td:eq(4)').text());     
			     
			

});


//Remove Operation
$(document).on("click", ".btnRemove", function(event){
	$.ajax(
	{
		url : "PatientAPI",
		type : "DELETE",
		data : "patientID=" + $(this).data("pid"),
		dataType : "text",
		complete : function(response, status)
		{
			onPatientDeletedComplete(response.responseText, status);
		}
	});
});

function onPatientDeletedComplete(response, status)
{
	if(status == "success")
	{
		var resultSet = JSON.parse(response);
			
		if(resultSet.status.trim() == "success")
		{
			$("#alertSuccess").text("Successfully Deleted.");
			$("#alertSuccess").show();
					
			$("#divPatientGrid").html(resultSet.data);
	
		}else if(resultSet.status.trim() == "error"){
			$("#alertError").text(resultSet.data);
			$("#alertError").show();
		}
	}else if(status == "error"){
		$("#alertError").text("Error While Deleting.");
		$("#alertError").show();
	}else{
		$("#alertError").text("Unknown Error While Deleting.");
		$("#alertError").show();
	}
}

//CLIENTMODEL
function validatepatientForm() {  
	// First Name 
	if ($("#nic").val().trim() == "")  
	{   
		return "Insert NIC.";  
	} 

	// Last Name  
	if ($("#name").val().trim() == "")  
	{  
		return "Insert Name.";  
	}
	
	// Age  
	if ($("#age").val().trim() == "")  
	{   
		return "Insert Age.";  
	} 

	// Gender  
	if ($("#address").val().trim() == "")  
	{  
		return "Insert Address.";  
	}
	
	// Email  
	if ($("#phoneNo").val().trim() == "")  
	{   
		return "Insert Phone No.";  
	} 
	
	
	// Phone
	if ($("#phone").val().trim() == "")  
	{  
		return "Insert Phone number .";
	}
	
	//is Numerical value
	var phoneNum = $("#phone").val().trim();  
	if (!$.isNumeric(phoneNum))  {   
		return "Insert valid phone number.";  
	} 
	
	
	
	return true;
}
