package com.musala.drone.exception;

public class LimitExceedException extends RuntimeException {

	private static final long serialVersionUID = -6961750568973132737L;

	public LimitExceedException(String e) {
		super(e);
	}
}
