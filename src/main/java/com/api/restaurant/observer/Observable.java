package com.api.restaurant.observer;

public interface Observable {
    void addObserver(Observer observer);
    void notifyObservers();
}
