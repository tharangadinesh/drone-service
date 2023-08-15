package com.musala.drone.model.drone;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.musala.drone.constant.DroneModel;
import com.musala.drone.constant.DroneState;
import com.musala.drone.model.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Drone extends BaseModel{

	private static final long serialVersionUID = -2368036597033811588L;

	@Column(nullable = false, length = 100)
	private String serialNumber;

	@Column(nullable = false)
	private Double weightLimit;

	@Column(nullable = false)
	private Double batteryCapacity;

	@Column(nullable = false)
	private DroneModel model;

	@Column(nullable = false)
	private DroneState state;

}