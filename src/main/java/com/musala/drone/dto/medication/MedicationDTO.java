package com.musala.drone.dto.medication;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.musala.drone.dto.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MedicationDTO extends BaseDTO{

	@NotEmpty
	@Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "Name must contain only letters, numbers, '-' and '_'")
	private String name;

	@NotEmpty
	@Pattern(regexp = "^[A-Z0-9_]+$", message = "Code must contain only upper case letters, numbers, and '_'")
	private String code;

	private String image;

	@NotEmpty
	private Double weight;

	@NotEmpty
	private String droneSerialNo;

}
