package com.example.proyecto_integrado.controller;

import com.example.proyecto_integrado.dto.*;
import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.mapper.impl.ConsumidorMapperImpl;
import com.example.proyecto_integrado.mapper.impl.OfertanteMapperImpl;
import com.example.proyecto_integrado.security.JwtGenerator;
import com.example.proyecto_integrado.services.impl.ConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.OfertanteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "http://localhost:4200")
public class _AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConsumidorServiceImpl consumidorService;
    @Autowired
    private OfertanteServiceImpl ofertanteService;
    @Autowired
    private ConsumidorMapperImpl consumidorMapper;
    @Autowired
    private OfertanteMapperImpl ofertanteMapper;
    @Autowired
    private JwtGenerator jwtGenerator;

    // Método para registrar un consumidor
    @PostMapping("registerConsumidor")
    public ResponseEntity<Object> registrarConsumidor(@RequestBody ConsumidorDTO consumidorDTO) {
        // Verifica si el usuario o el email ya existen
        if (consumidorService.existsByUsuario(consumidorDTO.getUsuario())
                || ofertanteService.existsByUsuario(consumidorDTO.getUsuario())) {
            return new ResponseEntity<>("existe usuario", HttpStatus.BAD_REQUEST);
        }
        if (consumidorService.existsByEmail(consumidorDTO.getEmail())
                || ofertanteService.existsByEmail(consumidorDTO.getEmail())) {
            return new ResponseEntity<>("existe email", HttpStatus.BAD_REQUEST);
        }
        // Crea una nueva entidad de consumidor y establece sus propiedades
        ConsumidorEntity consumidor = new ConsumidorEntity();
        consumidor.setUsuario(consumidorDTO.getUsuario());
        consumidor.setNombre(consumidorDTO.getNombre());
        consumidor.setApellidos(consumidorDTO.getApellidos());
        consumidor.setFoto(consumidorDTO.getFoto());
        consumidor.setEmail(consumidorDTO.getEmail());
        consumidor.setContrasena(passwordEncoder.encode(consumidorDTO.getContrasena()));
        consumidor.setFechaCreacionUsuario(new Date());
        // Guarda el nuevo consumidor
        consumidorService.post(consumidor);
        // Devuelve una respuesta con el consumidor mapeado a DTO
        return new ResponseEntity<>(consumidorMapper.mapTo(consumidor), HttpStatus.OK);
    }

    // Método para registrar un ofertante
    @PostMapping("registerOfertante")
    public ResponseEntity<Object> registrarOfertante(@RequestBody OfertanteDTO ofertanteDTO) {
        // Verifica si el usuario o el email ya existen
        if (consumidorService.existsByUsuario(ofertanteDTO.getUsuario())
                || ofertanteService.existsByUsuario(ofertanteDTO.getUsuario())) {
            return new ResponseEntity<>("existe usuario", HttpStatus.BAD_REQUEST);
        }
        if (consumidorService.existsByEmail(ofertanteDTO.getEmail())
                || ofertanteService.existsByEmail(ofertanteDTO.getEmail())) {
            return new ResponseEntity<>("existe email", HttpStatus.BAD_REQUEST);
        }
        // Crea una nueva entidad de ofertante y establece sus propiedades
        OfertanteEntity ofertante = new OfertanteEntity();
        ofertante.setUsuario(ofertanteDTO.getUsuario());
        ofertante.setNombre(ofertanteDTO.getNombre());
        ofertante.setApellidos(ofertanteDTO.getApellidos());
        ofertante.setFoto(ofertanteDTO.getFoto());
        ofertante.setEmail(ofertanteDTO.getEmail());
        ofertante.setContrasena(passwordEncoder.encode(ofertanteDTO.getContrasena()));
        ofertante.setValoracion(null);
        ofertante.setDescripcion(ofertanteDTO.getDescripcion());
        ofertante.setFechaCreacionUsuario(new Date());
        // Guarda el nuevo ofertante
        ofertanteService.post(ofertante);
        // Devuelve una respuesta con el ofertante mapeado a DTO
        return new ResponseEntity<>(ofertanteMapper.mapTo(ofertante), HttpStatus.OK);
    }

    // Método para registrar un usuario como ofertante y consumidor
    @PostMapping("registerOfertanteConsumidor")
    public ResponseEntity<Object> registrarOfertanteConsumidor(@RequestBody OfertanteDTO ofertanteDTO) {
        // Verifica si el usuario o el email ya existen
        if (consumidorService.existsByUsuario(ofertanteDTO.getUsuario())
                || ofertanteService.existsByUsuario(ofertanteDTO.getUsuario())) {
            return new ResponseEntity<>("existe usuario", HttpStatus.BAD_REQUEST);
        }
        if (consumidorService.existsByEmail(ofertanteDTO.getEmail())
                || ofertanteService.existsByEmail(ofertanteDTO.getEmail())) {
            return new ResponseEntity<>("existe email", HttpStatus.BAD_REQUEST);
        }
        // Se registra como ofertante
        OfertanteEntity ofertante = new OfertanteEntity();
        ofertante.setUsuario(ofertanteDTO.getUsuario());
        ofertante.setNombre(ofertanteDTO.getNombre());
        ofertante.setApellidos(ofertanteDTO.getApellidos());
        ofertante.setFoto(ofertanteDTO.getFoto());
        ofertante.setEmail(ofertanteDTO.getEmail());
        ofertante.setContrasena(passwordEncoder.encode(ofertanteDTO.getContrasena()));
        ofertante.setValoracion(null);
        ofertante.setDescripcion(ofertanteDTO.getDescripcion());
        ofertante.setFechaCreacionUsuario(new Date());
        ofertanteService.post(ofertante);
        // Se registra como consumidor también
        ConsumidorEntity consumidor = new ConsumidorEntity();
        consumidor.setUsuario(ofertanteDTO.getUsuario());
        consumidor.setNombre(ofertanteDTO.getNombre());
        consumidor.setApellidos(ofertanteDTO.getApellidos());
        consumidor.setFoto(ofertanteDTO.getFoto());
        consumidor.setEmail(ofertanteDTO.getEmail());
        consumidor.setContrasena(passwordEncoder.encode(ofertanteDTO.getContrasena()));
        consumidor.setFechaCreacionUsuario(new Date());
        consumidorService.post(consumidor);
        // Devuelve una respuesta con el ofertante mapeado a DTO
        return new ResponseEntity<>(ofertanteMapper.mapTo(ofertante), HttpStatus.OK);
    }

    // Método para iniciar sesión
    @PostMapping("login")
    public ResponseEntity<_AuthRespuestaDTO> login(@RequestBody _UserDTO user) {
        // Autentica al usuario
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUser(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long idOfertante = 0L;
        Long idConsumidor = 0L;
        // Verifica si el usuario existe como ofertante y obtiene su ID
        if (ofertanteService.existsByUsuario(user.getUser())) {
            idOfertante = ofertanteService.getByUsuario(user.getUser()).get().getId();
        }
        // Verifica si el usuario existe como consumidor y obtiene su ID
        if (consumidorService.existsByUsuario(user.getUser())) {
            idConsumidor = consumidorService.getByUsuario(user.getUser()).get().getId();
        }
        // Genera un token JWT
        String token = jwtGenerator.generarToken(authentication, idOfertante, idConsumidor);
        // Si el token es válido, crea y devuelve una respuesta con el token y el rol del usuario
        if (token != null && token != "") {
            _AuthRespuestaDTO respuesta = new _AuthRespuestaDTO(token);
            respuesta.setUser(user.getUser());
            respuesta.setRol(authentication.getAuthorities().toString());
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            // Si el token no es válido, devuelve una respuesta de error
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
