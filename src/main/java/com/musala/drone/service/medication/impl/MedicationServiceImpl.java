package com.musala.drone.service.medication.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.exception.LimitExceedException;
import com.musala.drone.exception.MedicationException;
import com.musala.drone.mapper.medication.MedicationMapper;
import com.musala.drone.model.drone.Drone;
import com.musala.drone.model.medication.Medication;
import com.musala.drone.repository.medication.MedicationRepository;
import com.musala.drone.service.medication.api.IMedicationService;
import com.musala.drone.util.MessageResource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MedicationServiceImpl implements IMedicationService {

	private MedicationRepository medicationRepository;

	private MessageResource messageResource;

	@Override
	public List<MedicationDTO> findMedicationsByDroneSerialNo(String droneSerialNumber) {

		log.info(messageResource.getMessage("medication.log.info.by.serialno") );
		return medicationRepository.findMedicationsByDroneSerialNo(droneSerialNumber).stream()
				.map( x ->  MedicationMapper.getInstance().modelToDto(x)).collect(Collectors.toList());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void loadMedicationsToDrone(Drone drone, List<MedicationDTO> dtos) throws Exception, MedicationException, LimitExceedException  {

		log.info(messageResource.getMessage("medication.log.info.to.drone.started") );

		validateMaximumWeightCapacity(drone, dtos);

		validateBatteryLevel(drone);

		Set<Medication> medications = new HashSet<>();

		createMedications(dtos, drone, medications);

		medicationRepository.saveAll(medications);

		log.info(messageResource.getMessage("medication.log.info.to.drone.completed") );

	}

	private void validateBatteryLevel(Drone drone) throws MedicationException {

		log.info(messageResource.getMessage("medication.log.info.validate.battery.level") );
		if (drone.getBatteryCapacity() < 25) {
			throw new MedicationException( messageResource.getMessage("medication.error.low.battery") );
		}
	}

	private void validateMaximumWeightCapacity(Drone drone, List<MedicationDTO> dtos) throws LimitExceedException {
		log.info(messageResource.getMessage("medication.log.info.validate.maximum.weightcapacity") );
		Double totalWeight = dtos.stream().filter(m -> m.getWeight() != null).mapToDouble(MedicationDTO::getWeight).sum();
		if (totalWeight > drone.getWeightLimit() ) {
			throw new LimitExceedException( messageResource.getMessage("medication.error.exceed.capacity", new Object[] {drone.getWeightLimit(), totalWeight})) ;
		}
	}

	private void createMedications(List<MedicationDTO> dtos, Drone drone, Set<Medication> medications) {
		if ( dtos != null && !dtos.isEmpty()) {
			for (MedicationDTO dto : dtos) {

				Medication medication = new Medication();
				medication.setDrone(drone);
				MedicationMapper.getInstance().dtoToModel(medication, dto);
				medications.add(medication);
			}
		}
	}

}
