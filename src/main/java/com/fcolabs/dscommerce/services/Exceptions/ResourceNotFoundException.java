package com.fcolabs.dscommerce.services.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) { 
        super(message); 
    }
}
