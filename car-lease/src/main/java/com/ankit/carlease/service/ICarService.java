package com.ankit.carlease.service;

import com.ankit.carlease.entity.Car;
import com.ankit.carlease.model.CarDto;

import java.util.List;
import java.util.Optional;

public interface ICarService {

    Car save(CarDto customerDto);

    List<Car> getAllCars();

    Optional<Car> getCarById(int id);

    void deleteCarById(Car customer);

}