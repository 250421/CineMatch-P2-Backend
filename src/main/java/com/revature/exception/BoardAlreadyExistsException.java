package com.revature.exception;

public class BoardAlreadyExistsException extends RuntimeException {
    public BoardAlreadyExistsException(String message) {
        super(message);
    }
}
