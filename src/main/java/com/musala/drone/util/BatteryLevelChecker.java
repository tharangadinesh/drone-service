package com.musala.drone.util;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.musala.drone.dto.drone.DroneDTO;
import com.musala.drone.service.drone.api.IDroneService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BatteryLevelChecker {

	@Autowired
	private IDroneService droneService;

	@Autowired
	private MessageResource messageResource;

	public void startCheckingBatteryLevels() {

		log.info(messageResource.getMessage("battery.level.checker.started"));
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

		// Schedule the battery level check task to run every 30 minutes
		executor.scheduleAtFixedRate(this::checkBatteryLevelsAndLog, 0, 30, TimeUnit.MINUTES);
	}

	private void checkBatteryLevelsAndLog() {
		log.info(messageResource.getMessage("battery.level.checker.log"));
		List<DroneDTO> drones = droneService.getAllDrones();
		for (DroneDTO droneDTO : drones) {
			logBatteryLevel(droneDTO.getSerialNumber(), droneDTO.getBatteryCapacity());
		}
	}

	private void logBatteryLevel(String droneSerialNumber, double batteryLevel) {
		String logMessage = "Drone " + droneSerialNumber + " - Battery Level: " + batteryLevel + "%";
		log.info(logMessage);
	}
}
