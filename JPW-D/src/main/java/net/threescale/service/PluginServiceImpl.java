/*
 *  The code in this repo is not supported by Red Hat/3scale. 
 *  Rather it's example code of how to use the supported 3scale 
 *  Java Plugin to achieve very low latency API Management. 
/*
/*
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package net.threescale.service;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable; 

import org.springframework.stereotype.Service;

import threescale.v3.api.AuthorizeResponse;
import threescale.v3.api.ParameterMap;
import threescale.v3.api.ServerError;
import threescale.v3.api.ServiceApi;
import threescale.v3.api.impl.ServiceApiDriver;

@Service
/**
 * 
 * @author tomcorcoran
 *
 */
public class PluginServiceImpl implements PluginService{

	private Properties props = null;
    private Map <String, Boolean> authorizations = new ConcurrentHashMap<String, Boolean>();
    private ExecutorService executor = null;
    private ServiceApi serviceApi = null;
    
    private String serviceId = null;
    private String serviceToken = null;
    volatile boolean shutdown = false;

    
    public PluginServiceImpl() throws IOException {
        InputStream propsInputStream = this.getClass().getResourceAsStream(PROPERTIES_FILE);	
        props = new Properties();
        props.load(propsInputStream);
        propsInputStream.close();
        executor = Executors.newFixedThreadPool(10);
        serviceApi = ServiceApiDriver.createApi();
        
        serviceId = props.getProperty(SERVICE_ID);
        serviceToken = props.getProperty(SERVICE_TOKEN);

        initCache(props);
        
    }



    @Override
    public AuthorizeResponse authRep(String userKey, String methodOrMetric) throws ServerError {
        
        if (serviceId == null){
            throw new ServerError(props.getProperty(NO_SERVICE_ID_PROP_WARNING)+ methodOrMetric);
        }
        else if (serviceToken == null){
            throw new ServerError(props.getProperty(NO_SERVICE_TOKEN_PROP_WARNING)+ methodOrMetric);
        }
        
        AuthorizeResponse resp = null;
        
        String key = userKey+methodOrMetric;
        Boolean auth = getFromAuthMap(key);
        if (auth!=null && (auth==true)){
            Callable<Void> asyncAuth = new ASyncAuth(userKey, methodOrMetric, serviceId, serviceToken);

            executor.submit(asyncAuth);
            
        }
        else{
            return getAuthResponse(userKey, methodOrMetric, serviceId, serviceToken, true);       	
        }
        
        return resp;
    }
	
    class ASyncAuth implements Callable<Void> {    
        String userKey, methodOrMetric, serviceId, serviceToken;
        ASyncAuth(String uKey, String methodMetric, String servId, String servToken){
            userKey = uKey;
            methodOrMetric = methodMetric;
            serviceId = servId;
            serviceToken = servToken;
        }
        public Void call(){
            getAuthResponse(userKey, methodOrMetric, serviceId, serviceToken, false);
            return null;
        }
        
    }
	
	
    /**
     * 
     * @param userKey			Key on 3scale
     * @param methodOrMetric	System name of method on 3scale
     * @param serviceId			of Service on 3scale 
     * @param serviceToken		for Service on 3scale 
     * @param writeSuccessToMap Only write success back to map if it wasn't there before - to minimise processing 
     * @return
     */
    private AuthorizeResponse getAuthResponse(String userKey, String methodOrMetric, String serviceId, String serviceToken, boolean writeSuccessToMap){
        AuthorizeResponse authorizeResponse = null;    	
        ParameterMap paramMap = buildParameterMap(serviceId, userKey, methodOrMetric, 1);
        
        try {					
            authorizeResponse = serviceApi.authrep(serviceToken, serviceId, paramMap);
        } catch (ServerError e) {
            e.printStackTrace();
        }
        
        if (writeSuccessToMap && authorizeResponse.success()){
            putToAuthMap(userKey+methodOrMetric, true);
        }
        else if (!authorizeResponse.success()){
        	authorizationFailed(userKey+methodOrMetric);
        }
         
        return authorizeResponse;
    		
    }
	
	
    private ParameterMap buildParameterMap(String serviceId, String userKey, String metric, Integer incrementBy){
        ParameterMap params = new ParameterMap();
        params.add("service_id", serviceId);
        params.add("user_key", userKey);		
        		
        ParameterMap usage = new ParameterMap();
        usage.add(metric, incrementBy.toString());
        params.add("usage", usage);
        	
        return params;
    }
    
    private Boolean getFromAuthMap(String key){
        return authorizations.get(key);
    }

    private void authorizationFailed(String key){
        authorizations.put(key, false);
    }

    private void putToAuthMap(String key, Boolean success){
        authorizations.put(key, success); 	
    }


    private void initCache(Properties props){
    	String apiKeysCSV = props.getProperty(API_KEYS);
    	String methodsCSV = props.getProperty(METHODS);
    	String[] apiKeys = apiKeysCSV.split(",");
    	String[] methods = methodsCSV.split(",");
    	
    	System.out.println("Start Now is "+new java.util.Date()+", size is " + authorizations.size());
    	for (String apiKey: apiKeys){
    		for(String method: methods){
    			putToAuthMap(apiKey+method, true);
    		}
    	}
    	System.out.println("End Now is "+new java.util.Date()+", size is " + authorizations.size());
   	
    }
    
    //******************************************************************
    //Getters and Setters
    public void setAuthorizations(Map<String, Boolean> authorizations) {
        this.authorizations = authorizations;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void setServiceApi(ServiceApi serviceApi) {
		this.serviceApi = serviceApi;
	}

    public void setProps(Properties props) {
        this.props = props;
    }

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
	}
}
