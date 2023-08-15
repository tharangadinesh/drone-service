package com.musala.drone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI().addSecurityItem(new SecurityRequirement().
				addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes
						("Bearer Authentication", createAPIKeyScheme()))
				.info(new Info().title("Drones REST API")
						.description("Drones service REST API.")
						.version("1.0")
						.contact(new Contact().name("Tharanga Dinesh")
								.email( "dinesh10fficial@gmail.com")
								.url(""))
						.license(new License().name("License of API")
								.url("API license URL")));
	}

	/*
	 * @Bean public OpenApiCustomiser sortTagsAlphabetically() { return openApi ->
	 * openApi.setTags(openApi.getTags() .stream() .sorted(Comparator.comparing(tag
	 * -> StringUtils.stripAccents(tag.getName()))) .collect(Collectors.toList()));
	 * }
	 */

	private SecurityScheme createAPIKeyScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP)
				.bearerFormat("JWT")
				.scheme("bearer");
	}
}
