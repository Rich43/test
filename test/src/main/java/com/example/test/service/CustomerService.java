package com.example.test.service;

import com.example.test.dto.Customer;
import com.example.test.dto.CustomerWithoutId;
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

    public Customer createCustomer(CustomerWithoutId customerWithoutId) {
        final var max = customers.stream().map(Customer::id).max(Long::compareTo).orElse(0L) + 1;
        final var customer = new Customer(max, customerWithoutId.firstname(), customerWithoutId.surname());
        customers.add(customer);
        return customer;
    }

    public void deleteCustomer(Long id) {
        customers.stream().filter(customer -> customer.id().equals(id)).findFirst().ifPresent(customers::remove);
    }
}
