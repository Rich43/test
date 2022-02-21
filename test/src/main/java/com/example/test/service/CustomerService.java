package com.example.test.service;

import com.example.test.dto.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomerService {
    // I would normally use a repository here, but I'm using a collection for simplicity
    private static final Collection<Customer> customers = new ArrayList<>();

    public Collection<Customer> getAllCustomers() {
        return customers;
    }

    public Customer createCustomer(Customer customer) {
        customers.add(customer);
        return customer;
    }

    public void deleteCustomer(Long id) {
        customers.stream().filter(customer -> customer.id().equals(id)).findFirst().ifPresent(customers::remove);
    }
}
