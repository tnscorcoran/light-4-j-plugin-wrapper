package io.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.networknt.config.Config;
import com.networknt.handler.MiddlewareHandler;
import com.networknt.utility.Constants;
import com.networknt.utility.ModuleRegistry;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.*;



public class CInterceptor implements MiddlewareHandler {
    public static final String CONFIG_NAME = "audit";
    public static final String ENABLED = "enabled";
    public static final Map<String, Object> config;
    private volatile HttpHandler next;
    PluginWrapperClient PluginWrapperClient = new PluginWrapperClient();
    static {
        config = Config.getInstance().getJsonMapConfigNoCache(CONFIG_NAME);
        }
    
    public CInterceptor() {
    	System.out.println("in Service A Interceptor");

    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {

    	PluginWrapperClient.doAuthorize(exchange);
    	if(exchange.getResponseCode()==403){
    		exchange.endExchange();
    	}
    	else{
    		next.handleRequest(exchange);
    	}    	
    }

    @Override
    public HttpHandler getNext() {
        return next;
    }

    @Override
    public MiddlewareHandler setNext(final HttpHandler next) {
        Handlers.handlerNotNull(next);
        this.next = next;
        return this;
    }

    @Override
    public boolean isEnabled() {
        Object object = config.get(ENABLED);
        return object != null && (Boolean) object;
    }

    @Override
    public void register() {
        ModuleRegistry.registerModule(CInterceptor.class.getName(), config, null);
    }
}
