# Light-4-j-plugin-wrapper

Disclaimer: The code in this repo is not supported by Red Hat/3scale. Rather it's example code of how you can and we did achieve very low latency API Management. We did this by applying a wrapper around the supported 3scale Java Plugin which uses caching and asynchronous calls to 3scale. More details below.

Introduction  

This Repo contains the source code and instructions for running the solution detailed in Kavitha Srinivasan's and Tom Corcoran's recent Blog Low Latency API Management for Microservices framework Light-4-J - with Red Hat 3scale <INSERT LINK>.
The blog outlines an approach to achieve ultra low latency API Management provided by 3scale and implemented as a Java Plugin and Wrapper for the popular Microservices Framework https://networknt.github.io/light-rest-4j/ Seee the blog for high level details on the approach.

Implementation  
Pre-Requisites:  
A 3scale API Management account - preferably On Prem.

Instructions
1. Download and install the Java Plugin: https://github.com/3scale/3scale_ws_api_for_java
   Before running mvn clean install, set the DEFULAT_HOST here: https://github.com/3scale/3scale_ws_api_for_java/blob/master/src/main/java/threescale/v3/api/ServiceApi.java to be that of you 3scale API Management back end.
   Run mvn clean install.
   Take note of the version of line 6 in pom.xml in the root directory of the Repo.

2. Clone this repo.

3. Prepare your 3scale account.
As discussed in the Blog <INSERT LINK> in this example, we use 4 Microservices represented by 4 light-4-j components in this repo: swagger-light-java-a, swagger-light-java-b etc. These 4 Microservices are represented by 4 3scale Services.
<Insert 4 3scale Services>

Make the following Modifications. 

