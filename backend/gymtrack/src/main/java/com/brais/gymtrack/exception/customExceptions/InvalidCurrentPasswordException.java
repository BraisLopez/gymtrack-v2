package com.brais.gymtrack.exception.customExceptions;

public class InvalidCurrentPasswordException extends RuntimeException {
    public InvalidCurrentPasswordException() {
        super("Current password is incorrect.");
    }
}
