package com.example.proyecto_integrado.controller;

import com.example.proyecto_integrado.dto.ConsumidorDTO;
import com.example.proyecto_integrado.dto.OfertanteDTO;
import com.example.proyecto_integrado.entity.ActividadEntity;
import com.example.proyecto_integrado.entity.ConsumidorEntity;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.mapper.impl.ConsumidorMapperImpl;
import com.example.proyecto_integrado.services.impl.ActividadConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.ActividadServiceImpl;
import com.example.proyecto_integrado.services.impl.ConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.OfertanteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/consumidor/")
@CrossOrigin(origins = "http://localhost:4200")
public class ConsumidorController {
    @Autowired
    private ConsumidorServiceImpl consumidorService;
    @Autowired
    private ActividadConsumidorServiceImpl actividadConsumidorService;
    @Autowired
    private ActividadServiceImpl actividadService;
    @Autowired
    private OfertanteServiceImpl ofertanteService;
    @Autowired
    private ConsumidorMapperImpl consumidorMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para obtener todos los consumidores
    @GetMapping("get")
    public ResponseEntity<List<ConsumidorDTO>> get() {
        // Obtiene todas las entidades de consumidores
        List<ConsumidorEntity> consumidores = consumidorService.getAll();
        List<ConsumidorDTO> consumidoresDTO = new ArrayList<>();
        // Mapea cada entidad a su DTO correspondiente y los agrega a la lista
        for (ConsumidorEntity consu : consumidores) {
            ConsumidorDTO consuDTO = consumidorMapper.mapTo(consu);
            consumidoresDTO.add(consuDTO);
        }
        // Devuelve la lista de DTOs en la respuesta con estado OK
        return new ResponseEntity<>(consumidoresDTO, HttpStatus.OK);
    }

    // Método para obtener un consumidor por su ID
    @GetMapping("get/{id}")
    public ResponseEntity<ConsumidorDTO> getById(@PathVariable("id") Long id) {
        // Intenta obtener la entidad de consumidor por su ID
        Optional<ConsumidorEntity> consumidor = consumidorService.getById(id);
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!consumidorService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(consumidorMapper.mapTo(consumidor.get()), HttpStatus.OK);
    }

    // Método para obtener un consumidor por su nombre de usuario
    @GetMapping("getUsuario/{usuario}")
    public ResponseEntity<ConsumidorDTO> getByUsuario(@PathVariable("usuario") String usuario) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!consumidorService.existsByUsuario(usuario)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Intenta obtener la entidad de consumidor por su nombre de usuario
        Optional<ConsumidorEntity> consumidor = consumidorService.getByUsuario(usuario);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(consumidorMapper.mapTo(consumidor.get()), HttpStatus.OK);
    }

    // Método para verificar si existe un consumidor con un nombre de usuario específico
    @GetMapping("exist/{usuario}")
    public ResponseEntity exist(@PathVariable("usuario") String usuario) {
        // Devuelve true si el consumidor existe, false si no
        if (consumidorService.existsByUsuario(usuario)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    // Método para crear un nuevo consumidor
    @PostMapping("post")
    public ResponseEntity<ConsumidorDTO> post(@RequestBody ConsumidorDTO consumidorDTO) {
        // Verifica si ya existe un consumidor con el mismo nombre de usuario
        if (consumidorService.existsByUsuario(consumidorDTO.getUsuario())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // Crea una nueva entidad de consumidor y establece sus propiedades
            ConsumidorEntity consumidor = new ConsumidorEntity();
            consumidor.setUsuario(consumidorDTO.getUsuario());
            consumidor.setNombre(consumidorDTO.getNombre());
            consumidor.setApellidos(consumidorDTO.getApellidos());
            consumidor.setFoto(consumidorDTO.getFoto());
            consumidor.setEmail(consumidorDTO.getEmail());
            consumidor.setContrasena(consumidorDTO.getContrasena()); // no se codifica la contraseña
            consumidor.setFechaCreacionUsuario(new Date());
            // Guarda la nueva entidad
            consumidorService.post(consumidor);
            // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
            return new ResponseEntity<>(consumidorMapper.mapTo(consumidor), HttpStatus.OK);
        }
    }

    // Método para actualizar un consumidor existente
    @PutMapping("put")
    public ResponseEntity<ConsumidorDTO> put(@RequestBody ConsumidorDTO consumidorDTO) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!consumidorService.existsById(consumidorDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Hay que comprobar si existe un ofertante con el nuevo nombre pero no es él mismo en la tabla consumidor
        boolean existeOfer = false;
        // Si hay un ofertante con el Email distinto pero con el nombre de usuario nuevo
        // significa que existe un ofertante con ese nombre de usuario pero no es él mismo en la tabla consumidor
        if (ofertanteService.existsByUsuario(consumidorDTO.getUsuario())) {
            if (!ofertanteService.getByUsuario(consumidorDTO.getUsuario()).get().getEmail().equals(consumidorDTO.getEmail())) {
                existeOfer = true;
            }
        }

        // Verifica si el nombre de usuario ya existe en otro consumidor o si hay un conflicto con un ofertante
        if (consumidorService.existsByUsuarioIsAndIdIsNot(consumidorDTO.getUsuario(), consumidorDTO.getId()) || existeOfer) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // Crea una nueva entidad de consumidor y establece sus propiedades
            ConsumidorEntity consumidor = new ConsumidorEntity();
            consumidor.setId(consumidorDTO.getId());
            consumidor.setUsuario(consumidorDTO.getUsuario());
            consumidor.setNombre(consumidorDTO.getNombre());
            consumidor.setApellidos(consumidorDTO.getApellidos());
            consumidor.setFoto(consumidorDTO.getFoto());
            consumidor.setEmail(consumidorService.getById(consumidorDTO.getId()).get().getEmail());
            consumidor.setContrasena(consumidorService.getById(consumidorDTO.getId()).get().getContrasena());
            consumidor.setFechaCreacionUsuario(consumidorService.getById(consumidorDTO.getId()).get().getFechaCreacionUsuario());
            // Actualiza la entidad existente
            consumidorService.put(consumidor);
            // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
            return new ResponseEntity<>(consumidorMapper.mapTo(consumidor), HttpStatus.OK);
        }
    }

    // Método para eliminar un consumidor por su ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!consumidorService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Resta un participante a las actividades en las que estaba inscrito que no han comenzado
            actividadService.restNumParticipantesByIdConsumidorAndActividadNotFinished(id);
            // Borra la relación que tuviera con actividades en las que estaba inscrito
            actividadConsumidorService.deleteByIdConsumidor(id);
            // Finalmente, borra el consumidor de la base de datos
            consumidorService.deleteById(id);
            // Devuelve una respuesta sin contenido con estado NO CONTENT
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
