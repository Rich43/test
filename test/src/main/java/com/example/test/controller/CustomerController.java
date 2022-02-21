package com.example.test.controller;

import com.example.test.dto.Customer;
import com.example.test.service.CustomerService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(produces = "application/json")
    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping(produces = "application/json")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
    }
}
