package com.example.proyecto_integrado.security;

import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.services.impl.ConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.OfertanteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// Clase para buscar si existe el usuario
@Service
public class CustomUsersDetailsService implements UserDetailsService {
    private OfertanteServiceImpl ofertanteService;
    private ConsumidorServiceImpl consumidorService;

    @Autowired
    public CustomUsersDetailsService(OfertanteServiceImpl ofertanteService, ConsumidorServiceImpl consumidorService) {
        this.ofertanteService = ofertanteService;
        this.consumidorService = consumidorService;
    }

    // Método para mapear los roles a las autoridades
    public Collection<GrantedAuthority> mapToAutorities(List<String> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority((role))).collect(Collectors.toList());
    }

    // Método para cargar el usuario por nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<String> roles = new ArrayList<String>();

        // Verificar si el usuario es tanto consumidor como ofertante
        if (ofertanteService.existsByUsuario(username) && consumidorService.existsByUsuario(username)) {
            OfertanteEntity ofertante = ofertanteService.getByUsuario(username).get();
            roles.add("OFERTANTE");
            roles.add("CONSUMIDOR");
            return new User(ofertante.getUsuario(), ofertante.getContrasena(), mapToAutorities(roles));
        }

        // Verificar si el usuario es solo consumidor
        if (consumidorService.existsByUsuario(username)) {
            ConsumidorEntity consumidor = consumidorService.getByUsuario(username).get();
            roles.add("CONSUMIDOR");
            return new User(consumidor.getUsuario(), consumidor.getContrasena(), mapToAutorities(roles));
        }

        // Verificar si el usuario es solo ofertante
        if (ofertanteService.existsByUsuario(username)) {
            OfertanteEntity ofertante = ofertanteService.getByUsuario(username).get();
            roles.add("OFERTANTE");
            return new User(ofertante.getUsuario(), ofertante.getContrasena(), mapToAutorities(roles));
        }

        // Lanzar excepción si el usuario no se encuentra
        throw new UsernameNotFoundException("Usuario no encontrado");
    }

}
