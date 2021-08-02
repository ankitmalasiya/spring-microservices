package com.ankit.carlease.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDto {

    private Integer id;

    private String make;
    private String model;
    private Integer version;
    private String noOfDoors;
    private String co2Emission;
    private Integer grossPrice;
    private String netPrice;

}