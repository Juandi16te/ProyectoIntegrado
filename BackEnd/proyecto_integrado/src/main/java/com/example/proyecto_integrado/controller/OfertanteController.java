package com.example.proyecto_integrado.controller;

import com.example.proyecto_integrado.dto.OfertanteDTO;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.mapper.impl.OfertanteMapperImpl;
import com.example.proyecto_integrado.services.impl.ActividadServiceImpl;
import com.example.proyecto_integrado.services.impl.ConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.OfertanteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ofertante/")
@CrossOrigin(origins = "http://localhost:4200")
public class OfertanteController {
    @Autowired
    private OfertanteServiceImpl ofertanteService;
    @Autowired
    private ConsumidorServiceImpl consumidorService;
    @Autowired
    private ActividadServiceImpl actividadService;
    @Autowired
    private OfertanteMapperImpl ofertanteMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para obtener todos los ofertantes
    @GetMapping("get")
    public ResponseEntity<List<OfertanteDTO>> get() {
        // Obtiene todas las entidades de ofertantes
        List<OfertanteEntity> ofertantes = ofertanteService.getAll();
        List<OfertanteDTO> ofertantesDTO = new ArrayList<>();
        // Mapea cada entidad a su DTO correspondiente y los agrega a la lista
        for (OfertanteEntity ofer : ofertantes) {
            OfertanteDTO oferDTO = ofertanteMapper.mapTo(ofer);
            ofertantesDTO.add(oferDTO);
        }
        // Devuelve la lista de DTOs en la respuesta con estado OK
        return new ResponseEntity<>(ofertantesDTO, HttpStatus.OK);
    }

    // Método para obtener un ofertante por su ID
    @GetMapping("get/{id}")
    public ResponseEntity<OfertanteDTO> getById(@PathVariable("id") Long id) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!ofertanteService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Intenta obtener la entidad de ofertante por su ID
        Optional<OfertanteEntity> ofertante = ofertanteService.getById(id);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(ofertanteMapper.mapTo(ofertante.get()), HttpStatus.OK);
    }

    // Método para obtener un ofertante por su nombre de usuario
    @GetMapping("getUsuario/{usuario}")
    public ResponseEntity<OfertanteDTO> getByUsuario(@PathVariable("usuario") String usuario) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!ofertanteService.existsByUsuario(usuario)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Intenta obtener la entidad de ofertante por su nombre de usuario
        Optional<OfertanteEntity> ofertante = ofertanteService.getByUsuario(usuario);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(ofertanteMapper.mapTo(ofertante.get()), HttpStatus.OK);
    }

    // Método para verificar si existe un ofertante con un nombre de usuario específico
    @GetMapping("exist/{usuario}")
    public ResponseEntity exist(@PathVariable("usuario") String usuario) {
        // Devuelve true si el ofertante existe, false si no
        if (ofertanteService.existsByUsuario(usuario)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    // Método para crear un nuevo ofertante
    @PostMapping("post")
    public ResponseEntity<OfertanteDTO> post(@RequestBody OfertanteDTO ofertanteDTO) {
        // Verifica si ya existe un ofertante con el mismo nombre de usuario
        if (ofertanteService.existsByUsuario(ofertanteDTO.getUsuario())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // Crea una nueva entidad de ofertante y establece sus propiedades
            OfertanteEntity ofertante = new OfertanteEntity();
            ofertante.setUsuario(ofertanteDTO.getUsuario());
            ofertante.setNombre(ofertanteDTO.getNombre());
            ofertante.setApellidos(ofertanteDTO.getApellidos());
            ofertante.setFoto(ofertanteDTO.getFoto());
            ofertante.setEmail(ofertanteDTO.getEmail());
            ofertante.setContrasena(ofertanteDTO.getContrasena()); // no se codifica la contraseña
            ofertante.setValoracion(null);
            if (ofertanteDTO.getDescripcion() == null) {
                ofertante.setDescripcion("");
            } else {
                ofertante.setDescripcion(ofertanteDTO.getDescripcion());
            }
            ofertante.setFechaCreacionUsuario(new Date());
            // Guarda la nueva entidad
            ofertanteService.post(ofertante);
            // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
            return new ResponseEntity<>(ofertanteMapper.mapTo(ofertante), HttpStatus.OK);
        }
    }

    // Método para actualizar un ofertante existente
    @PutMapping("put")
    public ResponseEntity<OfertanteDTO> put(@RequestBody OfertanteDTO ofertanteDTO) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!ofertanteService.existsById(ofertanteDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Hay que comprobar si existe un consumidor con el nuevo nombre pero no es él mismo en la tabla consumidor
        boolean existeConsu = false;
        // Si hay un consumidor con el Email distinto pero con el nombre de usuario nuevo
        // significa que existe un consumidor con ese nombre de usuario pero no es él mismo en la tabla consumidor
        if (consumidorService.existsByUsuario(ofertanteDTO.getUsuario())) {
            if (!consumidorService.getByUsuario(ofertanteDTO.getUsuario()).get().getEmail().equals(ofertanteDTO.getEmail())) {
                existeConsu = true;
            }
        }

        // Verifica si el nombre de usuario ya existe en otro ofertante o si hay un conflicto con un consumidor
        if (ofertanteService.existsByUsuarioIsAndIdIsNot(ofertanteDTO.getUsuario(), ofertanteDTO.getId()) || existeConsu) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // Crea una nueva entidad de ofertante y establece sus propiedades
            OfertanteEntity ofertante = new OfertanteEntity();
            ofertante.setId(ofertanteDTO.getId());
            ofertante.setUsuario(ofertanteDTO.getUsuario());
            ofertante.setNombre(ofertanteDTO.getNombre());
            ofertante.setApellidos(ofertanteDTO.getApellidos());
            ofertante.setFoto(ofertanteDTO.getFoto());
            ofertante.setEmail(ofertanteService.getById(ofertanteDTO.getId()).get().getEmail());
            ofertante.setContrasena(ofertanteService.getById(ofertanteDTO.getId()).get().getContrasena());
            ofertante.setValoracion(BigDecimal.valueOf(ofertanteDTO.getValoracion()));
            ofertante.setDescripcion(ofertanteDTO.getDescripcion());
            ofertante.setFechaCreacionUsuario(ofertanteService.getById(ofertanteDTO.getId()).get().getFechaCreacionUsuario());
            // Actualiza la entidad existente
            ofertanteService.put(ofertante);
            // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
            return new ResponseEntity<>(ofertanteMapper.mapTo(ofertante), HttpStatus.OK);
        }
    }

    // Método para eliminar un ofertante por su ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!ofertanteService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Desasigna el ofertante de todas las actividades y lo elimina
            actividadService.unassignOfertante(id);
            ofertanteService.deleteById(id);
            // Devuelve una respuesta sin contenido con estado NO CONTENT
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
