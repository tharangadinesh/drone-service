package com.musala.drone.payload.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse <T> {

	private int statusCode;
	private LocalDateTime timestamp;
	private String message;
	private T data;

	public SuccessResponse(int statusCode, String message, T data) {
		this.statusCode = statusCode;
		this.timestamp = LocalDateTime.now();
		this.message = message;
		this.data = data;
	}

}
