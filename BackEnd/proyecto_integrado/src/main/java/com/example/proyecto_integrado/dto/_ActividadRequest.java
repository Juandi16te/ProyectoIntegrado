package com.example.proyecto_integrado.dto;

import lombok.Data;

import java.util.List;

@Data
public class _ActividadRequest {
    private Long idTipoActividad;
    private Long idOfertante;
    private String nombre;
}