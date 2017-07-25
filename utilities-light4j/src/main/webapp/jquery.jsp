<!DOCTYPE html>
<html>
<head>
	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
</head>

<body>

   	<script>
	    $( document ).ready(function() {
	    	  
	    	function validateEmail($email) {
	    		var full = $email.value;
	    		var trimmed = $.trim(full);
	    		$email.value = trimmed;
	    		
	    		var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	    	    if( !regex.test( $email.value ) ) {
    		      	return false;
    		  	} else {
    		  		return true;
    		  	}
    		}

	        
	    	$( "#email" ).blur(function() {
	 	    	if(validateEmail(this)){
	    			$( "#acceptTCs").removeAttr("disabled");
	    		}
	    		else{
	    			$( "#acceptTCs").attr('disabled','disabled');
	    		}	    		
	    	});
	        
		    $('#get-data').click(function(){ 
		    	$.ajax({ 
		             type: "GET",
		             dataType: "json",
		             url: "http://two-methods-prints-headers.herokuapp.com/gsma/terms-conditions",
		             success: function(data){        		                 
 		            	 var s = data.detailedTermsAndConditionsText;
 		            	 console.log(s); 		                 
 		            	 $('#authorisedContent').html(s);		                 
		             }
		         }); 
		    });		    
	        
		    $('#acceptTCs').click(function(){ 
		    	var myObj = {};
		    	myObj["accepted"] = true;
		    	myObj["acceptedUserEmail"] = $("#email").val();
		    	myObj["detailedTermsAndConditionsText"] = "";
		    	
		    	var json = JSON.stringify(myObj);
		    	//alert(json);
		    	console.log("sending json:"+json); 	
		    	console.log($("#email").val()); 
		    	$.ajax({ 
		             type: "POST",
		             data: json,
		             contentType: "application/json",		             
		             url: "http://two-methods-prints-headers.herokuapp.com/accept/gsma/terms-conditions",
		             success: function(data){        		                 
 		            	 var s = data.detailedTermsAndConditionsText;
 		            	 console.log("SUCCESSS:"+s); 		                 
 		            	 $('#authorisedContent').html(s);		                 
		             }
		         }); 
		    });		    
	    });	 
	    

	    
   	</script>
	
	
	
	<label for="email">Email</label><input type="text" name="email" id="email" size="20"  /><br/><br/>
	<!-- <button id="get-data">Get Data</button><br/> -->
	<div id="tandctext">These are detailed Terms and Conditions curated by GSMA which need to be accepted to proceed</div><br/><br/>
	<button id="acceptTCs" disabled="disabled">Accept Terms And Conditions</button><br/><br/>
	<button id="getAuthContent" disabled="disabled">Get Authorised Content</button><br/><br/>
	<div id="authorisedContent"></div><br/><br/>
	
</body>
</html>
