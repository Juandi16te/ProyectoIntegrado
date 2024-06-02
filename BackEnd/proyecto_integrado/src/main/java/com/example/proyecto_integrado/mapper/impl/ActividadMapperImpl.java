package com.example.proyecto_integrado.mapper.impl;

import com.example.proyecto_integrado.dto.ActividadDTO;
import com.example.proyecto_integrado.dto.TipoActividadDTO;
import com.example.proyecto_integrado.entity.ActividadEntity;
import com.example.proyecto_integrado.entity.TipoActividadEntity;
import com.example.proyecto_integrado.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActividadMapperImpl implements Mapper<ActividadEntity, ActividadDTO> {
    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public ActividadDTO mapTo(ActividadEntity entity) {
        return modelMapper.map(entity, ActividadDTO.class);
    }

    @Override
    public ActividadEntity mapFrom(ActividadDTO dto) {
        return modelMapper.map(dto, ActividadEntity.class);
    }
}
