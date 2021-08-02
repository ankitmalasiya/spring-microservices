package com.ankit.carlease.service;

import com.ankit.carlease.entity.Car;
import com.ankit.carlease.model.CarDto;
import com.ankit.carlease.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarService implements ICarService {

    private final CarRepository repository;
    private final CarMapper customerMapper;

    @Override
    public Car save(CarDto carDto) {
        if (validateRequest(carDto)) {
            Car customer = customerMapper.map(carDto);
            return repository.save(customer);
        }
        return null;
    }

    @Override
    public List<Car> getAllCars() {
        return repository.findAll();
    }

    @Override
    public Optional<Car> getCarById(int id) {
        return repository.findById(id);
    }

    @Override
    public void deleteCarById(Car customer) {
        repository.delete(customer);
    }

    private boolean validateRequest(CarDto carDto) {
//        String type = carDto.getType();
//        int shares = carDto.getShares();
//        if (!(type.equals("buy") || type.equals("sell"))) {
//            throw new InvalidInputException(SHARE_TYPE_ERROR_MSG);
//        }
//        if (shares < 1 || shares > 100)
//            throw new InvalidInputException(SHARES_ERROR_MSG);
        return true;
    }
}