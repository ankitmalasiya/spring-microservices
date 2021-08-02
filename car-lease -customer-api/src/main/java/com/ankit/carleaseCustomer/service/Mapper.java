package com.ankit.carleaseCustomer.service;

public interface Mapper<T, U> {
    U map(T t);
}
