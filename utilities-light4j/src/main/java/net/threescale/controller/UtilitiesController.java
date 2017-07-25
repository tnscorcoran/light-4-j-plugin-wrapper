package net.threescale.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.threescale.model.GenericResult;
import net.threescale.service.UtilitiesService;
import threescale.v3.api.ServerError;

/**
 * 
 * @author tomcorcoran
 *
 * This creates 
 * 	a Java Controller with multiple (numberOfEndpoints) endpoints.
 *  a CSV file  
 */

@Controller
public class UtilitiesController {

		@Autowired
		private UtilitiesService utilitiesService;

		@RequestMapping(value="/buildCSVfiles", method=RequestMethod.GET, produces="application/json") 
	    public @ResponseBody GenericResult buildSource() throws ServerError, IOException {
	        

	    	return utilitiesService.buildCSVfiles();
	    }
		@RequestMapping(value="/writeMethodSystemNames", method=RequestMethod.GET, produces="application/json") 
	    public @ResponseBody GenericResult writeMethodSystemNames() throws ServerError, IOException {
	        

	    	return utilitiesService.writeMethodSystemNames();
	    }
		@RequestMapping(value="/writeAPIKeys", method=RequestMethod.GET, produces="application/json") 
	    public @ResponseBody GenericResult writeAPIKeys() throws ServerError, IOException {
	        

	    	return utilitiesService.writeAPIKeys();
	    }


		@RequestMapping(value="/createAccountsAndApplications", method=RequestMethod.GET, produces="application/json") 
	    public @ResponseBody GenericResult uploadData() throws ServerError, IOException {
	        

	    	return utilitiesService.createAccountsAndApplications();
	    }
		
}
