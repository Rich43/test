package com.example.test.controller;

import com.example.test.dto.Customer;
import com.example.test.dto.CustomerWithoutId;
import com.example.test.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        customerController = new CustomerController(customerService);
    }

    @Test
    void givenEmptyCustomerListWhenGettingAllCustomersThenReturnEmptyCustomerList() {
        Collection<Customer> allCustomers = customerController.getAllCustomers();
        assertTrue(allCustomers.isEmpty());
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void shouldCreateOneCustomerAndCallCustomerService() {
        CustomerWithoutId customerWithoutId = new CustomerWithoutId("John", "Doe");
        Customer customer = new Customer(1L, "John", "Doe");
        when(customerService.createCustomer(customerWithoutId)).thenReturn(customer);
        Customer returnedCustomer = customerController.createCustomer(customerWithoutId);
        assertEquals(customer, returnedCustomer);
        verify(customerService, times(1)).createCustomer(customerWithoutId);
    }

    @Test
    void whenDeletingACustomerCustomerServiceShouldHaveBeenCalled() {
        customerController.deleteCustomer(1L);
        verify(customerService, times(1)).deleteCustomer(1L);
    }
}