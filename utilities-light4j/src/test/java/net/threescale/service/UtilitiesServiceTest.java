package net.threescale.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;


import net.threescale.model.GenericResult;
import threescale.v3.api.HttpResponse;




public class UtilitiesServiceTest {
	private APIAccessor mockApiAccessor;
    private UtilitiesServiceImpl systemUnderTest = null;
	
    private String appList = "{\"applications\":[{\"application\":{\"user_key\":\"WJUDB3RRNONV5YZTNUS2SAZXLUOFVC46\"}},     ,{\"application\":{\"user_key\":\"WJUDB3RRNONV5YZTNUS2SAZXLUOFVC46\"}}     ]}";
    private String createAppResp = "\"id\":\"mock-post-creation-id\", \"data\":\"mock-post-creation-data\", ";
	public UtilitiesServiceTest() throws IOException{
		systemUnderTest = new UtilitiesServiceImpl();
		
	}
	
	@Before 
	public void before() throws Exception {
		mockApiAccessor = Mockito.mock(APIAccessor.class);
		systemUnderTest.setApiAccessor(mockApiAccessor);
	}
	
	@Test
	public void testBuildCSVfiles() throws Exception {
    	systemUnderTest.setNumApps(2);
    	systemUnderTest.setNumCsvLines(2);
       	systemUnderTest.setCacheInitializerCsvFile("generated/cache.csv");
       	systemUnderTest.setLoadTestCsvFile("generated/load.csv");
		
		HttpResponse mockresp = new HttpResponse(200, appList);
		when(mockApiAccessor.get(anyString())).thenReturn(mockresp);

		GenericResult result = systemUnderTest.buildCSVfiles();
		assertNotNull(result);
		assertEquals(result.getSummary(), "SUCCESS");
 	}

	
	@Test
	public void testCreateAccountsAndApplications() throws Exception {
		
		HttpResponse mockresp = new HttpResponse(200, appList);
		when(mockApiAccessor.get(anyString())).thenReturn(mockresp);
		
		mockresp = new HttpResponse(200, createAppResp);
		when(mockApiAccessor.post(anyString(),anyString())).thenReturn(mockresp);
		
		GenericResult result = systemUnderTest.createAccountsAndApplications();
				assertNotNull(result);
		assertEquals(result.getSummary(), "SUCCESS");
	}

	
	@Test
	public void testWriteMethodSystemNames() throws Exception {
		
		GenericResult result = systemUnderTest.writeMethodSystemNames();
		assertNotNull(result);
		String[] arr = result.getSummary().split(",");
		
		assertEquals(arr.length, 100);
	}
	
	@Test
	public void testWriteAPIKeys() throws Exception {
    	systemUnderTest.setNumApps(2);
    	systemUnderTest.setNumCsvLines(2);

    	HttpResponse mockresp = new HttpResponse(200, appList);
		when(mockApiAccessor.get(anyString())).thenReturn(mockresp);
		
		GenericResult result = systemUnderTest.writeAPIKeys();
		assertNotNull(result);
		String[] arr = result.getDetails().split(",");
		
		assertEquals(arr.length, 2);
	}



	
}
