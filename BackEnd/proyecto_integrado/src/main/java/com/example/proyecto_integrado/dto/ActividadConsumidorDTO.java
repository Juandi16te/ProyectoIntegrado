package com.example.proyecto_integrado.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ActividadConsumidorDTO implements Serializable {
    private Long idActividad;
    private Long idConsumidor;
    private BigDecimal valoracion;
}
