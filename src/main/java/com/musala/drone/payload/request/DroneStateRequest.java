package com.musala.drone.payload.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.musala.drone.constant.DroneState;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneStateRequest implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	@NotNull
	private String serialNo;

	@NotNull
	private DroneState droneState;

	@NotNull
	private Double batteryLevel;
}