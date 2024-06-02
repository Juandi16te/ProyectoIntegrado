package com.example.proyecto_integrado.repository;

import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumidorRepository extends JpaRepository<ConsumidorEntity, Long> {
    public Optional<ConsumidorEntity> findByUsuario(String usuario);

    public boolean existsByUsuario(String usuario);

    public boolean existsByEmail(String email);

    public void deleteByUsuario(String usuario);

    @Modifying
    @Transactional
    @Query("DELETE FROM ConsumidorEntity c WHERE c.id = :id")
    public void deleteById(Long id);

    public boolean existsByUsuarioIsAndIdIsNot(String usuario, Long id);
}