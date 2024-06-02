package com.example.proyecto_integrado.repository;

import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfertanteRepository extends JpaRepository<OfertanteEntity, Long> {
    public Optional<OfertanteEntity> findByUsuario(String usuario);

    public boolean existsByUsuario(String usuario);

    public boolean existsByEmail(String email);

    public void deleteByUsuario(String usuario);

    public boolean existsByUsuarioIsAndIdIsNot(String usuario, Long id);
}