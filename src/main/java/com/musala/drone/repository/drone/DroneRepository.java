package com.musala.drone.repository.drone;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.musala.drone.constant.DroneState;
import com.musala.drone.model.drone.Drone;

@Repository
public interface DroneRepository extends JpaRepository<Drone,Integer> {

	@Query("FROM Drone WHERE state = :state")
	List<Drone> findAllAvailableDrone(@Param("state") DroneState state);

	@Query("SELECT batteryCapacity FROM Drone WHERE serialNumber = :droneSerialNumber")
	Double findDroneBatteryLevelBySerialNo(@Param("droneSerialNumber")String droneSerialNumber);

	Optional<Drone> findBySerialNumber(String droneSerialNumber);
}
