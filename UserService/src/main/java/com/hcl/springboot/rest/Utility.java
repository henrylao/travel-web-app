package com.hcl.springboot.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Utility {
    static Logger logger = LoggerFactory.getLogger(Utility.class);

    /**
     * GET Method for querying REST API endpoints for JSON Arrays
     *
     * @param uriEndpoint Ex. http://localhost:%d//%s//%s
     * @param port
     * @param resourceName
     * @param extension
     * @return - String | body of the response
     */
    public static String getJSONAtEndpoint(String uriEndpoint, int port, String resourceName, String extension) {
//        final String uri = String.format("http://localhost:%d//%s//%s", port, resourceName, extension);
        final String uri = String.format(uriEndpoint, port, resourceName, extension);
        logger.trace(uri);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        logger.trace(String.valueOf(response));
        return String.valueOf(response.getBody());
    }

}
