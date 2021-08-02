package com.ankit.carleaseCustomer.service;

import com.ankit.carleaseCustomer.entity.Customer;
import com.ankit.carleaseCustomer.model.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements Mapper<CustomerDto, Customer> {

    @Override
    public Customer map(CustomerDto dto) {
        return Customer.builder()
                .id(dto.getId())
                .name(dto.getName())
                .street(dto.getStreet())
                .houseNumber(dto.getHouseNumber())
                .zipCode(dto.getZipCode())
                .place(dto.getPlace())
                .phoneNumber(dto.getPhoneNumber())
                .emailAddress(dto.getEmailAddress())
                .build();
    }
}