# Light-4-j-plugin-wrapper

**Disclaimer**: The code in this repo is not supported by Red Hat/3scale. Rather it's example code of how you can and we did achieve very low latency API Management. We did this by applying a wrapper around the supported 3scale Java Plugin which uses caching and asynchronous calls to 3scale. More details below.

## Introduction  
This Repo contains the source code and instructions for running the solution detailed in Kavitha Srinivasan's and Tom Corcoran's recent Blog [Light-4-J - with Red Hat 3scale](http://middlewareblog.redhat.com/2017/07/25/low-latency-api-management-for-microservices-framework-light-4-j-with-red-hat-3scale/). The blog outlines an approach to achieve ultra low latency API Management provided by 3scale and implemented as a Java Plugin and Wrapper for the popular Microservices Framework https://networknt.github.io/light-rest-4j/ See the blog for high level details on the approach.

## Pre-Requisite: A 3scale API Management account - preferably [On Premises](https://support.3scale.net/guides/infrastructure/onpremises20-installation).  

## Instructions
1. Download and install the Java Plugin (3scale-api): https://github.com/3scale/3scale_ws_api_for_java
   Before running *mvn clean install*, set the DEFULAT_HOST here: /src/main/java/threescale/v3/api/ServiceApi.java to be that of you 3scale API Management back end - if you are using 3scale On Prem for this exercise (recommended).
   Run *mvn clean install*.
   Take note of the version of line 6 in pom.xml in the root directory of the Repo.  
   Clone this repo.

3. Prepare your 3scale account.
As discussed in the [Blog](http://middlewareblog.redhat.com/2017/07/25/low-latency-api-management-for-microservices-framework-light-4-j-with-red-hat-3scale/) in this example, we use 4 Microservices represented by 4 light-4-j components in this repo: swagger-light-java-a, swagger-light-java-b etc. These 4 Microservices are represented by 4 3scale Services.  
	3.1 Add 4 3scale Services. ![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/1-3scaleServices.png)  
	Your first service will already exist with the name *API*. Open it, choose Definition, Edit it and change its name to Service A. System is *api* and is not editable.
	For your remaining 3 Services, choose Create Service. Give each one a system name like *service-b* and a name like *Service B*, repeating for C and D.  
	  
	3.2 Setup & Remove your 3scale Default Application Plans. Open each Service and click on Publish Application Plans.  
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/2-Applications-PlansLink.png)  
	A will have 2, the others will have none. With Service A, there will be 2 a Basic and Unlimited. With the other 3 services, you'll need to create an Application Plan. Just enter a descriptive Name and System name (no spaces). For all 4, make sure the Default Plan is blank. This ensures when we create accounts, a default Application will not be created (we'll be doing this ourselves using the 3scale APIs).
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/2-Applications-PlansList.png)  
	3.3 Retrieve your Service Ids and Tokens. These are required to make Reporting and Authorization from Plugin to 3scale API Manager.  
	First get your 4 Service Ids. Open each Service's Overview and get it as shown. (with 3scale On Prem this is likely to be a single or double digit number). Keep note of each one and the service it relates to.
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/3-Service-Id.png)  
	Now get your Service Tokens. Go to Gear Sign -> Personal Settings -> Tokens. Copy your 4 tokens - you'll need them when configuring your Plugin Wrappers.
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/4-Service-tokens.png)  
	
4. Update your Plugin Wrappers and build. For each of JPW-A, JPW-B, JPW-C, JPW-D, open /src/main/resources.props.properties. Set your serviceid and servicetoken to the correct one for Services A, B, C and D. Now open each of these project's pom.xml. Ensure the version of the 3scale-api dependency is the same as the actual component in 1. above.  
Run *mvn clean install* on each.
	
5. Update your APIs and build. For each of api_a, api_b, api_c, api_d, add the same properties file to as you inserted to JPW-A, JPW-B, JPW-C, JPW-D respectively. (JPW-x's are picked up at build time, api_x's at runtime).  
Note - taking API code from this Repo is an interim approach to provisioning the APIs. Time permitting, we will enhance this README and Repo to reflect the recommended OAI spec driven approach on the [Light 4 J Chained Microservices Tutorial](https://networknt.github.io/light-rest-4j/tutorial/ms-chain/) 

6. Use the 3scale APIs to populate required data on 3scale. There are 2 major repetitive tasks we need to undertake (details below). For those we use the utility project included in this repo, *utilities-light4j*.  
Both are driven off a local Tomcat web app configured to run these utilities.
To start run *mvn tomcat7:run*
First *method* creation. We have 25 endpoints in each of the 4 Microservices A, B, C and D. These 100 endpoints each have a logical *method* defined on 3scale. Traffic Authorization and Reporting is done on these methods. We use a logical naming convention to build the method at runtime. e.g. a request with a path /apid/data12 is translated into a method called apid_data12. Creation and storage of these 100 method is task 1.  	
This will be rolled out shortly.  
Second *Account and Application* creation. We need to create 100 clients in each API. To do this run:
http://localhost:8080/utilities-light4j/createAccountsAndApplications
	
7. Run your 4 microservices. Run the following inside each of api_a, api_b, api_c, api_d:
   mvn -Dmaven.test.skip=true clean install exec:exec&  	 
   Test each:
   		curl http://localhost:7004/apid/data12?apiKey=<apiKeyX>
   		curl http://localhost:7003/apic/data18?apiKey=<apiKeyX>
   		curl http://localhost:7002/apib/data11?apiKey=<apiKeyX>
   		curl http://localhost:7001/apia/data18?apiKey=<apiKeyX>
    			
  	


