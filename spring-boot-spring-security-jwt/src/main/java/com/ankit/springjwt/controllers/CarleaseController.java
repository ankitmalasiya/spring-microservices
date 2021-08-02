package com.ankit.springjwt.controllers;

import com.ankit.springjwt.models.Car;
import com.ankit.springjwt.models.Customer;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/carlease")

public class CarleaseController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    //    HystrixCommand Bulkhead pattern
    @GetMapping("/cars")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @HystrixCommand(
            fallbackMethod = "fallbackCars",
            threadPoolKey = "carThreadPool-bulkhead-pattern",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            }
    )
//    HystrixCommand handles the fallback mechanism > In case there is a slow microservice which eats up all the resources (piles up thread)
//    instead of returning error mesage fallback method will be called and cached response will be returned. There are 3 ways:
//    1. Increase the instances/resources or set the timeout
//    2. Circuit breaker where calling microservices sets criteria after which it will trip/stop for sometime and start processing again
//    3. Bulkhead pattern, resources will be splitted in groups/buckets. So if a service is slow it only blocks those resources and do not
//    impact other services by consuming other service resources

//    Hystrix command works on Bean/Component level -> When a request comes from outside it actually calls the proxy object and only call
//    actual implementation when it is used. If HystrixCommand is called from a method instead of a request from outside it will not treat
//    as proxy and HystrixCommand will not work as expected.

    public ResponseEntity<Car> getCars() {
        Car car = restTemplate.getForObject("http://carlease-api/cars/1", Car.class);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    public ResponseEntity<Car> fallbackCars() {
        return new ResponseEntity<>(new Car(), HttpStatus.OK);
    }


    //    HystrixCommand CircuitBreaker example

    @GetMapping("/customers")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @HystrixCommand(groupKey = "StoreSubmission", commandKey = "StoreSubmission", threadPoolKey = "StoreSubmission", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "30000"),
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "60000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000")}, threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "30"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "180000")})
    public ResponseEntity<Customer> getCustomers() {
        Customer customer = restTemplate.getForObject("http://carlease-customer-api/customers/1", Customer.class);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}