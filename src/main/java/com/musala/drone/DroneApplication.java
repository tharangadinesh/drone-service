package com.musala.drone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.musala.drone.util.BatteryLevelChecker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
@ComponentScan(basePackages = {	"com.musala.drone.config", "com.musala.drone.security", "com.musala.drone.util", "com.musala.drone.controller", "com.musala.drone.service", "com.musala.drone.aop"})
public class DroneApplication {

	@Autowired
	public DroneApplication(BatteryLevelChecker batteryLevelChecker) {
		batteryLevelChecker.startCheckingBatteryLevels();
	}

	public static void main(String[] args) {
		SpringApplication.run(DroneApplication.class, args);
	}

}
