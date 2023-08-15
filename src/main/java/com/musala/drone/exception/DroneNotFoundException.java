package com.musala.drone.exception;

public class DroneNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6961750568973132737L;

	public DroneNotFoundException(String e) {
		super(e);
	}
}
