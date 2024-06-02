package com.example.proyecto_integrado.services;

import com.example.proyecto_integrado.dto.TipoActividadDTO;
import com.example.proyecto_integrado.entity.ActividadEntity;
import com.example.proyecto_integrado.entity.ConsumidorEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActividadService {
    public Boolean existsById(Long id);

    public List<ActividadEntity> getAll();

    public Optional<ActividadEntity> getById(Long id);

    public List<ActividadEntity> getByIdConsumidor(Long id);

    public List<ActividadEntity> getByCriteria(Long idTipoActividad, Long idOfertante, String nombre);

    public void post(ActividadEntity actividad);

    public void put(ActividadEntity actividad);

    public void unassignOfertante(Long idOfertante);

    public void deleteById(Long id);

    public void restNumParticipantesByIdConsumidorAndActividadNotFinished(Long idConsumidor);

}
