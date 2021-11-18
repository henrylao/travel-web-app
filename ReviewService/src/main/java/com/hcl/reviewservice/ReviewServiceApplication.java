package com.hcl.reviewservice;

import com.hcl.reviewservice.repositories.ReviewRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = ReviewRepository.class)
public class ReviewServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReviewServiceApplication.class, args);
    }
}
