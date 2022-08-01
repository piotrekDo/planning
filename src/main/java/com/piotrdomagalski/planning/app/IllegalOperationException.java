package com.piotrdomagalski.planning.app;

public class IllegalOperationException extends RuntimeException{
    public IllegalOperationException(String message) {
        super(message);
    }
}
