package com.atos.restapi.customer.domain;

import lombok.Builder;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String surName;
}
