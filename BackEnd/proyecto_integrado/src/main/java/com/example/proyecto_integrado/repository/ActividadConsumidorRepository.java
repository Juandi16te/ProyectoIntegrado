package com.example.proyecto_integrado.repository;

import com.example.proyecto_integrado.entity.ActividadConsumidorEntity;
import com.example.proyecto_integrado.entity.ConsumidorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActividadConsumidorRepository extends JpaRepository<ActividadConsumidorEntity, Long> {
    public boolean existsByIdActividadIsAndIdConsumidorIs(Long idActividad, Long idConsumidor);

    public boolean existsByIdActividad(Long idActividad);

    public boolean existsByIdConsumidor(Long idConsumidor);

    public Optional<ActividadConsumidorEntity> findByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor);

    public List<ActividadConsumidorEntity> findByIdActividad(Long idActividad);

    public List<ActividadConsumidorEntity> findByIdConsumidor(Long idConsumidor);

    @Modifying
    @Transactional
    @Query("DELETE FROM ActividadConsumidorEntity ac WHERE ac.idConsumidor = :idConsumidor")
    public void deleteByIdConsumidor(Long idConsumidor);

    public void deleteByIdActividad(Long idActividad);

    @Modifying
    @Transactional
    @Query("DELETE FROM ActividadConsumidorEntity ac WHERE ac.idActividad = :idActividad AND ac.idConsumidor = :idConsumidor")
    public void deleteByIdActividadAndIdConsumidor(Long idActividad, Long idConsumidor);

    @Transactional
    @Query("SELECT AVG(a.valoracion) FROM ActividadConsumidorEntity a WHERE a.idActividad = :idActividad AND a.valoracion IS NOT NULL ")
    BigDecimal findAverageValoracionByIdActividad(@Param("idActividad") Long idActividad);

    @Modifying
    @Transactional
    @Query("UPDATE ActividadConsumidorEntity a SET a.idConsumidor = NULL WHERE a.idConsumidor = :idConsumidor")
    void unassignConsumidor(@Param("idConsumidor") Long idConsumidor);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM actividad_consumidor " +
            "WHERE id_actividad IN ( SELECT id FROM actividad " +
            "WHERE fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR)) " +
            "AND id_consumidor = :idConsumidor"
            , nativeQuery = true)
    void deleteByIdConsumidorAndActividadNotFinished(@Param("idConsumidor") Long idConsumidor);

}