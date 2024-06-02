package com.example.proyecto_integrado.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@jakarta.persistence.Table(name = "actividad_consumidor", schema = "proyecto_integrado", catalog = "")
@IdClass(com.example.proyecto_integrado.entity.ActividadConsumidorEntityPK.class)
public class ActividadConsumidorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "id_actividad")
    private Long idActividad;

    public Long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Long idActividad) {
        this.idActividad = idActividad;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @jakarta.persistence.Column(name = "id_consumidor")
    private Long idConsumidor;

    public Long getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(Long idConsumidor) {
        this.idConsumidor = idConsumidor;
    }

    @Basic
    @Column(name = "valoracion")
    private BigDecimal valoracion;

    public BigDecimal getValoracion() {
        return valoracion;
    }

    public void setValoracion(BigDecimal valoracion) {
        this.valoracion = valoracion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActividadConsumidorEntity that = (ActividadConsumidorEntity) o;

        if (idActividad != that.idActividad) return false;
        if (idConsumidor != that.idConsumidor) return false;
        if (valoracion != null ? !valoracion.equals(that.valoracion) : that.valoracion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Math.toIntExact(idActividad);
        result = (int) (31 * result + idConsumidor);
        result = 31 * result + (valoracion != null ? valoracion.hashCode() : 0);
        return result;
    }
}
