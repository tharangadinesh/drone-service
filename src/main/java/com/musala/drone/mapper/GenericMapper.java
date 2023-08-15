package com.musala.drone.mapper;

import java.util.ArrayList;
import java.util.List;

import com.musala.drone.dto.BaseDTO;
import com.musala.drone.model.BaseModel;

public abstract class GenericMapper <DTO extends BaseDTO,MODEL extends BaseModel>{

	public abstract void dtoToModel(MODEL model,DTO dto);
	public abstract DTO modelToDto(MODEL model);

	public List<DTO> modelToList(final Iterable<? extends MODEL> modelList)  {
		if(modelList == null) {
			return new ArrayList<>();
		}
		final List<DTO> dtoList = new ArrayList<>();

		for(final MODEL model : modelList){
			dtoList.add(modelToDto(model));
		}
		return dtoList;
	}

	protected void setCommonDTOData(DTO dto, MODEL model){
		dto.setVersion(model.getVersion());
		dto.setId(model.getId());
	}

	protected void setCommonModelData(MODEL model,DTO dto){
		model.setVersion(dto.getVersion());
		if (dto.getId() != null) {
			model.setId(dto.getId());
		}
	}
}

