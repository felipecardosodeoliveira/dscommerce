package com.fcolabs.dscommerce.services.Exceptions;

import jakarta.persistence.EntityNotFoundException;

public class EntityNotFound extends EntityNotFoundException {
    public EntityNotFound(String message) { 
        super(message); 
    }
}
