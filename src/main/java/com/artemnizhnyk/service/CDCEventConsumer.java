package com.artemnizhnyk.service;

public interface CDCEventConsumer {

    void handle(String message);
}
