package com.spring.authentication.exceptions;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJwtException extends AuthenticationException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidJwtException(String ex) {
		super(ex);
	}

}
