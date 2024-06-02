package com.example.proyecto_integrado.mapper.impl;

import com.example.proyecto_integrado.dto.ActividadConsumidorDTO;
import com.example.proyecto_integrado.dto.ConsumidorDTO;
import com.example.proyecto_integrado.entity.ActividadConsumidorEntity;
import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActividadConsumidorMapperImpl implements Mapper<ActividadConsumidorEntity, ActividadConsumidorDTO> {
    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public ActividadConsumidorDTO mapTo(ActividadConsumidorEntity entity) {
        return modelMapper.map(entity, ActividadConsumidorDTO.class);
    }

    @Override
    public ActividadConsumidorEntity mapFrom(ActividadConsumidorDTO dto) {
        return modelMapper.map(dto, ActividadConsumidorEntity.class);
    }
}
