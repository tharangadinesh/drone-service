package com.musala.drone.controller.drone;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musala.drone.dto.drone.DroneDTO;
import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.exception.DroneException;
import com.musala.drone.exception.LimitExceedException;
import com.musala.drone.exception.MedicationException;
import com.musala.drone.payload.request.DroneStateRequest;
import com.musala.drone.payload.response.DroneStateResponse;
import com.musala.drone.payload.response.SuccessResponse;
import com.musala.drone.service.drone.api.IDroneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/drones")
@CrossOrigin
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Tag(name = "Drones")
public class DroneRestController {

	private IDroneService droneService;

	@GetMapping("")
	@Operation(summary = "This method is used to fetch all Drones")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Drones successfully retrieved.",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = DroneDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "403", description = "", content = @Content)})
	public ResponseEntity<SuccessResponse<List<DroneDTO>>> getAllDrones() {

		return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(HttpStatus.OK.value(), "Drones successfully retrieved.", droneService.getAllDrones()));
	}

	@PostMapping("/register")
	@Operation(summary = "This method is used to register a Drone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Drone successfully registered.",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = DroneDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
	public ResponseEntity<SuccessResponse<DroneDTO>> registerDrone( @RequestBody @Validated DroneDTO droneDTO) throws DroneException, Exception {

		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(HttpStatus.CREATED.value(), "Drone successfully registered.",  droneService.registerDrone(droneDTO)));
	}

	@PostMapping("/{serialNumber}/load")
	@Operation(summary = "This method is used to load medications to given drone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully loaded medication for drone.",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = MedicationDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
	public ResponseEntity<SuccessResponse<String>> loadDroneWithMedication(@PathVariable String serialNumber, @RequestBody List<MedicationDTO> medicationDTOs) throws MedicationException, LimitExceedException, Exception {

		droneService.loadDroneWithMedications(serialNumber, medicationDTOs);
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(HttpStatus.CREATED.value(), "Loaded successfully", "Created"));
	}

	@GetMapping("/{serialNumber}/loaded-medications")
	@Operation(summary = "This method is used to get loaded medications in drone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Successfully fetched loaded medications.",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = DroneDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
	public ResponseEntity<SuccessResponse<List<MedicationDTO>>> getLoadedMedications(@PathVariable String serialNumber) {

		return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(HttpStatus.OK.value(), "Loaded Medications", droneService.getLoadedMedications(serialNumber)));
	}

	@GetMapping("/available-for-loading")
	@Operation(summary = "This method is used to get available drones for loading")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Available drone's for loading succefully fetched.",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = DroneDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
	public ResponseEntity<SuccessResponse<List<DroneDTO>>> getAvailableDronesForLoading() {

		return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(HttpStatus.OK.value(), "Available Drones For Loading", droneService.getAvailableDronesForLoading()));
	}

	@GetMapping("/{serialNumber}/battery-level")
	@Operation(summary = "This method is used to get battery level given drone")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Battery level fetched.",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = DroneDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
	public ResponseEntity<SuccessResponse<Double>> getDroneBatteryLevel(@PathVariable String serialNumber) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse<>(HttpStatus.OK.value(), "Battery Level for " + serialNumber , droneService.getDroneBatteryLevel(serialNumber)));
	}

	@PutMapping("/update-state")
	@Operation(summary = "This method is used to update drone state")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Drone state successfully update.",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = DroneStateRequest.class)) }),
			@ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
			@ApiResponse(responseCode = "404", description = "Resource Not Found", content = @Content)})
	public ResponseEntity<SuccessResponse<DroneStateResponse>> updateDroneState(@RequestBody @Validated DroneStateRequest droneStateRequest) {

		return ResponseEntity.status(HttpStatus.OK)
				.body(new SuccessResponse<>(HttpStatus.OK.value(), "Drone state updated : " + droneStateRequest.getSerialNo() , droneService.updateDroneState(droneStateRequest)));
	}
}
