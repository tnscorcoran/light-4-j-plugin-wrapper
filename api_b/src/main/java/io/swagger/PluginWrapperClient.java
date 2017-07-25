package io.swagger;

import java.io.IOException;
import java.util.Deque;
import java.util.Map;

import io.undertow.server.HttpServerExchange;
import net.threescale.service.PluginService;
import net.threescale.service.PluginServiceImpl;
import threescale.v3.api.AuthorizeResponse;
import threescale.v3.api.ServerError;

public class PluginWrapperClient {
	private PluginService pluginService = null;
	
	public PluginWrapperClient()
	{
		try {
			pluginService = new PluginServiceImpl();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void doAuthorize(HttpServerExchange exchange) throws  ServerError{
		Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
        Deque<String> parameter = queryParams.get("apiKey");
        String apiKey = parameter.getFirst();

        
		String path = exchange.getRequestPath();
		String threescaleMethod = getThreescaleMethodName(path);
		
		AuthorizeResponse auth = pluginService.authRep(apiKey, threescaleMethod);

		if (auth!=null && (!auth.success())){
			//TODO find non-deprecated way of doing this.
			exchange.setResponseCode(403);
    	}
	}
	
	private static String getThreescaleMethodName(String path){
		
		String method = path.substring(1).replaceAll("/", "_");
		return method;
		
	}
}
