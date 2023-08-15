package com.musala.drone.service.drone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.musala.drone.constant.DroneState;
import com.musala.drone.dto.drone.DroneDTO;
import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.exception.DroneException;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.repository.drone.DroneRepository;
import com.musala.drone.service.drone.impl.DroneServiceImpl;
import com.musala.drone.service.medication.api.IMedicationService;
import com.musala.drone.util.MessageResource;

@SpringBootTest
class DroneServiceTest {

	@Mock
	private DroneRepository droneRepository;

	@Mock
	private IMedicationService medicationService;

	@Mock
	private MessageResource messageResource;

	@InjectMocks
	private DroneServiceImpl droneService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegisterDrone_Success() {
		// Given
		DroneDTO droneDTO = new DroneDTO();
		droneDTO.setSerialNumber("DRN-00001");

		when(droneRepository.count()).thenReturn(2L);

		try {
			// When
			DroneDTO result = droneService.registerDrone(droneDTO);

			// Then
			assertNotNull(result);
			assertEquals("DRN-00001", result.getSerialNumber());
			verify(droneRepository, times(1)).saveAndFlush(any(Drone.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void testRegisterDrone_FleetLimitExceeded() {
		// Given
		DroneDTO droneDTO = new DroneDTO();
		droneDTO.setSerialNumber("DRN-00001");

		when(droneRepository.count()).thenReturn(11L).thenThrow(new DroneException("Some error occurred."));

		// When, Then
		assertThrows(DroneException.class, () -> droneService.registerDrone(droneDTO));
	}

	@Test
	void testRegisterDrone_Exception() {
		// Given
		DroneDTO droneDTO = new DroneDTO();
		droneDTO.setSerialNumber("DRN-00001");

		when(droneRepository.count()).thenReturn(11L);
		when(droneRepository.saveAndFlush(any(Drone.class))).thenThrow(new DroneException("Some error occurred."));

		// When, Then
		assertThrows(DroneException.class, () -> droneService.registerDrone(droneDTO));
	}

	@Test
	void testLoadDroneWithMedications_Success() throws Exception {
		// Given
		String droneSerialNumber = "DRN-00001";
		List<MedicationDTO> dtos = new ArrayList<>();

		Optional<Drone> drone = Optional.of(new Drone());
		when(droneRepository.findBySerialNumber(droneSerialNumber)).thenReturn(drone);

		// When
		droneService.loadDroneWithMedications(droneSerialNumber, dtos);

		// Then
		verify(droneRepository, times(2)).save(drone.get());
	}

	@Test
	void testAvailableDronesForLoading_Success() throws Exception {
		// Given
		List<Drone> dtos = new ArrayList<>();

		Drone drone1 = new Drone();
		drone1.setSerialNumber("DRN-00001");
		drone1.setState(DroneState.IDLE);

		Drone drone2 = new Drone();
		drone2.setSerialNumber("DRN-00001");
		drone2.setState(DroneState.IDLE);

		dtos.add(drone1);
		dtos.add(drone2);

		when(droneRepository.findAllAvailableDrone(DroneState.IDLE)).thenReturn(dtos);

		assertEquals( droneService.getAvailableDronesForLoading().size(), 2);

	}
}
