package com.fcolabs.dscommerce.services;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcolabs.dscommerce.DTO.UserDTO;
import com.fcolabs.dscommerce.entities.Role;
import com.fcolabs.dscommerce.entities.User;
import com.fcolabs.dscommerce.projections.UserDetailsProjection;
import com.fcolabs.dscommerce.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;

	UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
		
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}
		
		User user = new User();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}

	protected User authenticated() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();			
  		 	Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
			String username = jwtPrincipal.getClaim("username");
			return userRepository.findByEmail(username).get();
		} catch (Exception e) {
			throw new UsernameNotFoundException("Usuário não encontrado.");
		}
	}

	@Transactional(readOnly = true)
	public UserDTO getMe() {
		return new UserDTO(authenticated());
	}
}
