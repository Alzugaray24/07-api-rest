package com.api.restaurant.services.interfaces;

public interface ICommand<T> {
    T execute();
}