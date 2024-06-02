package com.example.proyecto_integrado.services;

import com.example.proyecto_integrado.entity.OfertanteEntity;

import java.util.List;
import java.util.Optional;

public interface OfertanteService {
    public List<OfertanteEntity> getAll();

    public Optional<OfertanteEntity> getById(Long id);

    public Optional<OfertanteEntity> getByUsuario(String usuario);

    public void post(OfertanteEntity consumidor);

    public void put(OfertanteEntity consumidor);

    public void deleteById(Long id);

    public void deleteByUsuario(String usuario);

    public Boolean existsByUsuario(String usuario);

    public Boolean existsById(Long id);

    public Boolean existsByEmail(String email);

    public Boolean existsByUsuarioIsAndIdIsNot(String usuario, Long id);


}
