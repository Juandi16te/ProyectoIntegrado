package com.example.proyecto_integrado.services.impl;

import com.example.proyecto_integrado.dto.ActividadConsumidorDTO;
import com.example.proyecto_integrado.entity.ActividadConsumidorEntity;
import com.example.proyecto_integrado.entity.ActividadEntity;
import com.example.proyecto_integrado.repository.ActividadConsumidorRepository;
import com.example.proyecto_integrado.repository.ActividadRepository;
import com.example.proyecto_integrado.services.ActividadConsumidorService;
import com.example.proyecto_integrado.services.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadConsumidorServiceImpl implements ActividadConsumidorService {
    @Autowired
    ActividadConsumidorRepository actividadConsumidorRepository;
    @Autowired
    ActividadRepository actividadRepository;


    @Override
    public List<ActividadConsumidorEntity> getAll() {
        return actividadConsumidorRepository.findAll();
    }

    @Override
    public List<ActividadConsumidorEntity> getByIdActividad(Long id) {
        return actividadConsumidorRepository.findByIdActividad(id);
    }

    @Override
    public List<ActividadConsumidorEntity> getByIdConsumidor(Long id) {
        return actividadConsumidorRepository.findByIdConsumidor(id);
    }

    @Override
    public Optional<ActividadConsumidorEntity> getByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor) {
        return actividadConsumidorRepository.findByIdActividadAndIdConsumidor(idActividad, idConsumidor);
    }

    @Override
    public void post(ActividadConsumidorEntity actividadConsumidor) {
        if (actividadConsumidor != null) {
            actividadConsumidorRepository.save(actividadConsumidor);
        }
    }

    @Override
    public void put(ActividadConsumidorEntity actividadConsumidor) {
        if (actividadConsumidor != null) {
            actividadConsumidorRepository.save(actividadConsumidor);
        }
    }

    @Override
    public void deleteByIdActividad(Long id) {
        actividadConsumidorRepository.deleteByIdActividad(id);
    }

    @Override
    public void deleteByIdConsumidor(Long id) {
        actividadConsumidorRepository.deleteByIdConsumidor(id);
    }

    @Override
    public void deleteByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor) {
        actividadConsumidorRepository.deleteByIdActividadAndIdConsumidor(idActividad, idConsumidor);
    }

    @Override
    public Boolean existsByIdActividad(Long id) {
        return actividadConsumidorRepository.existsByIdActividad(id);
    }

    @Override
    public Boolean existsByIdConsumidor(Long id) {
        return actividadConsumidorRepository.existsByIdConsumidor(id);
    }

    @Override
    public Boolean existsByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor) {
        return actividadConsumidorRepository.existsByIdActividadIsAndIdConsumidorIs(idActividad, idConsumidor);
    }

    @Override
    public void unassignConsumidor(Long idConsumidor) {
        actividadConsumidorRepository.unassignConsumidor(idConsumidor);
    }

}
