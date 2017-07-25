<!DOCTYPE html>
<html>
<head>
	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
</head>

<body>

   	<script>
	    $( document ).ready(function() {
	    	  
	    	function validateEmail($email) {
	    		
	    		//console.log( "validateEmail called for <"+$email.value +">"); 
	    		var full = $email.value;
	    		var trimmed = $.trim(full);
	    		$email.value = trimmed;
	    		//console.log( "validateEmail called for <"+$email.value +">"); 
	    		
	    		var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	    	    if( !regex.test( $email.value ) ) {
    		      	//console.log("Invalid email:"+$email.value);
    		    	return false;
    		  	} else {
    		  		//console.log("Valid email:"+$email.value)
    		  		return true;
    		  	}
    		}

	        
	    	$( "#email" ).blur(function() {
	 	    		if(validateEmail(this)){
	    			$( "#acceptTCs").removeAttr("disabled");
	    		}
	    		else{
	    			
	    			$( "#acceptTCs").attr('disabled','disabled');
	    			alert();
	    		}	    		
	    	});
	        
		    $('#get-data').click(function(){ 
		    	//alert('#get-data'); 
		    	$.ajax({ 
		             type: "GET",
		             dataType: "json",
		             url: "http://two-methods-prints-headers.herokuapp.com/gsma/terms-conditions",
		             //url: "http://localhost:8080/tcrest06TwoMethodsPrintsHeaders/gsma/terms-conditions",
		             //url: "/tcrest06TwoMethodsPrintsHeaders/gsma/terms-conditions",
		             success: function(data){        
		                 
 		            	 var s = data.detailedTermsAndConditionsText;
 		            	 //alert('s is\n'+data.detailedTermsAndConditionsText);
 		            	 console.log(s); 		                 
 		            	 $('#authorisedContent').html(s);
		                 
		             }
		         }); 
		    });
		    
	    });	 
	    

	    
   	</script>
	
	
	
	<label for="email">Email</label><input type="text" name="email" id="email" size="20" value=""/><br/><br/>
	<!-- <button id="get-data">Get Data</button><br/> -->
	<div id="tandctext">These are detailed Terms and Conditions curated by GSMA which need to be accepted to proceed</div><br/><br/>
	<button id="acceptTCs" disabled="disabled">Accept Terms And Conditions</button><br/><br/>
	<button id="getAuthContent" disabled="disabled">Get Authorised Content</button><br/><br/>
	<div id="authorisedContent"></div><br/><br/>
	
</body>
</html>
