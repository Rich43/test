package com.example.test.service;

import com.example.test.dto.CustomerWithoutId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerServiceTest {

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService();
    }

    @Test
    void whenCallingGetAllCustomersReturnEmptyCollection() {
        final var allCustomers = customerService.getAllCustomers();
        assertTrue(allCustomers.isEmpty());
    }

    @Test
    void whenCreatingACustomerGetAllCustomersShouldReturnOneCustomer() {
        final var customer = customerService.createCustomer(new CustomerWithoutId("John", "Doe"));
        final var allCustomers = customerService.getAllCustomers();
        assertEquals(1, allCustomers.size());
        assertEquals(customer, allCustomers.stream().findFirst().orElse(null));
    }

    @Test
    void shouldDeleteACustomerAfterCreatingItFirst() {
        final var customer = customerService.createCustomer(new CustomerWithoutId("John", "Doe"));
        final var allCustomers = customerService.getAllCustomers();
        assertEquals(1, allCustomers.size());
        assertEquals(customer, allCustomers.stream().findFirst().orElse(null));
        customerService.deleteCustomer(customer.id());
        final var allCustomersAfterDeletion = customerService.getAllCustomers();
        assertTrue(allCustomersAfterDeletion.isEmpty());
    }
}
