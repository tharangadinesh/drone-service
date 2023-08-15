package com.musala.drone.mapper.medication;

import com.musala.drone.dto.medication.MedicationDTO;
import com.musala.drone.mapper.GenericMapper;
import com.musala.drone.model.medication.Medication;

public class MedicationMapper extends GenericMapper<MedicationDTO, Medication> {

	private static MedicationMapper instance = null;

	private MedicationMapper() {  }

	public static MedicationMapper getInstance() {
		if (instance == null) {
			instance = new MedicationMapper();
		}
		return instance;
	}

	@Override
	public void dtoToModel(Medication model, MedicationDTO dto) {

		model.setName(dto.getName());
		model.setCode(dto.getCode());
		model.setImage(dto.getImage());
		model.setWeight(dto.getWeight());

		setCommonModelData(model, dto);
	}

	@Override
	public MedicationDTO modelToDto(Medication model) {

		MedicationDTO dto = new MedicationDTO();

		dto.setName(model.getName());
		dto.setCode(model.getCode());
		dto.setImage(model.getImage());
		dto.setWeight(model.getWeight());

		if (model.getDrone() != null) {
			dto.setDroneSerialNo(model.getDrone().getSerialNumber());
		}

		setCommonDTOData(dto, model);
		return dto;
	}

}
