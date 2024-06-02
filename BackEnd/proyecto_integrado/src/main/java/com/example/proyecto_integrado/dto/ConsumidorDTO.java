package com.example.proyecto_integrado.dto;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ConsumidorDTO implements Serializable {
    private Long id;
    private String usuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String contrasena;
    private String foto;
    private Date fechaCreacionUsuario;
}
