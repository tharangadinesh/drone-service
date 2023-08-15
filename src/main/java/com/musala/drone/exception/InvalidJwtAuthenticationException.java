package com.musala.drone.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidJwtAuthenticationException extends AuthenticationException {
   
	private static final long serialVersionUID = -6961750568973132737L;

	public InvalidJwtAuthenticationException(String e) {
        super(e);
    }
}
