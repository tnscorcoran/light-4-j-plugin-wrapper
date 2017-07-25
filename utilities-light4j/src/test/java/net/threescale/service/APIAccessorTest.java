package net.threescale.service;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import threescale.v3.api.HttpResponse;
import threescale.v3.api.ServerError;

public class APIAccessorTest {
	private final String URL = "https://echo-api.3scale.net/";
	private APIAccessor apiAccessor;
	
	@Before 
	public void before() throws Exception {
		apiAccessor = new APIAccessorImpl();
	}
	
	@Test
	public void testGet() throws ServerError {
		HttpResponse resp = apiAccessor.get(URL);
		assertNotNull(resp);
	}

	@Test
	public void testPost() throws ServerError {
		HttpResponse resp = apiAccessor.get(URL);
		assertNotNull(resp);
	}

}
