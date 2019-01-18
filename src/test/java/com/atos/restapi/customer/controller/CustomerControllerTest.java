package com.atos.restapi.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.atos.restapi.customer.dao.CustomerRepository;
import com.atos.restapi.customer.domain.Customer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.*;

public class CustomerControllerTest {

    public static final String CUSTOMERS_URI = "/customers";
    public static final String JOHN = "John";
    public static final String JOE = "Joe";
    public static final String RYAN = "Ryan";
    public static final String DAN = "Dan";
    public static final String CUSTOMERS_1_URL = "/customers/1";

    @Mock
    private CustomerRepository customerRepository;

    private MockMvc mockMvc;


    @Before
    public void setup() {
        initMocks(this);
        this.mockMvc = standaloneSetup(new CustomerController(customerRepository))
                .build();
    }

    @Test
    public void shouldReturnStatusCodeOf400BadRequestIfMandatoryFieldsForCustomerAreMissing() throws Exception {

        mockMvc.perform(
                post(CUSTOMERS_URI).contentType(MediaType.APPLICATION_JSON_UTF8).content("{}")
        ).andDo(print()).andExpect(status().isBadRequest());

        verify(customerRepository, never()).save(any(Customer.class));

    }

    @Test
    public void shouldReturnStatusCodeOf201AndCreateANewCustomer()  throws Exception {
        Customer customer = Customer.builder().id(1L).firstName("John").surName(JOE).build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        mockMvc.perform(
                post(CUSTOMERS_URI)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("{\"firstName\" : \"John\",\"surName\" : \"Joe\"}")
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect( MockMvcResultMatchers.header().string("Location", "http://localhost/customers/1"));

    }

    @Test
    public void shouldReturnStatusCodeOf200AndCustomerForGivenCustomerId() throws Exception {
        Customer customer = Customer.builder().id(1L).firstName("John").surName(JOE).build();
        Optional<Customer> optionalCustomer = Optional.of(customer);
        when(customerRepository.findById(1L)).thenReturn(optionalCustomer);

        mockMvc.perform(
                get(CUSTOMERS_1_URL)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.surName").value(JOE))
                .andExpect(jsonPath("$.links[0].href").value("http://localhost/customers/1"));

    }


    @Test
    public void shouldReturnStatus404IfCustomerNotFoundForGivenId() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(
                get(CUSTOMERS_1_URL)
        ).andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldReturnListOfAllCustomers() throws Exception {

        Customer customer1 = Customer.builder().id(1L).firstName(JOHN).surName(JOE).build();
        Customer customer2 = Customer.builder().id(2L).firstName(RYAN).surName(DAN).build();

        List<Customer> customers = new ArrayList<>(Arrays.asList(customer1, customer2));

        when(customerRepository.findAll()).thenReturn(customers);

        mockMvc.perform(
                get(CUSTOMERS_URI)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content.length()").value("2"))
                .andExpect(jsonPath("$.content[0].id").value("1"))
                .andExpect(jsonPath("$.content[0].firstName").value(JOHN))
                .andExpect(jsonPath("$.content[0].surName").value(JOE))
                .andExpect(jsonPath("$.content[0].links[0].href").value("http://localhost/customers/1"))
                .andExpect(jsonPath("$.content[1].id").value("2"))
                .andExpect(jsonPath("$.content[1].firstName").value(RYAN))
                .andExpect(jsonPath("$.content[1].surName").value(DAN))
                .andExpect(jsonPath("$.content[1].links[0].href").value("http://localhost/customers/2"));
    }

    @Test
    public void shouldReturnEmptyContentIfNoCustomerExists() throws Exception {
        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(
                get(CUSTOMERS_URI)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.content.length()").value("0"));

    }

    @Test
    public void shouldDeleteCustomerByIdAndReturnStatusCodeOf200() throws Exception {

        doNothing().when(customerRepository).deleteById(1L);

        mockMvc.perform(
                delete(CUSTOMERS_1_URL)
        ).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnAStatusCodeOf204IfNoCustomerFoundForGivenIdToDelete() throws Exception {
       doThrow(new EmptyResultDataAccessException(1)).when(customerRepository).deleteById(1L);

        mockMvc.perform(
                delete(CUSTOMERS_1_URL)
        ).andDo(print())
                .andExpect(status().isNoContent());

    }
}
