package com.atos.restapi.customer;

import com.atos.restapi.customer.controller.CustomerController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackageClasses = {
        CustomerController.class
})
public class CustomerApplication {

    public static void main(String... args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
