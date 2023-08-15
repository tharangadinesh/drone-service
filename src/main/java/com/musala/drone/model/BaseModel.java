package com.musala.drone.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseModel implements Serializable {

	private static final long serialVersionUID = -5356423279919704668L;

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	protected Date createdDate;

	@Column(nullable = true)
	protected Date updatedDate;

	@Version
	protected Integer version;

	@PrePersist
	public void prePersist() {
		createdDate = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		updatedDate = new Date();
	}
}
