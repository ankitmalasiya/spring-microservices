package com.ankit.carleaseCustomer.service;

import com.ankit.carleaseCustomer.entity.Customer;
import com.ankit.carleaseCustomer.model.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {

    Customer save(CustomerDto customerDto);

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(int id);

    void deleteCustomerById(Customer customer);

}