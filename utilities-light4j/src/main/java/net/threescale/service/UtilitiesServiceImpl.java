package net.threescale.service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.threescale.model.GenericResult;
import threescale.v3.api.HttpResponse;
import threescale.v3.api.ServerError;

@Service
/**
 * 
 * @author tomcorcoran
 *
 */
public class UtilitiesServiceImpl implements UtilitiesService{
    private Properties props = null;
    

    @Autowired
    APIAccessor apiAccessor;

    private final int API_KEY_LENGTH = 32;
    

    private String HOST;
	private Integer NUM_ACCOUNTS_TO_CREATE;

    private int numCsvLines = 10000;
    private int numApps = 10000;
    private String loadTestCsvFile = "", cacheInitializerCsvFile = "";
    

    public UtilitiesServiceImpl() throws IOException {
        InputStream propsInputStream = this.getClass().getResourceAsStream(PROPERTIES_FILE);	
        props = new Properties();
        props.load(propsInputStream);

        HOST = props.getProperty("host");
        NUM_ACCOUNTS_TO_CREATE = new Integer(props.getProperty("number-accounts-to-create"));
        loadTestCsvFile = props.getProperty("load-test-csv-file");
        cacheInitializerCsvFile = props.getProperty("cache-initializer-csv-file");
        
        
    }

    @Override
    public GenericResult createMethods() throws ServerError, IOException{
    	String serviceId = props.getProperty("service-id-a");
    	String hitsMetricId = props.getProperty("hits-metric-id-a");
    	String base = "apia_data";
		for (int i = 1; i<=25; i++){
			createMethod(serviceId, base+i, hitsMetricId);
		}
		
		serviceId = props.getProperty("service-id-b");
		hitsMetricId = props.getProperty("hits-metric-id-b");
		base = "apib_data";
		for (int i = 1; i<=25; i++){
			createMethod(serviceId, base+i, hitsMetricId);
			System.out.println("created :"+base+i);
		}
		
		serviceId = props.getProperty("service-id-c");
		hitsMetricId = props.getProperty("hits-metric-id-c");
		base = "apic_data";
		for (int i = 1; i<=25; i++){
			createMethod(serviceId, base+i, hitsMetricId);
			System.out.println("created :"+base+i);
		}
		
		serviceId = props.getProperty("service-id-d");
		hitsMetricId = props.getProperty("hits-metric-id-d");
		base = "apid_data";
		for (int i = 1; i<=25; i++){
			createMethod(serviceId, base+i, hitsMetricId);
			System.out.println("created :"+base+i);
		}
		
    	return new GenericResult("SUCCESS");
    }
    
    
	@Override
	public GenericResult buildCSVfiles() throws ServerError, IOException {
		
		List<String> apiKeys = getAPIKeyList();
		
		List<String> csvLines = getCSVLines(apiKeys);
		
		buildCacheInitializerCSV(csvLines);
		
		buildLoadTestCSV(csvLines);
		
		return new GenericResult("SUCCESS");
	}

	@Override
	public GenericResult writeMethodSystemNames() throws ServerError, IOException {
		String base = "apia_data";
		String csv = "apia_data1";
		String toWrite = "";
		for (int i = 2; i<=25; i++){
			csv+=","+base+i;
		}
		toWrite = csv;
		
		base = "apib_data";
		csv = ",apib_data1";
		for (int i = 2; i<=25; i++){
			csv+=","+base+i;
		}
		toWrite += csv;
		
		base = "apic_data";
		csv = ",apic_data1";
		for (int i = 2; i<=25; i++){
			csv+=","+base+i;
		}
		toWrite += csv;
		
		base = "apid_data";
		csv = ",apid_data1";
		for (int i = 2; i<=25; i++){
			csv+=","+base+i;
		}
		toWrite += csv;
		
		return new GenericResult(toWrite);
	}



	@Override
	public GenericResult writeAPIKeys() throws ServerError, IOException {

		
		List<String> apiKeys = getAPIKeyList();
		String commmaSeparatedList = apiKeys.get(0);
		for (int i = 1; i< apiKeys.size(); i++ ){
			commmaSeparatedList+=","+apiKeys.get(i);
		}
		
		return new GenericResult("SUCCESS", commmaSeparatedList);
	}


