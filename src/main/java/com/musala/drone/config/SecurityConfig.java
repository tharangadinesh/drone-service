package com.musala.drone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.musala.drone.security.jwt.AuthEntryPointJwt;
import com.musala.drone.security.jwt.JwtAuthFilter;
import com.musala.drone.service.JpaUserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	private AuthEntryPointJwt unauthorizedHandler;

	private final JpaUserDetailsServiceImpl jpaUserDetailsService;

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(jpaUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(AbstractHttpConfigurer::disable)
		.csrf(AbstractHttpConfigurer::disable)
		.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
		.authorizeHttpRequests(auth -> auth
				.antMatchers("/api/v1/login").permitAll()
				.antMatchers(
						"/api/auth/**",
						"/swagger-ui-custom.html" ,
						"/swagger-ui.html",
						"/swagger-ui/**",
						"/v3/api-docs/**",
						"/webjars/**",
						"/swagger-ui/index.html",
						"/api-docs/**") .permitAll()
				.antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/swagger-ui.html").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.anyRequest().authenticated())
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		.userDetailsService(jpaUserDetailsService);

		http.headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));

		http.authenticationProvider(authenticationProvider());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}

