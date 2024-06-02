package com.example.proyecto_integrado.services.impl;

import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.repository.ActividadRepository;
import com.example.proyecto_integrado.repository.OfertanteRepository;
import com.example.proyecto_integrado.services.OfertanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfertanteServiceImpl implements OfertanteService {
    @Autowired
    OfertanteRepository ofertanteRepository;
    @Autowired
    ActividadRepository actividadRepository;

    @Override
    public List<OfertanteEntity> getAll() {
        return ofertanteRepository.findAll();
    }

    @Override
    public Optional<OfertanteEntity> getById(Long id) {
        return ofertanteRepository.findById(id);
    }

    @Override
    public Optional<OfertanteEntity> getByUsuario(String usuario) {
        return ofertanteRepository.findByUsuario(usuario);
    }

    @Override
    public void post(OfertanteEntity ofertante) {
        if (ofertante != null && !existsByUsuario(ofertante.getUsuario())) {
            ofertanteRepository.save(ofertante);
        }
    }

    @Override
    public void put(OfertanteEntity ofertante) {
        if (ofertante != null && ofertante.getUsuario() != null) {
            //se actualiza su valoración con la media de valoraciones que tengan sus actividades
            if (actividadRepository.findAverageValoracionByIdOfertante(ofertante.getId()) != null) {
                ofertante.setValoracion(actividadRepository.findAverageValoracionByIdOfertante(ofertante.getId()));
            }
            ofertanteRepository.save(ofertante);
        }
    }

    @Override
    public void deleteById(Long id) {
        ofertanteRepository.deleteById(id);
    }

    @Override
    public void deleteByUsuario(String usuario) {
        ofertanteRepository.deleteByUsuario(usuario);
    }

    @Override
    public Boolean existsByUsuario(String usuario) {
        return ofertanteRepository.existsByUsuario(usuario);
    }

    @Override
    public Boolean existsById(Long id) {
        return ofertanteRepository.existsById(id);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return ofertanteRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsuarioIsAndIdIsNot(String usuario, Long id) {
        return ofertanteRepository.existsByUsuarioIsAndIdIsNot(usuario, id);
    }
}
