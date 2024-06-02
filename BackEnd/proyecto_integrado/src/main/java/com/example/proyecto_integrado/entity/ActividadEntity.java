package com.example.proyecto_integrado.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@jakarta.persistence.Table(name = "actividad", schema = "proyecto_integrado", catalog = "")
public class ActividadEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "descripcion")
    private String descripcion;
    @Basic
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Basic
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @Basic
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @Basic
    @Column(name = "ubicacion")
    private String ubicacion;
    @Basic
    @Column(name = "num_participantes")
    private int numParticipantes;
    @Basic
    @Column(name = "num_participantes_total")
    private int numParticipantesTotal;
    @Basic
    @Column(name = "valoracion")
    private BigDecimal valoracion;
    @Basic
    @Column(name = "id_ofertante")
    private Long idOfertante;
    @Basic
    @Column(name = "id_tipo_actividad")
    private Long idTipoActividad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaIncio) {
        this.fechaInicio = fechaIncio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getNumParticipantes() {
        return numParticipantes;
    }

    public void setNumParticipantes(int numParticipantes) {
        this.numParticipantes = numParticipantes;
    }

    public int getNumParticipantesTotal() {
        return numParticipantesTotal;
    }

    public void setNumParticipantesTotal(int numParticipantesTotal) {
        this.numParticipantesTotal = numParticipantesTotal;
    }

    public BigDecimal getValoracion() {
        return valoracion;
    }

    public void setValoracion(BigDecimal valoracion) {
        this.valoracion = valoracion;
    }

    public Long getIdOfertante() {
        return idOfertante;
    }

    public void setIdOfertante(Long idOfertante) {
        this.idOfertante = idOfertante;
    }

    public Long getIdTipoActividad() {
        return idTipoActividad;
    }

    public void setIdTipoActividad(Long idTipoActividad) {
        this.idTipoActividad = idTipoActividad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActividadEntity that = (ActividadEntity) o;

        if (id != that.id) return false;
        if (numParticipantes != that.numParticipantes) return false;
        if (numParticipantesTotal != that.numParticipantesTotal) return false;
        if (idOfertante != that.idOfertante) return false;
        if (idTipoActividad != that.idTipoActividad) return false;
        if (nombre != null ? !nombre.equals(that.nombre) : that.nombre != null) return false;
        if (descripcion != null ? !descripcion.equals(that.descripcion) : that.descripcion != null) return false;
        if (fechaInicio != null ? !fechaInicio.equals(that.fechaInicio) : that.fechaInicio != null) return false;
        if (fechaFin != null ? !fechaFin.equals(that.fechaFin) : that.fechaFin != null) return false;
        if (fechaCreacion != null ? !fechaCreacion.equals(that.fechaCreacion) : that.fechaCreacion != null)
            return false;
        if (ubicacion != null ? !ubicacion.equals(that.ubicacion) : that.ubicacion != null) return false;
        if (valoracion != null ? !valoracion.equals(that.valoracion) : that.valoracion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = Math.toIntExact(id);
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + (fechaInicio != null ? fechaInicio.hashCode() : 0);
        result = 31 * result + (fechaFin != null ? fechaFin.hashCode() : 0);
        result = 31 * result + (fechaCreacion != null ? fechaCreacion.hashCode() : 0);
        result = 31 * result + (ubicacion != null ? ubicacion.hashCode() : 0);
        result = 31 * result + numParticipantes;
        result = 31 * result + numParticipantesTotal;
        result = 31 * result + (valoracion != null ? valoracion.hashCode() : 0);
        result = (int) (31 * result + idOfertante);
        result = (int) (31 * result + idTipoActividad);
        return result;
    }
}
