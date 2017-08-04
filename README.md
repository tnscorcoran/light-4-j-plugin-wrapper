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

2. Prepare your 3scale account.
As discussed in the [Blog](http://middlewareblog.redhat.com/2017/07/25/low-latency-api-management-for-microservices-framework-light-4-j-with-red-hat-3scale/) in this example, we use 4 Microservices represented by 4 light-4-j components in this repo: swagger-light-java-a, swagger-light-java-b etc. These 4 Microservices are represented by 4 3scale Services.  
	  
	2.1 Add 4 3scale Services. ![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/1-3scaleServices.png)  
	Your first service will already exist with the name *API*. Open it, choose Definition, Edit it and change its name to Service A. System is *api* and is not editable.
	For your remaining 3 Services, choose Create Service. Give each one a system name like *service-b* and a name like *Service B*, repeating for C and D.  
	  
	2.2 Setup & Remove your 3scale Default Application Plans. Open each Service and click on Published Application Plans.  
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/2-Applications-PlansLink.png)  
	Service A will have 2, the others will have none. With Service A, there will be 2 a Basic and Unlimited. Leave them unchanged. With the other 3 services, you'll need to create an Application Plan. Just enter a descriptive Name and System name (no spaces). For all 4, make sure the Default Plan is blank. This ensures when we create accounts, a default Application will not be created (we'll be doing this ourselves using the 3scale APIs).
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/2-Applications-PlansList.png)  
	  
	2.3 Retrieve your Service Ids and Tokens. These are required to make Reporting and Authorization from Plugin to 3scale API Manager.  
	First get your 4 Service Ids. Open each Service's Overview and get it as shown. (with 3scale On Prem this is likely to be a single or double digit number). Keep note of each one and the service it relates to.
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/3-Service-Id.png)  
	Now get your Service Tokens. Go to Gear Sign -> Personal Settings -> Tokens. Copy your 4 tokens - you'll need them when configuring your Plugin Wrappers.
	![Alt text](https://github.com/tnscorcoran/light-4-j-plugin-wrapper/blob/master/_images/4-Service-tokens.png)  
	
3. Update your Plugin Wrappers and build. For each of JPW-A, JPW-B, JPW-C, JPW-D, open /src/main/resources.props.properties. Set your serviceid and servicetoken to the correct one for Services A, B, C and D. Now open each of these project's pom.xml. Ensure the version of the 3scale-api dependency is the same as the actual component in 1. above.  
Run *mvn clean install* on each.
	
4. Update your APIs and build. For each of api_a, api_b, api_c, api_d, add the same properties file to as you inserted to JPW-A, JPW-B, JPW-C, JPW-D respectively. (JPW-x's are picked up at build time, api_x's at runtime).  
Note - taking API code from this Repo is an interim approach to provisioning the APIs. Time permitting, we will enhance this README and Repo to reflect the recommended OAI spec driven approach on the [Light 4 J Chained Microservices Tutorial](https://networknt.github.io/light-rest-4j/tutorial/ms-chain/) 

5. Use the 3scale APIs to populate required data on 3scale. There are 2 major repetitive tasks we need to undertake (details below).  
	5.1.1 Create our *3scale Methods*. These are 3scale representations of the Microservice endpoints used to access control and traffic analytics. In each of our 4 Microservices, there are 25 endpoints. We will reflect this in 3scale with 25 methods in each of our 4 3scale Services.  
	5.1.2 Create our 100 clients - represented by Applications in 3scale.  
For those tasks, we use the utility project included in this repo, *utilities-light4j*.  
First we need to initialize its props.properties with various Ids associated with our 3scale account.  
We also have a third task applicable to JMeter:  
	5.1.3  Generation of a CSV file with random data elements drawn from our clients and methods to simulate client API calls to Microservices.   
These 3 are driven off a local Tomcat web app configured to run these utilities.
![Utilities-props.properties](https://raw.githubusercontent.com/tnscorcoran/light-4-j-plugin-wrapper/master/_images/5-Utilities-props.properties.png)  
Before running it, we need to initialize its /src/main/resources.props.properties. Open that file and make the following changes:

	5.2. The following points refer to the screenshot items. 
	1 Get your 3scale Access token (used to create gateway). Go to Gear sign - Personal Settings - Tokens. Add Access token. Name it, give it read/write access to all scopes and Create Access token. Copy it (you won't be able to see it again). We'll refer to this your *3scale-access-token*   
  	2  Insert your 4 Application Plan Ids. Use the *Basic* plan on Microservice A and the other 3 you created in 2.2. above. Navigate to each one and it will be in your browser address bar.  
  	3  Set a random default password for your accounts. As this is a test, they will not be used but rather are needed to create Accounts using the 3scale API.    
  	4  Use any random string   
  	5  URL of your 3scale API Manager. Something like mycoolhost-admin.3scale.net   
  	6  Id of Microservice A, retrieved in 2.3 above.   
  	7  Path where you want generated CSV to be   
  	8  Path where you want generated CSV to be   
  	9  Host or IP of the machine you will run each Microservice   
  
To start run *mvn tomcat7:run*
	
First *method* creation. We have 25 endpoints in each of the 4 Microservices A, B, C and D. These 100 endpoints each have a logical *method* defined on 3scale. Traffic Authorization and Reporting is done on these methods. We use a logical naming convention to build the method at runtime. e.g. a request with a path /apid/data12 is translated into a method called apid_data12. Creation and storage of these 100 method is task 1.  	
Assuming you are running Tomcat on localhost, hit this URL:  
http://localhost:8080/utilities-light4j/createMethods  
Within a couple of minutes your curl or web page should return a successful status message.
  

Second *Account and Application* creation. We need to create 100 clients in each API. Be sure number-accounts-to-create=100 in your utilities properties file. You may want to revery this to 0 in your props file afterwards so extra accounts are not inadvertently created. 
Run:
http://localhost:8080/utilities-light4j/createAccountsAndApplications
After several minutes your curl or web page should return a successful status message.

Third (optional) *Build CSV files*. These feed into the JMeter jmx file that is used for the test we did on the blog. It's located in the */load_test/input* dir of this repo. We have a CSV to initialize the cache and one with a random selection of clients and endpoints. The first is not really necessary as you cache will *warm up* by running the second.
First ensure you have entered the properties cache-initializer-csv-file and load-test-csv-file in the repo's /light-4-j-plugin-wrapper/utilities-light4j/src/main/resources/props.properties 
Start Tomcat and run the following to generate your CSV file:  
http://localhost:8080/utilities-light4j/buildCSVfiles	
Within a couple of minutes your curl or web page should return a successful status message.

6. Run your 4 microservices. Run the following inside each of api_a, api_b, api_c, api_d:
   mvn -Dmaven.test.skip=true clean install exec:exec&  	
   They will be exposed under ports 7001, 7002, 7003, 7004 respectively. 
   Test each:  
   		curl http://localhost:7001/apia/data18?apiKey=apiKeyZ  
   		curl http://localhost:7002/apib/data11?apiKey=apiKeYX  
   		curl http://localhost:7003/apic/data18?apiKey=apiKeyX  
   		curl http://localhost:7004/apid/data12?apiKey=apiKeyW  
