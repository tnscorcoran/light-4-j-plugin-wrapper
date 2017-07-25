/*
 *  The code in this repo is not supported by Red Hat/3scale. 
 *  Rather it's example code of how to use the supported 3scale 
 *  Java Plugin to achieve very low latency API Management. 
/*
/*
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package net.threescale.service;

import threescale.v3.api.AuthorizeResponse;
import threescale.v3.api.ServerError;

public interface PluginService {
    public String PROPERTIES_FILE = "/props.properties";		
    public String SERVICE_ID = "serviceid";		
    public String SERVICE_TOKEN = "servicetoken";		
    public String NO_SERVICE_ID_PROP_WARNING = "NO_SERVICE_ID_PROP_WARNING";		
    public String NO_SERVICE_TOKEN_PROP_WARNING = "NO_SERVICE_TOKEN_PROP_WARNING";		
    public String API_KEYS = "apiKeys";		
    public String METHODS = "methods";		

    public AuthorizeResponse authRep(String userKey, String methodOrMetricSystemName) throws ServerError;
    
}
