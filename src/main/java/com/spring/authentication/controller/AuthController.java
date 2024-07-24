package com.spring.authentication.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.authentication.config.TokenProvider;
import com.spring.authentication.dtos.JwtDto;
import com.spring.authentication.dtos.SignInDto;
import com.spring.authentication.dtos.SignUpDto;
import com.spring.authentication.entity.User;
import com.spring.authentication.exceptions.InvalidJwtException;
import com.spring.authentication.service.AuthService;



@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
	
	  @Autowired
	  private AuthService authService;
	
	  @Autowired
	  private AuthenticationManager authenticationManager;
	
	  @Autowired
	  private TokenProvider tokenService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody @Validated SignUpDto data) throws InvalidJwtException {
			authService.signUp(data);
		return ResponseEntity.status(HttpStatus.CREATED).build(); 
	}
	
	
	
	@PostMapping("/signin")
    public ResponseEntity<JwtDto> signIn(@RequestBody @Validated SignInDto data) throws AuthenticationException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
		var authUser = authenticationManager.authenticate(usernamePassword);
		var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());

		return ResponseEntity.ok(new JwtDto(accessToken));
    }
	


}
