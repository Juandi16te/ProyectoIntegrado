package com.example.proyecto_integrado.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ActividadDTO implements Serializable {
    private Long id;
    private String nombre;
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaCreacion;
    private String ubicacion;
    private int numParticipantes;
    private int numParticipantesTotal;
    private double valoracion;
    private Long idOfertante;
    private Long idTipoActividad;
}
