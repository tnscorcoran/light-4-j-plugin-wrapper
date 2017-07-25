    package io.swagger.handler;

    import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.client.Client;
import com.networknt.config.Config;
import com.networknt.exception.ClientException;

import io.swagger.PluginWrapperClient;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

    public class Data10GetHandler implements HttpHandler {
    	static int errorCount;

        static String HANDLER_NUMBER = "10";
        static String CONFIG_NAME = "api_a";
        static String apibUrl = (String)Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_b_endpoint"+HANDLER_NUMBER);
        static String apicUrl = (String)Config.getInstance().getJsonMapConfig(CONFIG_NAME).get("api_c_endpoint"+HANDLER_NUMBER);

    	//PluginWrapperClient PluginWrapperClient = new PluginWrapperClient();
        public void handleRequest(HttpServerExchange exchange) throws Exception {
        	long one = System.currentTimeMillis();
        	//PluginWrapperClient.doAuthorize(exchange);
        	long two = System.currentTimeMillis();
        	long took = two-one;
        	
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Deque<String> parameter = queryParams.get("apiKey");
            List<String> list = new Vector<String>();
            final HttpGet[] requests = new HttpGet[] {
                    new HttpGet(apibUrl+"?apiKey="+parameter.getFirst()),
                    new HttpGet(apicUrl+"?apiKey="+parameter.getFirst()),
            };
            try {
                CloseableHttpAsyncClient client = Client.getInstance().getAsyncClient();
                final CountDownLatch latch = new CountDownLatch(requests.length);
                for (final HttpGet request: requests) {
                    //Client.getInstance().propagateHeaders(request, exchange);
                    client.execute(request, new FutureCallback<HttpResponse>() {
                        @Override
                        public void completed(final HttpResponse response) {
                            try {
                                List<String> apiList = (List<String>) Config.getInstance().getMapper().readValue(response.getEntity().getContent(),
                                        new TypeReference<List<String>>(){});
                                list.addAll(apiList);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            catch (Exception e) {
                            	errorCount++;
                            	if (errorCount%10==0){
                            		System.out.print("-"+errorCount);
                            	}                            	
	                        }                            
                            latch.countDown();
                        }

                        @Override
                        public void failed(final Exception ex) {
                            ex.printStackTrace();
                            latch.countDown();
                        }

                        @Override
                        public void cancelled() {
                            System.out.println("cancelled");
                            latch.countDown();
                        }
                    });
                }
                latch.await();
            } catch (ClientException e) {
                e.printStackTrace();
                throw new Exception("ClientException:", e);
            }
            // now add API A specific messages
            list.add("API A: Message 1");
            list.add("API A: Message 2");
            exchange.getResponseSender().send(Config.getInstance().getMapper().writeValueAsString(list));
        }
    }
