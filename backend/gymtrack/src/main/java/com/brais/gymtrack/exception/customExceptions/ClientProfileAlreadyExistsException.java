package com.brais.gymtrack.exception.customExceptions;

public class ClientProfileAlreadyExistsException extends RuntimeException {
    public ClientProfileAlreadyExistsException() {
        super("Client profile already exists for this user");
    }
}
