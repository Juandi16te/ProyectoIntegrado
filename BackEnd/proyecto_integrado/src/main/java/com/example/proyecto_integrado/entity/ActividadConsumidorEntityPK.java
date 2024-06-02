package com.example.proyecto_integrado.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

public class ActividadConsumidorEntityPK implements Serializable {
    @Column(name = "id_actividad")
    @Id
    private Long idActividad;
    @Column(name = "id_consumidor")
    @Id
    private Long idConsumidor;

    public Long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(Long idActividad) {
        this.idActividad = idActividad;
    }

    public Long getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(Long idConsumidor) {
        this.idConsumidor = idConsumidor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActividadConsumidorEntityPK that = (ActividadConsumidorEntityPK) o;

        if (idActividad != that.idActividad) return false;
        if (idConsumidor != that.idConsumidor) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Math.toIntExact(idActividad);
        result = (int) (31 * result + idConsumidor);
        return result;
    }
}
