package com.musala.drone.mapper.drone;

import com.musala.drone.dto.drone.DroneDTO;
import com.musala.drone.mapper.GenericMapper;
import com.musala.drone.model.drone.Drone;

public class DroneMapper extends GenericMapper<DroneDTO, Drone> {

	private static DroneMapper instance = null;

	private DroneMapper() {  }

	public static DroneMapper getInstance() {
		if (instance == null) {
			instance = new DroneMapper();
		}
		return instance;
	}

	@Override
	public void dtoToModel(Drone model, DroneDTO dto) {

		model.setSerialNumber(dto.getSerialNumber());
		model.setModel(dto.getModel());
		model.setWeightLimit(dto.getWeightLimit());
		model.setBatteryCapacity(dto.getBatteryCapacity());
		model.setState(dto.getState());

		setCommonModelData(model, dto);
	}

	@Override
	public DroneDTO modelToDto(Drone model) {

		DroneDTO dto = new DroneDTO();

		dto.setSerialNumber(model.getSerialNumber());
		dto.setModel(model.getModel());
		dto.setWeightLimit(model.getWeightLimit());
		dto.setBatteryCapacity(model.getBatteryCapacity());
		dto.setState(model.getState());

		setCommonDTOData(dto, model);
		return dto;
	}

}
