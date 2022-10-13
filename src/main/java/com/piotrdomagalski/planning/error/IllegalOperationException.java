package com.piotrdomagalski.planning.error;

/**
 * Unique error regarding taking not allowed operation.
 */

public class IllegalOperationException extends RuntimeException {
    public IllegalOperationException(String message) {
        super(message);
    }
}
