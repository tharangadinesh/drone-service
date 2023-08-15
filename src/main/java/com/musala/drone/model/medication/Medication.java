package com.musala.drone.model.medication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.musala.drone.model.BaseModel;
import com.musala.drone.model.drone.Drone;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Medication extends BaseModel {

	private static final long serialVersionUID = 5599541231161242935L;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String code;

	private String image;

	private Double weight;

	//	@JoinColumn(name = "load_id")
	//	@ManyToOne(targetEntity = Load.class, fetch = FetchType.LAZY)
	//	private Load load;

	@JoinColumn(name = "drone_id")
	@ManyToOne(targetEntity = Drone.class, fetch = FetchType.LAZY)
	private Drone drone;

}
