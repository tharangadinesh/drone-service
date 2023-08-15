package com.musala.drone.service.drone.api;

import java.util.List;

import com.musala.drone.dto.drone.DroneDTO;
import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.exception.DroneException;
import com.musala.drone.payload.request.DroneStateRequest;
import com.musala.drone.payload.response.DroneStateResponse;

public interface IDroneService {

	public DroneDTO registerDrone(DroneDTO drone) throws Exception, DroneException;

	public void loadDroneWithMedications(String droneSerialNumber, List<MedicationDTO> medicationDTOs) throws Exception;

	public List<MedicationDTO> getLoadedMedications(String droneSerialNumber);

	public List<DroneDTO> getAvailableDronesForLoading();

	public List<DroneDTO> getAllDrones();

	public Double getDroneBatteryLevel(String droneSerialNumber);

	public DroneStateResponse updateDroneState(DroneStateRequest droneStateRequest);

}
