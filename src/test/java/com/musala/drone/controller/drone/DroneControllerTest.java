package com.musala.drone.controller.drone;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.musala.drone.constant.DroneModel;
import com.musala.drone.constant.DroneState;
import com.musala.drone.dto.drone.DroneDTO;
import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.exception.DroneException;
import com.musala.drone.exception.MedicationException;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.payload.request.DroneStateRequest;
import com.musala.drone.payload.response.DroneStateResponse;
import com.musala.drone.repository.drone.DroneRepository;
import com.musala.drone.repository.medication.MedicationRepository;
import com.musala.drone.service.drone.api.IDroneService;

@WebMvcTest(DroneRestController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@WithMockUser
class DroneControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IDroneService droneService;

	@MockBean
	private DroneRepository droneRepository;

	@MockBean
	private MedicationRepository medicationRepository;

	@InjectMocks
	private DroneRestController droneController;

	DroneDTO testDroneDTO = null;
	Drone testDrone = null;
	MedicationDTO testMedicationDTO = null;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		testDroneDTO = new DroneDTO("DRN-00001", 400d, 75d, DroneModel.HEAVY_WEIGHT, DroneState.IDLE);
		testMedicationDTO = new MedicationDTO("Medicine A", "MED-A", "base64_encoded_image", 0.1, "DRN-00001");
	}

	@Test
	@Order(1)
	void testGetAllDrones() throws Exception {

		List<DroneDTO> drones = new ArrayList<>();
		drones.add(testDroneDTO);

		when(droneService.getAllDrones()).thenReturn(drones);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/drones"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Drones successfully retrieved."))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].serialNumber").value("DRN-00001"));

	}

	@Test
	@Order(2)
	void testRegisterDrone_Success() throws Exception {

		when(droneService.registerDrone(testDroneDTO)).thenReturn(testDroneDTO);

		String requestBody = "{ \"serialNumber\": \"DRN-00001\", \"weightLimit\": 400, \"batteryCapacity\": 75, \"model\": \"HEAVY_WEIGHT\", \"state\": \"IDLE\" }";

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/drones/register")
				.content(requestBody)
				.contentType("application/json"))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Drone successfully registered."))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data.serialNumber").value("DRN-00001"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data.weightLimit").value(400d))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data.batteryCapacity").value(75d))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data.model").value("HEAVY_WEIGHT"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data.state").value("IDLE"));

		verify(droneService, times(1)).registerDrone(any(DroneDTO.class));
	}

	@Test
	void testRegisterDrone_DroneException() throws Exception {

		doThrow(new DroneException("Error occured")).when(droneService).registerDrone(any(DroneDTO.class));

		String requestBody = "{ \"serialNo\": \"DRN-00001\", \"weight\": 400, \"batteryCapacity\": 75, \"model\": \"HEAVY_WEIGHT\", \"state\": \"IDLE\" }";

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/drones/register")
				.content(requestBody)
				.contentType("application/json"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error occured"));

		verify(droneService, times(1)).registerDrone(any(DroneDTO.class));
	}

	@Test
	@Order(3)
	void testLoadDroneWithMedication_Success() throws Exception {
		//		List<MedicationDTO> medicationDTOs = new ArrayList<>();
		//		medicationDTOs.add(testMedicationDTO);
		//
		//		doNothing().when(droneService.loadDroneWithMedications("DRN-00001", medicationDTOs));

		String requestBody = "[{ \"name\": \"Medicine A\", \"weight\": 0.1, \"code\": \"MED-A\", \"image\": \"base64_encoded_image\", \"serialNumber\" : \"DRN-00001\" }]";

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/drones/DRN-00001/load")
				.content(requestBody)
				.contentType("application/json"))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Loaded successfully"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").value("Created"));

		verify(droneService, times(1)).loadDroneWithMedications(eq("DRN-00001"), anyList());
	}

	@Test
	void testGetLoadedMedications_Success() throws Exception {
		List<MedicationDTO> loadedMedications = new ArrayList<>();
		loadedMedications.add(testMedicationDTO);

		when(droneService.getLoadedMedications("DRN-00001")).thenReturn(loadedMedications);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/drones/DRN-00001/loaded-medications"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Loaded Medications"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Medicine A"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].weight").value(0.1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].code").value("MED-A"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].image").value("base64_encoded_image"));

		verify(droneService, times(1)).getLoadedMedications("DRN-00001");
	}

	@Test
	void testLoadDroneWithMedication_MedicationException() throws Exception {
		doThrow(new MedicationException("Error occured.")).when(droneService).loadDroneWithMedications(anyString(), anyList());

		String requestBody = "[{ \"name\": \"Medicine A\", \"weight\": 0.1, \"code\": \"MED-A\", \"image\": \"base64_encoded_image\" }]";

		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/drones/DRN-00001/load")
				.content(requestBody)
				.contentType("application/json"))
		.andExpect(MockMvcResultMatchers.status().isInternalServerError())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Error occured."));

		verify(droneService, times(1)).loadDroneWithMedications(eq("DRN-00001"), anyList());
	}

	@Test
	void testGetAvailableDronesForLoading_Success() throws Exception {
		List<DroneDTO> availableDrones = new ArrayList<>();
		availableDrones.add(testDroneDTO);

		when(droneService.getAvailableDronesForLoading()).thenReturn(availableDrones);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/drones/available-for-loading"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Available Drones For Loading"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].serialNumber").value("DRN-00001"));

		verify(droneService, times(1)).getAvailableDronesForLoading();
	}
	@Test
	void testGetDroneBatteryLevel_Success() throws Exception {

		when(droneService.getDroneBatteryLevel("DRN-00001")).thenReturn(75d);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/drones/DRN-00001/battery-level"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Battery Level for DRN-00001"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").value(75));

		verify(droneService, times(1)).getDroneBatteryLevel("DRN-00001");
	}

	@Test
	void testUpdateDroneState_Success() throws Exception {

		DroneStateResponse stateResponse = new DroneStateResponse("DRN-00001", DroneState.LOADED);

		when(droneService.updateDroneState(any(DroneStateRequest.class))).thenReturn(stateResponse);

		String requestBody = "{ \"serialNo\": \"DRN-00001\", \"droneState\": \"LOADED\" }";

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/drones/update-state")
				.content(requestBody)
				.contentType("application/json"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Drone state updated : DRN-00001"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.data.serialNo").value("DRN-00001"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.data.droneState").value("LOADED"));

		verify(droneService, times(1)).updateDroneState(any(DroneStateRequest.class));
	}

	@Test
	void testUpdateDroneState_InvalidData() throws Exception {

		String requestBody = "{ \"serialNo\": \"DRN-00001\", \"droneState\": \"LOADEDS\" }";

		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/drones/update-state")
				.content(requestBody)
				.contentType("application/json"))
		.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

}
