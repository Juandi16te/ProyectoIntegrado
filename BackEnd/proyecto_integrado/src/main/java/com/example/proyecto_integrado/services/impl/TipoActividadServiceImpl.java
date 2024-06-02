package com.example.proyecto_integrado.services.impl;

import com.example.proyecto_integrado.entity.TipoActividadEntity;
import com.example.proyecto_integrado.repository.TipoActividadRepository;
import com.example.proyecto_integrado.services.TipoActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoActividadServiceImpl implements TipoActividadService {
    @Autowired
    TipoActividadRepository tipoActividadRepository;

    @Override
    public List<TipoActividadEntity> getAll() {
        return tipoActividadRepository.findAll();
    }

    @Override
    public Optional<TipoActividadEntity> getById(Long id) {
        return tipoActividadRepository.findById(id);
    }

    @Override
    public void post(TipoActividadEntity tipoActividad) {
        if (tipoActividad != null) {
            tipoActividadRepository.save(tipoActividad);
        }
    }

    @Override
    public void put(TipoActividadEntity tipoActividad) {
        if (tipoActividad != null) {
            tipoActividadRepository.save(tipoActividad);
        }
    }

    @Override
    public void deleteById(Long id) {
        tipoActividadRepository.deleteById(id);
    }

    @Override
    public Boolean existsById(Long id) {
        return tipoActividadRepository.existsById(id);
    }
}
