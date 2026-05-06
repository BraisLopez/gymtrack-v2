package com.brais.gymtrack.exception.customExceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException() {
        super("Client profile not found");
    }
    
    public ClientNotFoundException(Long clientId) {
        super("Client profile not found with ID:" + clientId);
    }
}
