package com.ankit.carlease.service;

public interface Mapper<T, U> {
    U map(T t);
}
