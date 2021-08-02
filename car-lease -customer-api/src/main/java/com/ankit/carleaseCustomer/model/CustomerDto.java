package com.ankit.carleaseCustomer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    private Integer id;
//    private AddressDto address;

    private String name;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String place;
    private String EmailAddress;
    private String phoneNumber;
}