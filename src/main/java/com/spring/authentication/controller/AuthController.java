package com.spring.authentication.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.authentication.config.TokenProvider;
import com.spring.authentication.dtos.JwtDto;
import com.spring.authentication.dtos.RefreshTokenRequest;
import com.spring.authentication.dtos.ResponseDto;
import com.spring.authentication.dtos.SignInDto;
import com.spring.authentication.dtos.SignUpDto;
import com.spring.authentication.entity.User;
import com.spring.authentication.exceptions.InvalidJwtException;
import com.spring.authentication.service.AuthService;
import com.spring.authentication.util.Constants;



@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
	
	  @Autowired
	  private AuthService authService;
	
	  @Autowired
	  private AuthenticationManager authenticationManager;
	
	  @Autowired
	  private TokenProvider tokenProvider;
	  
	
	  

	@PostMapping("/signup")
	public ResponseDto signUp(@RequestBody @Validated SignUpDto data) throws InvalidJwtException {
			
		return authService.signUp(data);
	}
	
	
	
	@PostMapping("/signin")
    public ResponseDto signIn(@RequestBody @Validated SignInDto data) throws AuthenticationException {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
		var authUser = authenticationManager.authenticate(usernamePassword);
		var accessToken = tokenProvider.generateAccessToken((User) authUser.getPrincipal());
		//var refreshToken = tokenProvider.generateRefreshAccessToken(new HashMap<>(), (User) authUser.getPrincipal());
		var refreshToken=tokenProvider.generateRefreshToken((User) authUser.getPrincipal());

		return ResponseDto.builder()
				.message(Constants.SIGNIN)
				.data(new JwtDto(accessToken,refreshToken))
				.statusCode(200)
				.build();
    }

	
	
	@PostMapping("/refresh")
    public ResponseDto refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        try {
            String newAccessToken = tokenProvider.refreshAccessToken(request.getRefreshToken());
            String refreshToken = request.setRefreshToken(newAccessToken);
            return ResponseDto.builder().message(Constants.CREATED).data(refreshToken).statusCode(200).build();
        } catch (Exception e) {
            return ResponseDto.builder().statusCode(401).message("Invalid refresh token").build();
            
        }
    }
	
	
	
	
//	@PostMapping("/refresh")
//	public JwtDto refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
//	    try {
//	        String username = tokenService.extractUserName(refreshTokenRequest.getRefreshToken());
//	        User user = (User) userRepository.findByLogin(username);
//
//	        if (tokenService.isTokenValid(refreshTokenRequest.getRefreshToken(), user)) {
//	            String jwt = tokenService.generateAccessToken(user);
//	            JwtDto jwtDto=new JwtDto();
//	            jwtDto.setAccessToken(jwt);
//	            jwtDto.setRefreshToken(refreshTokenRequest.getRefreshToken());
//	            
//	           
//	            return jwtDto;
//	        }
//
//	        throw new RuntimeException("Invalid token");
//	    } catch (RuntimeException e) {
//
//	        System.err.println(e.getMessage());
//
//	        return null;
//	    }
//	
//	}




	


}
