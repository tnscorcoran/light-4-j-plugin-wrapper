    package io.swagger.handler;

    import com.networknt.config.Config;

import io.swagger.PluginWrapperClient;
import io.undertow.server.HttpHandler;
    import io.undertow.server.HttpServerExchange;
    import io.undertow.util.HttpString;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import org.apache.commons.lang3.StringEscapeUtils;

    public class Data8GetHandler implements HttpHandler {

    	//PluginWrapperClient PluginWrapperClient = new PluginWrapperClient();
        public void handleRequest(HttpServerExchange exchange) throws Exception {
        	long one = System.currentTimeMillis();
        	//PluginWrapperClient.doAuthorize(exchange);
        	long two = System.currentTimeMillis();
        	long took = two-one;
        	       	
            List<String> messages = new ArrayList<String>();
            messages.add("API D: "+this.getClass().getName()+"Message 1");
            messages.add("API D: "+this.getClass().getName()+"Message 2");
            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(messages));
            exchange.setPersistent(false);
        }
    }