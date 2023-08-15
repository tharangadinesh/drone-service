package com.musala.drone.service.drone.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.musala.drone.constant.DroneState;
import com.musala.drone.dto.drone.DroneDTO;
import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.exception.DroneException;
import com.musala.drone.exception.DroneNotFoundException;
import com.musala.drone.mapper.drone.DroneMapper;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.payload.request.DroneStateRequest;
import com.musala.drone.payload.response.DroneStateResponse;
import com.musala.drone.repository.drone.DroneRepository;
import com.musala.drone.service.drone.api.IDroneService;
import com.musala.drone.service.medication.api.IMedicationService;
import com.musala.drone.util.MessageResource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DroneServiceImpl implements IDroneService {

	private DroneRepository droneRepository;

	private IMedicationService medicationService;

	private MessageResource messageResource;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public DroneDTO registerDrone(DroneDTO droneDTO) throws Exception, DroneException {
		try {

			log.info(messageResource.getMessage("drone.log.info.register.started", new Object[]{droneDTO.getSerialNumber()} ));

			if (!validateFleetCount()) {
				throw new DroneException(messageResource.getMessage("drone.register.limit.exceed"));
			}

			Drone drone = new Drone();
			DroneMapper.getInstance().dtoToModel(drone, droneDTO);

			drone = droneRepository.saveAndFlush(drone);

			log.info(messageResource.getMessage("drone.log.info.register.completed", new Object[]{droneDTO.getSerialNumber()} ));

			return DroneMapper.getInstance().modelToDto(drone);

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}

	}

	public boolean validateFleetCount() {

		return droneRepository.count() < 10;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void loadDroneWithMedications(String droneSerialNumber, List<MedicationDTO> dtos) throws Exception {

		try {

			log.info(messageResource.getMessage("drone.log.info.load.drone.started"));

			Drone drone = droneRepository.findBySerialNumber(droneSerialNumber).orElseThrow(
					() -> new DroneException(messageResource.getMessage("drone.not.found", new Object[]{droneSerialNumber}))
					);

			updateDroneStateToLoading(drone);

			medicationService.loadMedicationsToDrone(drone, dtos);

			updateDroneStateToLoaded(drone);

			log.info(messageResource.getMessage("drone.log.info.load.drone.completed"));

		} catch (DroneException e){
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}

	}

	private void updateDroneStateToLoading(Drone drone) {
		drone.setState(DroneState.LOADING);
		droneRepository.save(drone);
		log.info(messageResource.getMessage("drone.log.info.load.drone.loading"));
	}

	private void updateDroneStateToLoaded(Drone drone) {
		drone.setState(DroneState.LOADED);
		droneRepository.save(drone);
		log.info(messageResource.getMessage("drone.log.info.load.drone.loaded"));
	}

	@Override
	public List<MedicationDTO> getLoadedMedications(String droneSerialNumber) {

		log.info(messageResource.getMessage("drone.log.info.loaded.medications"));
		return medicationService.findMedicationsByDroneSerialNo(droneSerialNumber);
	}

	@Override
	public List<DroneDTO> getAvailableDronesForLoading() {

		log.info(messageResource.getMessage("drone.log.info.available.drones"));
		return droneRepository.findAllAvailableDrone(DroneState.IDLE).stream()
				.map(x->DroneMapper.getInstance().modelToDto(x)).collect(Collectors.toList());
	}


	@Override
	public List<DroneDTO> getAllDrones() {

		log.info(messageResource.getMessage("drone.log.info.all.drones"));
		return droneRepository.findAll().stream()
				.map(x->DroneMapper.getInstance().modelToDto(x)).collect(Collectors.toList());
	}

	@Override
	public Double getDroneBatteryLevel(String droneSerialNumber) {

		log.info(messageResource.getMessage("drone.log.info.battery.level"));
		return droneRepository.findDroneBatteryLevelBySerialNo(droneSerialNumber);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public DroneStateResponse updateDroneState(DroneStateRequest droneStateRequest) {
		try {

			Object[] logArg = new Object[]{droneStateRequest.getSerialNo()};

			log.info(messageResource.getMessage("drone.log.info.drone.state.started", logArg ));

			Drone drone = droneRepository.findBySerialNumber(droneStateRequest.getSerialNo()).orElseThrow(
					() -> new DroneNotFoundException(messageResource.getMessage("drone.not.found", logArg))
					);

			drone.setBatteryCapacity(droneStateRequest.getBatteryLevel());
			drone.setState(droneStateRequest.getDroneState());

			drone = droneRepository.saveAndFlush(drone);

			log.info(messageResource.getMessage("drone.log.info.drone.state.completed", logArg ));

			return new DroneStateResponse(drone.getSerialNumber(), drone.getState());

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

}
