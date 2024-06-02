package com.example.proyecto_integrado.mapper.impl;

import com.example.proyecto_integrado.dto.ConsumidorDTO;
import com.example.proyecto_integrado.dto.TipoActividadDTO;
import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.entity.TipoActividadEntity;
import com.example.proyecto_integrado.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TipoActividadMapperImpl implements Mapper<TipoActividadEntity, TipoActividadDTO> {
    @Autowired
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public TipoActividadDTO mapTo(TipoActividadEntity entity) {
        return modelMapper.map(entity, TipoActividadDTO.class);
    }

    @Override
    public TipoActividadEntity mapFrom(TipoActividadDTO dto) {
        return modelMapper.map(dto, TipoActividadEntity.class);
    }
}
