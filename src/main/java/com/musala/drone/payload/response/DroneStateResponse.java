package com.musala.drone.payload.response;

import java.io.Serializable;

import com.musala.drone.constant.DroneState;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DroneStateResponse implements Serializable {

	private static final long serialVersionUID = 5926468583005150707L;

	private String serialNo;
	private DroneState droneState;

}