package com.musala.drone.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.musala.drone.service.JpaUserDetailsServiceImpl;
import com.musala.drone.util.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JpaUserDetailsServiceImpl jpaUserDetailsService;
	private final JwtUtil jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


		String jwtToken = request.getHeader("Authorization");

		if (jwtToken == null) {
			filterChain.doFilter(request, response);
			return;
		}

		if(jwtToken.startsWith("Bearer ")) {
			jwtToken = jwtToken.substring(7); // Remove the "Bearer " prefix
		}

		// Validate the JWT token
		final String username = jwtUtils.extractUsername(jwtToken);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(username);

			if (Boolean.TRUE.equals(jwtUtils.validateToken(jwtToken, userDetails))) {

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		filterChain.doFilter(request, response);
	}
}