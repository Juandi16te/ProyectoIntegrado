package com.example.proyecto_integrado.mapper.impl;

import com.example.proyecto_integrado.dto.ConsumidorDTO;
import com.example.proyecto_integrado.dto.OfertanteDTO;
import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumidorMapperImpl implements Mapper<ConsumidorEntity, ConsumidorDTO> {
    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public ConsumidorDTO mapTo(ConsumidorEntity entity) {
        return modelMapper.map(entity, ConsumidorDTO.class);
    }

    @Override
    public ConsumidorEntity mapFrom(ConsumidorDTO dto) {
        return modelMapper.map(dto, ConsumidorEntity.class);
    }
}
