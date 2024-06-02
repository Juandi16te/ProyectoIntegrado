package com.example.proyecto_integrado.services;

import com.example.proyecto_integrado.entity.ConsumidorEntity;

import java.util.List;
import java.util.Optional;

public interface ConsumidorService {
    public List<ConsumidorEntity> getAll();

    public Optional<ConsumidorEntity> getById(Long id);

    public Optional<ConsumidorEntity> getByUsuario(String usuario);

    public void post(ConsumidorEntity consumidor);

    public void put(ConsumidorEntity consumidor);

    public void deleteById(Long id);

    public void deleteByUsuario(String usuario);

    public Boolean existsByUsuario(String usuario);

    public Boolean existsById(Long id);

    public Boolean existsByEmail(String email);

    public Boolean existsByUsuarioIsAndIdIsNot(String usuario, Long id);
}
