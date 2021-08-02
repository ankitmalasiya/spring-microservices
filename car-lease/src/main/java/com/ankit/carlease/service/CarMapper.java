package com.ankit.carlease.service;

import com.ankit.carlease.entity.Car;
import com.ankit.carlease.model.CarDto;
import org.springframework.stereotype.Component;

@Component
public class CarMapper implements Mapper<CarDto, Car> {

    @Override
    public Car map(CarDto dto) {
        return Car.builder()
                .id(dto.getId())
                .make(dto.getMake())
                .model(dto.getModel())
                .version(dto.getVersion())
                .noOfDoors(dto.getNoOfDoors())
                .co2Emission(dto.getCo2Emission())
                .grossPrice(dto.getGrossPrice())
                .netPrice(dto.getNetPrice())
                .build();
    }
}