package com.example.proyecto_integrado.security;

import com.example.proyecto_integrado.repository.ConsumidorRepository;
import com.example.proyecto_integrado.repository.OfertanteRepository;
import com.example.proyecto_integrado.services.impl.ConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.OfertanteServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// Clase para generar el token JWT
@Component
public class JwtGenerator {
    // Clave secreta utilizada para firmar el token JWT
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    // Método para generar un token JWT utilizando la autenticación, ID del ofertante y ID del consumidor
    public String generarToken(Authentication authentication, Long idOfertante, Long idConsumidor) {
        String username = authentication.getName(); // Obtener el nombre de usuario de la autenticación
        Date tiempoActual = new Date(); // Obtener la fecha y hora actual
        Date expiracionToken = new Date(tiempoActual.getTime() + 600000); // Establecer la expiración del token (10 minutos)
        List<String> authorities = authentication.getAuthorities().stream() // Obtener las autoridades del usuario
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Línea para generar el token JWT
        String token = Jwts.builder()
                .setSubject(username) // Establecer el nombre de usuario como el sujeto del token
                .claim("authorities", authorities) // Añadir las autoridades como un reclamo
                .claim("idOfertante", idOfertante) // Añadir el ID del ofertante como un reclamo
                .claim("idConsumidor", idConsumidor) // Añadir el ID del consumidor como un reclamo
                .setIssuedAt(new Date()) // Establecer la fecha de emisión del token
                .setExpiration(expiracionToken) // Establecer la fecha de expiración del token
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firmar el token utilizando la clave secreta y el algoritmo HS256
                .compact(); // Compactar y serializar el token
        return token; // Devolver el token generado
    }

    // Método para extraer el nombre de usuario de un token JWT
    public String obtenerUsernameDeJwt(String token) {
        Claims claims = Jwts.parser() // Crear un parser para el token
                .setSigningKey(getSigningKey()) // Establecer la clave secreta para firmar el token
                .parseClaimsJws(token) // Parsear el token
                .getBody(); // Obtener el cuerpo del token (claims)
        return claims.getSubject(); // Devolver el sujeto del token (nombre de usuario)
    }

    // Método para validar el token JWT
    public boolean validarToken(String token) {
        try {
            Jwts.parser() // Crear un parser para el token
                    .setSigningKey(getSigningKey()) // Establecer la clave secreta para firmar el token
                    .parseClaimsJws(token); // Parsear el token
            return true; // Si no hay excepciones, el token es válido
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("jwt ha expirado o es incorrecto"); // Si hay una excepción, el token no es válido
        }
    }

    // Método para obtener la clave de firma
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodificar la clave secreta
        return Keys.hmacShaKeyFor(keyBytes); // Crear la clave HMAC utilizando los bytes decodificados
    }
}
