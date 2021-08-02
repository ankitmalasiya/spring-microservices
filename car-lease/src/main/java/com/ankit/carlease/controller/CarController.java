package com.ankit.carlease.controller;

import com.ankit.carlease.entity.Car;
import com.ankit.carlease.model.CarDto;
import com.ankit.carlease.service.ICarService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping
public class CarController {

    private final ICarService carService;

    @PostMapping("/cars")
    public ResponseEntity<Car> saveCarDto(@RequestBody CarDto carDto) {
        Car savedCarDto = carService.save(carDto);
        return new ResponseEntity<>(savedCarDto, HttpStatus.CREATED);
    }

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String userId
    ) {
        List<Car> Cars = new ArrayList<>(carService.getAllCars());
        return new ResponseEntity<>(Cars, HttpStatus.OK);
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") int id) {
        Optional<Car> Car = carService.getCarById(id);
        return Car.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@RequestBody CarDto carDto, @PathVariable("id") int id) {
        Optional<Car> customer = carService.getCarById(id);
        if (customer.isPresent()) {
            carDto.setId(id);
            carService.save(carDto);
        }
        return customer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable("id") int id) {
        if (carService.getCarById(id).isPresent()) {
            carService.deleteCarById(carService.getCarById(id).get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}