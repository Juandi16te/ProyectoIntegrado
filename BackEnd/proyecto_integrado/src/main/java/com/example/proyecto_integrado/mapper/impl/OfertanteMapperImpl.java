package com.example.proyecto_integrado.mapper.impl;

import com.example.proyecto_integrado.dto.OfertanteDTO;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfertanteMapperImpl implements Mapper<OfertanteEntity, OfertanteDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OfertanteDTO mapTo(OfertanteEntity entity) {
        return modelMapper.map(entity, OfertanteDTO.class);
    }

    @Override
    public OfertanteEntity mapFrom(OfertanteDTO dto) {
        return modelMapper.map(dto, OfertanteEntity.class);
    }
}
