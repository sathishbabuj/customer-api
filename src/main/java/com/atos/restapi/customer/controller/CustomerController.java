package com.atos.restapi.customer.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.atos.restapi.customer.dao.CustomerRepository;
import com.atos.restapi.customer.domain.Customer;
import com.atos.restapi.customer.exception.CustomerNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @PostMapping(value = "/customers", consumes = APPLICATION_JSON_UTF8_VALUE,
            produces =  APPLICATION_JSON_UTF8_VALUE )
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiOperation(value = "Create new customer")
    public ResponseEntity<?> newCustomer(@RequestBody @Valid Customer newCustomer, UriComponentsBuilder uriComponentsBuilder) {
        Customer customer = customerRepository.save(newCustomer);

        HttpHeaders headers = getHttpHeaders(uriComponentsBuilder, customer);
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }


    @GetMapping(value = "/customers/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Get Customer by Id")
    public Resource<Customer> getCustomerById(@PathVariable Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        return new Resource<>(customer,
                linkTo(methodOn(CustomerController.class).getCustomerById(id)).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"));
    }


    @GetMapping(value = "/customers", produces = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "Get all customers")
    public Resources<Resource<Customer>> getAllCustomers() {

        List<Resource<Customer>> customers = customerRepository.findAll().stream()
                .map(customer -> new Resource<>(customer,
                        linkTo(methodOn(CustomerController.class).getCustomerById(customer.getId())).withSelfRel(),
                        linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers")))
                .collect(Collectors.toList());

        return new Resources<>(customers,
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel());
    }


    @DeleteMapping(value = "/customers/{id}")
    @ApiOperation(value = "Delete Customer by Id")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.noContent().build();
        }
    }

    private HttpHeaders getHttpHeaders(UriComponentsBuilder uriComponentsBuilder, Customer customer) {
        UriComponents uriComponents = uriComponentsBuilder.path("/customers/{id}").buildAndExpand(customer.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return headers;
    }

}
