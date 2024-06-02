package com.example.proyecto_integrado.services.impl;

import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.repository.ConsumidorRepository;
import com.example.proyecto_integrado.services.ConsumidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsumidorServiceImpl implements ConsumidorService {
    @Autowired
    ConsumidorRepository consumidorRepository;


    @Override
    public List<ConsumidorEntity> getAll() {
        return consumidorRepository.findAll();
    }

    @Override
    public Optional<ConsumidorEntity> getById(Long id) {
        return consumidorRepository.findById(id);
    }

    @Override
    public Optional<ConsumidorEntity> getByUsuario(String usuario) {
        return consumidorRepository.findByUsuario(usuario);
    }

    @Override
    public void post(ConsumidorEntity consumidor) {
        if (consumidor != null && !existsByUsuario(consumidor.getUsuario())) {
            consumidorRepository.save(consumidor);
        }
    }

    @Override
    public void put(ConsumidorEntity consumidor) {
        if (consumidor != null && consumidor.getUsuario() != null) {
            consumidorRepository.save(consumidor);
        }
    }

    @Override
    public void deleteById(Long id) {
        consumidorRepository.deleteById(id);
    }

    @Override
    public void deleteByUsuario(String usuario) {
        consumidorRepository.deleteByUsuario(usuario);
    }

    @Override
    public Boolean existsByUsuario(String usuario) {
        return consumidorRepository.existsByUsuario(usuario);
    }

    @Override
    public Boolean existsById(Long id) {
        return consumidorRepository.existsById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return consumidorRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsuarioIsAndIdIsNot(String usuario, Long id) {
        return consumidorRepository.existsByUsuarioIsAndIdIsNot(usuario, id);
    }
}
