    package io.swagger.handler;

    import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Client;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;

import io.swagger.PluginWrapperClient;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

    public class Data22GetHandler implements HttpHandler {

        static String HANDLER_NUMBER = "22";
        static String CONFIG_NAME = "api_b";
        static String apidUrl = (String)Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_d_endpoint"+HANDLER_NUMBER);

    	//PluginWrapperClient PluginWrapperClient = new PluginWrapperClient();
        public void handleRequest(HttpServerExchange exchange) throws Exception {
        	long one = System.currentTimeMillis();
        	//PluginWrapperClient.doAuthorize(exchange);
        	long two = System.currentTimeMillis();
        	long took = two-one;
        	
            List<String> list = new ArrayList<String>();
            try {
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Deque<String> parameter = queryParams.get("apiKey");

                CloseableHttpClient client = Client.getInstance().getSyncClient();
                HttpGet httpGet = new HttpGet(apidUrl+"?apiKey="+parameter.getFirst());
                CloseableHttpResponse response = client.execute(httpGet);
                int responseCode = response.getStatusLine().getStatusCode();
                if(responseCode != 200){
                    throw new Exception("Failed to call API D: " + responseCode);
                }
                List<String> apidList = (List<String>) Config.getInstance().getMapper().readValue(response.getEntity().getContent(),
                        new TypeReference<List<String>>(){});
                list.addAll(apidList);
            } catch (ClientException e) {
                throw new Exception("Client Exception: ", e);
            } catch (IOException e) {
                throw new Exception("IOException:", e);
            }
            // now add API B specific messages
            list.add("API B: Message 1");
            list.add("API B: Message 2");
            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
        }
    }
    
