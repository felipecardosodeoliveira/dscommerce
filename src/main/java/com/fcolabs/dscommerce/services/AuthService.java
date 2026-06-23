package com.fcolabs.dscommerce.services;

import org.springframework.stereotype.Service;

import com.fcolabs.dscommerce.entities.User;
import com.fcolabs.dscommerce.services.Exceptions.ForbiddenException;

@Service
public class AuthService {
    
    private final UserService userService;

    AuthService(UserService userService) {
        this.userService = userService;
    }
    
    public void validateSelfOrAdmin(Long userId) {
        User me = userService.authenticated();
		if(!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
    }
}
