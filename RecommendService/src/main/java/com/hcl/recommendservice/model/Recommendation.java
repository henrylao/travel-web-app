package com.hcl.recommendservice.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(
//        schema = "micro",
        name = "recommendations"
)
@Data
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @NotBlank
    private Long destId;

    @NotBlank
    private String author;

    @NotBlank
    private int rate;

    @NotBlank
    private String content;
}
