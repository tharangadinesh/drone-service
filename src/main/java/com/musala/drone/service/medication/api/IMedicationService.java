package com.musala.drone.service.medication.api;

import java.util.List;

import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.exception.LimitExceedException;
import com.musala.drone.exception.MedicationException;
import com.musala.drone.model.drone.Drone;

public interface IMedicationService {

	List<MedicationDTO> findMedicationsByDroneSerialNo(String droneSerialNumber);

	void loadMedicationsToDrone(Drone drone, List<MedicationDTO> dtos) throws Exception, MedicationException, LimitExceedException ;

}
