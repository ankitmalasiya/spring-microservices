package com.ankit.carleaseCustomer.service;

import com.ankit.carleaseCustomer.entity.Customer;
import com.ankit.carleaseCustomer.model.CustomerDto;
import com.ankit.carleaseCustomer.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper customerMapper;
    private static final String SHARE_TYPE_ERROR_MSG = "Share type can only be 'buy' or 'sell'.";
    private static final String SHARES_ERROR_MSG = "Shares should be between 1 and 100.";

    @Override
    public Customer save(CustomerDto customerDto) {
        if (validateRequest(customerDto)) {
            Customer customer = customerMapper.map(customerDto);
            return repository.save(customer);
        }
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(int id) {
        return repository.findById(id);
    }

    @Override
    public void deleteCustomerById(Customer customer) {
        repository.delete(customer);
    }

    private boolean validateRequest(CustomerDto customerDto) {
//        String type = customerDto.getType();
//        int shares = customerDto.getShares();
//        if (!(type.equals("buy") || type.equals("sell"))) {
//            throw new InvalidInputException(SHARE_TYPE_ERROR_MSG);
//        }
//        if (shares < 1 || shares > 100)
//            throw new InvalidInputException(SHARES_ERROR_MSG);
        return true;
    }
}