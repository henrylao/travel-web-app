package com.hcl.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.client.RestTemplate;


@SpringBootApplication
public class UserServiceApplication {
    ////@EnableEurekaClient
//@EnableDiscoveryClient
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

//    @LoadBalanced
//    @Bean
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }
}
