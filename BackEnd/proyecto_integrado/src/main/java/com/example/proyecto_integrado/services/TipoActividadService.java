package com.example.proyecto_integrado.services;

import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.entity.TipoActividadEntity;

import java.util.List;
import java.util.Optional;

public interface TipoActividadService {
    public List<TipoActividadEntity> getAll();

    public Optional<TipoActividadEntity> getById(Long id);

    public void post(TipoActividadEntity tipoActividad);

    public void put(TipoActividadEntity tipoActividad);

    public void deleteById(Long id);

    public Boolean existsById(Long id);
}
