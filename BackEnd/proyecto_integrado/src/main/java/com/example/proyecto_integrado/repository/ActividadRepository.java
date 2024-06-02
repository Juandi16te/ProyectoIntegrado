package com.example.proyecto_integrado.repository;

import com.example.proyecto_integrado.entity.ActividadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ActividadRepository extends JpaRepository<ActividadEntity, Long> {

    @Transactional(readOnly = true)
    public ActividadEntity getById(Long id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT a.* FROM actividad a ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    List<ActividadEntity> getAll();

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT a.* FROM actividad a WHERE a.id_ofertante = :id ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    List<ActividadEntity> findDistinctByIdOfertanteOrderByFechaInicio(@Param("id") Long id);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT a.* FROM actividad a WHERE a.id_tipo_actividad = :idTipoActividad ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    public List<ActividadEntity> findDistinctByIdTipoActividadIsOrderByFechaInicio(@Param("idTipoActividad") Long idTipoActividad);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT a.* FROM actividad a WHERE a.nombre LIKE %:nombre% ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    public List<ActividadEntity> findDistinctByNombreContainingOrderByFechaInicio(@Param("nombre") String nombre);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT a.* FROM actividad a WHERE a.id_tipo_actividad = :idTipoActividad AND a.id_ofertante = :idOfertante ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    public List<ActividadEntity> findDistinctByIdTipoActividadIsAndIdOfertanteIsOrderByFechaInicio(@Param("idTipoActividad") Long idTipoActividad, @Param("idOfertante") Long idOfertante);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT a.* FROM actividad a WHERE a.id_tipo_actividad = :idTipoActividad AND a.nombre LIKE %:nombre% ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    public List<ActividadEntity> findDistinctByIdTipoActividadIsAndNombreContainingOrderByFechaInicio(@Param("idTipoActividad") Long idTipoActividad, @Param("nombre") String nombre);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT a.* FROM actividad a WHERE a.id_ofertante = :idOfertante AND a.nombre LIKE %:nombre% ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    public List<ActividadEntity> findDistinctByIdOfertanteIsAndNombreContainingOrderByFechaInicio(@Param("idOfertante") Long idOfertante, @Param("nombre") String nombre);

    @Transactional(readOnly = true)
    @Query(value = "SELECT DISTINCT a.* FROM actividad a WHERE a.id_tipo_actividad = :idTipoActividad AND a.id_ofertante = :idOfertante AND a.nombre LIKE %:nombre% ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    public List<ActividadEntity> findDistinctByIdTipoActividadIsAndIdOfertanteIsAndNombreContainingOrderByFechaInicio(@Param("idTipoActividad") Long idTipoActividad, @Param("idOfertante") Long idOfertante, @Param("nombre") String nombre);

    @Transactional(readOnly = true)
    @Query(value = "SELECT a.* FROM actividad a WHERE a.id IN (" +
            "SELECT ac.id_actividad FROM actividad_consumidor ac WHERE ac.id_consumidor = :idConsumidor) ORDER BY " +
            "CASE " +
            "  WHEN a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR) THEN 0 " +
            "  ELSE 1 " +
            "END, " +
            "a.fecha_inicio ASC", nativeQuery = true)
    List<ActividadEntity> getByIdConsumidor(@Param("idConsumidor") Long idConsumidor);

    @Transactional(readOnly = true)
    @Query("SELECT AVG(a.valoracion) FROM ActividadEntity a WHERE a.idOfertante = :idOfertante AND a.valoracion IS NOT NULL")
    BigDecimal findAverageValoracionByIdOfertante(@Param("idOfertante") Long idOfertante);

    @Modifying
    @Transactional
    @Query("UPDATE ActividadEntity a SET a.idOfertante = NULL WHERE a.idOfertante = :idOfertante")
    void unassignOfertante(@Param("idOfertante") Long idOfertante);

    @Modifying
    @Transactional
    @Query(value = "UPDATE actividad a SET a.num_participantes = a.num_participantes - 1 WHERE a.id IN (" +
            "SELECT ac.id_actividad FROM actividad_consumidor ac WHERE ac.id_consumidor = :idConsumidor)" +
            " AND a.fecha_inicio >= DATE_ADD(CURRENT_TIMESTAMP(), INTERVAL 2 HOUR)", nativeQuery = true)
    void restNumParticipantesByIdConsumidorAndActividadNotFinished(@Param("idConsumidor") Long idConsumidor);
}