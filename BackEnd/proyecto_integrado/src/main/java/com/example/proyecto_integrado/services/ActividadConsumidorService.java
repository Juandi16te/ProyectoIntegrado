package com.example.proyecto_integrado.services;

import com.example.proyecto_integrado.dto.ActividadConsumidorDTO;
import com.example.proyecto_integrado.entity.ActividadConsumidorEntity;
import com.example.proyecto_integrado.entity.ConsumidorEntity;

import java.util.List;
import java.util.Optional;

public interface ActividadConsumidorService {
    public List<ActividadConsumidorEntity> getAll();

    public List<ActividadConsumidorEntity> getByIdActividad(Long id);

    public List<ActividadConsumidorEntity> getByIdConsumidor(Long id);

    public Optional<ActividadConsumidorEntity> getByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor);

    public void post(ActividadConsumidorEntity actividadConsumidor);

    public void put(ActividadConsumidorEntity actividadConsumidor);

    public void deleteByIdActividad(Long id);

    public void deleteByIdConsumidor(Long id);

    public void deleteByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor);

    public Boolean existsByIdActividad(Long id);

    public Boolean existsByIdConsumidor(Long id);

    public Boolean existsByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor);

    public void unassignConsumidor(Long idConsumidor);
}
