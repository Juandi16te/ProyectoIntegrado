package com.example.proyecto_integrado.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Clase para filtrar las solicitudes y validar el token JWT
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUsersDetailsService customUsersDetailsService; // Servicio para cargar los detalles del usuario
    @Autowired
    private JwtGenerator jwtGenerator; // Generador de tokens JWT

    // Método para obtener el token de autorización de la solicitud HTTP
    private String obtenerTokenDeSolicitud(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // Obtener el encabezado de autorización
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) { // Verificar si el encabezado es válido
            return bearerToken.substring(7, bearerToken.length()); // Devolver el token JWT
        }
        return null; // Devolver nulo si no se encuentra un token
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = obtenerTokenDeSolicitud(request); // Obtener el token de la solicitud
        if (StringUtils.hasText(token) && jwtGenerator.validarToken(token)) { // Verificar si el token es válido
            String username = jwtGenerator.obtenerUsernameDeJwt(token); // Obtener el nombre de usuario del token
            UserDetails userDetails = customUsersDetailsService.loadUserByUsername(username); // Cargar los detalles del usuario
            List<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(); // Obtener los roles del usuario
            // Verificar si el usuario tiene el rol OFERTANTE o CONSUMIDOR
            if (userRoles.contains("OFERTANTE") || userRoles.contains("CONSUMIDOR")) {
                // Establecer la autenticación del usuario en el contexto de seguridad
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response); // Continuar con la cadena de filtros
    }
}
