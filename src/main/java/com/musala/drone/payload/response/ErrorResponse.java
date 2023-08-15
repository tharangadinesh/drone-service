package com.musala.drone.payload.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse<T> {

	private T message;
	private String details;
	private final LocalDateTime timestamp = LocalDateTime.now();

	public ErrorResponse(T message, String details) {
		super();
		this.message = message;
		this.details = details;
	}

}