    private List<String> getAPIKeyList() throws ServerError, JsonParseException, JsonMappingException, IOException{
    	List<String> list = new ArrayList<String>();
    	
		String accountListUrlPath = props.getProperty("application-list-url-path");
 	    
		accountListUrlPath=accountListUrlPath.replaceAll("<token>",  props.getProperty("acccess-token"));
		accountListUrlPath=accountListUrlPath.replaceAll("<serviceid>", props.getProperty("service-id-a"));
	     
		//String body = props.getProperty("tempbody");
		String body = apiAccessor.get(HOST+accountListUrlPath).getBody();
	    while ( body.indexOf("user_key")!=-1){							
	    	int indexOfStartOfKey = body.indexOf("user_key") + 11;
	    	int indexOfEndOfKey = indexOfStartOfKey+32; 
	    	String apiKey = body.substring(indexOfStartOfKey, indexOfEndOfKey);	   
	    	list.add(apiKey);
	    	
	    	body = body.substring(indexOfEndOfKey);
	    	
	    }
    	
    	return list;
    }

    
    
    private List<String> getCSVLines(List<String> apiKeys) throws ServerError, JsonParseException, JsonMappingException, IOException{
    	List<String> list = new ArrayList<String>();
  
    	String port_url_base = props.getProperty("apia-host-port-and-url-base");
    	String[] host_port_urlbase_split = port_url_base.split("-");
    	String host = host_port_urlbase_split[0];
    	String port = host_port_urlbase_split[1];
    	String urlbase = host_port_urlbase_split[2];
    	for (int i = 1; i <= 25; i++ ){
    		String fullUrl = urlbase+i;   
    		for(String apiKey: apiKeys){
        		String csv = host + "," + port + "," + fullUrl + "," + apiKey;
        		list.add(csv);
    		}
    	}
        
    	port_url_base = props.getProperty("apib-host-port-and-url-base");
    	host_port_urlbase_split = port_url_base.split("-");
    	host = host_port_urlbase_split[0];
    	port = host_port_urlbase_split[1];
    	urlbase = host_port_urlbase_split[2];
    	for (int i = 1; i <= 25; i++ ){
    		String fullUrl = urlbase+i;   
    		for(String apiKey: apiKeys){
        		String csv = host + "," + port + "," + fullUrl + "," + apiKey;
        		list.add(csv);
    		}
    	}
        
    	port_url_base = props.getProperty("apic-host-port-and-url-base");
    	host_port_urlbase_split = port_url_base.split("-");
    	host = host_port_urlbase_split[0];
    	port = host_port_urlbase_split[1];
    	urlbase = host_port_urlbase_split[2];
    	for (int i = 1; i <= 25; i++ ){
    		String fullUrl = urlbase+i;   
    		for(String apiKey: apiKeys){
        		String csv = host + "," + port + "," + fullUrl + "," + apiKey;
        		list.add(csv);
    		}
    	}
        
    	port_url_base = props.getProperty("apid-host-port-and-url-base");
    	host_port_urlbase_split = port_url_base.split("-");
    	host = host_port_urlbase_split[0];
    	port = host_port_urlbase_split[1];
    	urlbase = host_port_urlbase_split[2];
    	for (int i = 1; i <= 25; i++ ){
    		String fullUrl = urlbase+i;   
    		for(String apiKey: apiKeys){
        		String csv = host + "," + port + "," + fullUrl + "," + apiKey;
        		list.add(csv);
    		}
    	}
    
    	return list;
    }
   
    
    public String buildCacheInitializerCSV(List<String> csvLines) throws ServerError, IOException{
    	String fileContents = "";
    	
    	for (String line: csvLines){
    		fileContents+=line + "\n";    		
    	}

     	String targetFile = cacheInitializerCsvFile;
    	writeToFile(targetFile, fileContents);
    	
    	String message = "Successful Build of Cache Initializer CSV";
    	return message;
    	
    }
    
    public String buildLoadTestCSV(List<String> csvLines) throws ServerError, IOException{
    	String fileContents = "";
    	
    	for (int i = 0; i < numCsvLines; i++){
    		double rand = Math.random();
       		int index = (int)(numApps * rand);
    		String line = csvLines.get(index);
    		fileContents+=line + "\n";    
    	}
    	
     	String targetFile = loadTestCsvFile;
    	writeToFile(targetFile, fileContents);
    	
    	String message = "Successful Build of Load Test CSV";
    	return message;
    	
    }
    
	
	public GenericResult createAccountsAndApplications() throws ServerError, IOException {

		for(int i = 1; i<=NUM_ACCOUNTS_TO_CREATE; i++){			
			
			String accountId = createAccount(i);
			
			String apiKey = RandomStringUtils.randomAlphanumeric(API_KEY_LENGTH).toUpperCase();
			createApplication(accountId, "a", i, apiKey);
			createApplication(accountId, "b", i, apiKey);
			createApplication(accountId, "c", i, apiKey);
			createApplication(accountId, "d", i, apiKey);
			
			
		}
	    return new GenericResult("SUCCESS");

	}

