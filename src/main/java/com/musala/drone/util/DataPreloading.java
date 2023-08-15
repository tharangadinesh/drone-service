package com.musala.drone.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.musala.drone.constant.DroneModel;
import com.musala.drone.constant.DroneState;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.model.medication.Medication;
import com.musala.drone.repository.drone.DroneRepository;
import com.musala.drone.repository.medication.MedicationRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DataPreloading implements CommandLineRunner {

	private final DroneRepository droneRepository;
	//	private final LoadRepository loadRepository;
	private final MedicationRepository medicationRepository;

	@Override
	public void run(String... args) throws Exception {
		preloadDummyData();
	}

	private void preloadDummyData() {
		// Preload dummy Drone data
		Drone drone1 = new Drone();
		drone1.setSerialNumber("DRN-00001");
		drone1.setModel(DroneModel.LIGHT_WEIGHT);
		drone1.setWeightLimit(300.0);
		drone1.setBatteryCapacity(80.0);
		drone1.setState(DroneState.IDLE);
		droneRepository.saveAndFlush(drone1);

		Drone drone2 = new Drone();
		drone2.setSerialNumber("DRN-00002");
		drone2.setModel(DroneModel.MIDDLE_WEIGHT);
		drone2.setWeightLimit(200.0);
		drone2.setBatteryCapacity(90.0);
		drone2.setState(DroneState.IDLE);
		droneRepository.saveAndFlush(drone2);

		// Preload dummy Medication data


		//		Load load = new Load();
		//		load.setDrone(drone1);

		Set<Medication> medications = new HashSet<>();

		Medication medication1 = new Medication();
		medication1.setName("Medicine A");
		medication1.setWeight(50.0);
		medication1.setCode("MED-A");
		medication1.setImage("base64_encoded_image_data_here");
		//		medication1.setLoad(load);
		medication1.setDrone(drone1);
		medications.add(medication1);

		Medication medication2 = new Medication();
		medication2.setName("Medicine B");
		medication2.setWeight(70.0);
		medication2.setCode("MED-B");
		medication2.setImage("base64_encoded_image_data_here");
		medication2.setDrone(drone1);
		//		medication2.setLoad(load);
		medications.add(medication2);

		//		load.setMedications(medications);

		medicationRepository.saveAllAndFlush(medications);

		drone1.setState(DroneState.LOADED);
		droneRepository.saveAndFlush(drone1);


	}
}
