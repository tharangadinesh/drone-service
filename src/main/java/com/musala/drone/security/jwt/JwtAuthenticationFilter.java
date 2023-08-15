package com.musala.drone.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.musala.drone.util.jwt.JwtUtil;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		// Extract the JWT token from the "Authorization" header
		String token = request.getHeader("Authorization");

		if (token != null) {
			if(token.startsWith("Bearer ")) {
				token = token.substring(7); // Remove the "Bearer " prefix
			}

			// Validate the JWT token
			String username = jwtUtil.extractUsername(token);
			if (username != null) {
				return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, null));
			}
		}

		return null; // If token validation fails, return null
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		// Set the authenticated user in the SecurityContext
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}
}
