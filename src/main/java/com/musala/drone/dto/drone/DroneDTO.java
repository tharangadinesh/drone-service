package com.musala.drone.dto.drone;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.musala.drone.constant.DroneModel;
import com.musala.drone.constant.DroneState;
import com.musala.drone.dto.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class DroneDTO extends BaseDTO {

	@Max(value = 100, message = "Serial no cannot exceed 100 charactors")
	@NotEmpty
	private String serialNumber;

	@Max(value = 500, message = "Weight cannot be exceed 500g")
	@Min(value = 0, message = "Weight cannot be lower 0")
	@NotNull
	private Double weightLimit;

	@Max(value = 100, message = "Battery percentage cannot be exceed 100")
	@Min(value = 0, message = "Battery percentage cannot be lower 0")
	@NotNull
	private Double batteryCapacity;

	@NotNull
	private DroneModel model;

	@NotNull
	private DroneState state;

}