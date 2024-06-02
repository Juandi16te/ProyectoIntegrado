package com.example.proyecto_integrado.dto;

import lombok.Data;

//esta clase devuelve la info con el token y el tipo que tenga este
@Data
public class _AuthRespuestaDTO {
    private String accesToken;
    private String tokenType = "Bearer ";
    private String user;
    private String rol;

    public _AuthRespuestaDTO(String accesToken) {
        this.accesToken = accesToken;
    }
}
