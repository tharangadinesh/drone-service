package com.musala.drone.controller.auth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.musala.drone.payload.request.AuthenticationRequest;
import com.musala.drone.payload.response.AuthenticationResponse;
import com.musala.drone.service.JpaUserDetailsServiceImpl;
import com.musala.drone.util.jwt.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Authentication")
public class AuthRestController {

	private final AuthenticationManager authenticationManager;

	private final JpaUserDetailsServiceImpl jpaUserDetailsService;

	private final JwtUtil jwtUtils;

	@PostMapping("/api/v1/login")
	@Operation(summary = "This method is used to login")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully logged",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = AuthenticationResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad credentials", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	public AuthenticationResponse authenticate(@RequestBody final AuthenticationRequest request, HttpServletResponse response) {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( request.getUsername(), request.getPassword()));
		} catch (final BadCredentialsException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		final UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(request.getUsername());
		final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setAccess_token(jwtUtils.generateToken(userDetails));

		return authenticationResponse;

	}
}