	//****************************************************************************************
    //
    //	private methods
    //
    //****************************************************************************************
    private void writeToFile(String targetFileFullPath, String dataToWriteToFile) throws IOException{
    	FileUtils.writeStringToFile(new File(targetFileFullPath), dataToWriteToFile);
    }


	private String createAccount(int index) throws ServerError{
		String email = props.getProperty("email-prefix") + RandomStringUtils.randomAlphanumeric(6)+props.getProperty("email-suffix");
	     	    
		// ********************** Signup Express (Account Create) **********************
	    String accountCreateURLPath = props.getProperty("account-create-url-path");
	    String accountCreateBody = props.getProperty("account-create-body");
	     
	    accountCreateBody=accountCreateBody.replaceAll("<token>",  props.getProperty("acccess-token"));
	    accountCreateBody=accountCreateBody.replaceAll("<org>",  "Org-98");
	    accountCreateBody=accountCreateBody.replaceAll("<username>",  email);
	    accountCreateBody=accountCreateBody.replaceAll("<email>",  email);
	    accountCreateBody=accountCreateBody.replaceAll("<password>", props.getProperty("default-password"));
	    
	    String fullurl = HOST+accountCreateURLPath;
	    HttpResponse resp = apiAccessor.post(fullurl, accountCreateBody);
	    
	    String body = resp.getBody();
	    
	    //TODO - Use a Jackson JSON to Object creator and get the id that way.
	    int indexOfStartOfId = body.indexOf("id\":") + 4;
	    int indexOfEndOfId = body.indexOf(",");
		
		String accountId = body.substring(indexOfStartOfId, indexOfEndOfId);
		return accountId;
		
	}
	
	private void createMethod(String serviceId, String systemAndFriendlyMethodName, String hitsMetricId) throws ServerError{

	    String methodCreateURLPath = props.getProperty("method-create-url-path");
	    methodCreateURLPath=methodCreateURLPath.replaceAll("<service-id>", serviceId);
	    methodCreateURLPath=methodCreateURLPath.replaceAll("<hits-metric-id>", hitsMetricId);
	    
	    String methodCreateBody = props.getProperty("method-create-body");	     
	    methodCreateBody=methodCreateBody.replaceAll("<token>",  props.getProperty("acccess-token"));
	    methodCreateBody=methodCreateBody.replaceAll("<friendly_name>", systemAndFriendlyMethodName);
	    methodCreateBody=methodCreateBody.replaceAll("<system_name>", systemAndFriendlyMethodName);

	    String fullurl = HOST+methodCreateURLPath;
	    apiAccessor.post(fullurl, methodCreateBody);
	    	    	
	}

	
	private void createApplication(String accountId, String servicePrefix, int index, String apiKey) throws ServerError{
		
	    String applicationCreateURLPath = props.getProperty("application-create-url-path");
	    applicationCreateURLPath=applicationCreateURLPath.replaceAll("<accountId>", accountId);
	    
	    String applicationCreateBody = props.getProperty("application-create-body");	     
	    applicationCreateBody=applicationCreateBody.replaceAll("<token>",  props.getProperty("acccess-token"));
	    applicationCreateBody=applicationCreateBody.replaceAll("<plan_id>",  props.getProperty(servicePrefix + "-app-plan-id"));
	    applicationCreateBody=applicationCreateBody.replaceAll("<appName>", "App_"+servicePrefix+"_"+index);
	    applicationCreateBody=applicationCreateBody.replaceAll("<appDescription>", "App_"+index+"_"+servicePrefix);
	    applicationCreateBody=applicationCreateBody.replaceAll("<user_key>", apiKey);

	    String fullurl = HOST+applicationCreateURLPath;
	    apiAccessor.post(fullurl, applicationCreateBody);
	    	    	
	}



	public void setApiAccessor(APIAccessor apiAccessor) {
		this.apiAccessor = apiAccessor;
	}



	public void setNumCsvLines(int numCsvLines) {
		this.numCsvLines = numCsvLines;
	}



	public void setNumApps(int numApps) {
		this.numApps = numApps;
	}



	public void setLoadTestCsvFile(String loadTestCsvFile) {
		this.loadTestCsvFile = loadTestCsvFile;
	}



	public void setCacheInitializerCsvFile(String cacheInitializerCsvFile) {
		this.cacheInitializerCsvFile = cacheInitializerCsvFile;
	}

}

