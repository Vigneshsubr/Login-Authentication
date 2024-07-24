package com.spring.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.authentication.config.TokenProvider;

import com.spring.authentication.dtos.SignUpDto;
import com.spring.authentication.entity.User;
import com.spring.authentication.exceptions.InvalidJwtException;
import com.spring.authentication.repository.UserRepository;

@Service
@Lazy
public class AuthService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	TokenProvider tokenService;

	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		var user=userRepository.findByLogin(username);
		return  user;
	}


	public UserDetails signUp(SignUpDto data) throws InvalidJwtException {
		if(userRepository.findByLogin(data.getLogin())!=null) {
			throw new InvalidJwtException("User all ready exists");
			
		}
		 String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

//		    User newUser = new User(data.getLogin(), encryptedPassword, data.getRole());
//		    return userRepository.save(newUser);
		 
		 User user=new User();
		 user.setLogin(data.getLogin());
		 user.setPassword(encryptedPassword);
		 user.setRole(data.getRole());
		 return userRepository.save(user);

		
	}

	
}
