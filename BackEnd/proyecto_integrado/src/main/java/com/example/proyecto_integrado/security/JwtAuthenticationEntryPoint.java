package com.example.proyecto_integrado.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Clase para manejar las excepciones de autenticación
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Método para manejar la entrada del punto de autenticación
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()); // Enviar un error de no autorizado al cliente
    }
}
