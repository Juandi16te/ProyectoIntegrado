package com.example.proyecto_integrado.controller;

import com.example.proyecto_integrado.dto.ActividadDTO;
import com.example.proyecto_integrado.dto.TipoActividadDTO;
import com.example.proyecto_integrado.dto._ActividadRequest;
import com.example.proyecto_integrado.entity.ActividadEntity;
import com.example.proyecto_integrado.mapper.impl.ActividadMapperImpl;
import com.example.proyecto_integrado.services.impl.ActividadConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.ActividadServiceImpl;
import com.example.proyecto_integrado.services.impl.ConsumidorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actividad/")
@CrossOrigin(origins = "http://localhost:4200")
public class ActividadController {
    @Autowired
    private ActividadServiceImpl actividadService;
    @Autowired
    private ActividadConsumidorServiceImpl actividadConsumidorService;
    @Autowired
    private ConsumidorServiceImpl consumidorService;
    @Autowired
    private ActividadMapperImpl actividadMapper;

    // Método para obtener actividades según criterios específicos
    @PostMapping("get")
    public ResponseEntity<List<ActividadDTO>> get(@RequestBody _ActividadRequest request) {
        // Obtiene actividades según los criterios del request
        List<ActividadEntity> actividades = actividadService.getByCriteria(request.getIdTipoActividad(), request.getIdOfertante(), request.getNombre());
        List<ActividadDTO> actividadesDTO = new ArrayList<>();
        // Mapea cada entidad a su DTO correspondiente y los agrega a la lista
        for (ActividadEntity actividad : actividades) {
            ActividadDTO tipoDTO = actividadMapper.mapTo(actividad);
            actividadesDTO.add(tipoDTO);
        }
        // Devuelve la lista de DTOs en la respuesta con estado OK
        return new ResponseEntity<>(actividadesDTO, HttpStatus.OK);
    }

    // Método para obtener una actividad por su ID
    @GetMapping("get/{id}")
    public ResponseEntity getById(@PathVariable("id") Long id) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!actividadService.existsById(id)) {
            return new ResponseEntity<>("No se ha encontrado la actividad", HttpStatus.NOT_FOUND);
        }
        // Intenta obtener la entidad de actividad por su ID
        Optional<ActividadEntity> actividad = actividadService.getById(id);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(actividadMapper.mapTo(actividad.get()), HttpStatus.OK);
    }

    // Método para obtener actividades por ID de consumidor
    @GetMapping("getConsu/{id}")
    public ResponseEntity getByIdCosndumidor(@PathVariable("id") Long id) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!consumidorService.existsById(id)) {
            return new ResponseEntity<>("No se ha encontrado el consumidor", HttpStatus.NOT_FOUND);
        }
        // Obtiene las actividades según el ID del consumidor
        List<ActividadEntity> actividades = actividadService.getByIdConsumidor(id);
        List<ActividadDTO> actividadesDTO = new ArrayList<>();
        // Mapea cada entidad a su DTO correspondiente y los agrega a la lista
        for (ActividadEntity actividad : actividades) {
            ActividadDTO tipoDTO = actividadMapper.mapTo(actividad);
            actividadesDTO.add(tipoDTO);
        }
        // Devuelve la lista de DTOs en la respuesta con estado OK
        return new ResponseEntity<>(actividadesDTO, HttpStatus.OK);
    }

    // Método para crear una nueva actividad
    @PostMapping("post")
    public ResponseEntity<ActividadDTO> post(@RequestBody ActividadDTO actividadDTO) {
        // Crea una nueva entidad de actividad y establece sus propiedades
        ActividadEntity actividad = new ActividadEntity();
        actividad.setNombre(actividadDTO.getNombre());
        actividad.setDescripcion(actividadDTO.getDescripcion());
        actividad.setFechaInicio(actividadDTO.getFechaInicio());
        actividad.setFechaFin(actividadDTO.getFechaFin());
        actividad.setFechaCreacion(new Date());
        actividad.setUbicacion(actividadDTO.getUbicacion());
        actividad.setNumParticipantes(actividadDTO.getNumParticipantes());
        actividad.setNumParticipantesTotal(actividadDTO.getNumParticipantesTotal());
        actividad.setValoracion(null);
        actividad.setIdOfertante(actividadDTO.getIdOfertante());
        actividad.setIdTipoActividad(actividadDTO.getIdTipoActividad());
        // Guarda la nueva entidad
        actividadService.post(actividad);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(actividadMapper.mapTo(actividad), HttpStatus.OK);
    }

    // Método para actualizar una actividad existente
    @PutMapping("put")
    public ResponseEntity put(@RequestBody ActividadDTO actividadDTO) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!actividadService.existsById(actividadDTO.getId())) {
            return new ResponseEntity<>("No se ha encontrado la actividad", HttpStatus.NOT_FOUND);
        } else {
            // Crea una nueva entidad de actividad y establece sus propiedades
            ActividadEntity actividad = new ActividadEntity();
            actividad.setId(actividadDTO.getId());
            actividad.setNombre(actividadDTO.getNombre());
            actividad.setDescripcion(actividadDTO.getDescripcion());
            actividad.setFechaInicio(actividadDTO.getFechaInicio());
            actividad.setFechaFin(actividadDTO.getFechaFin());
            actividad.setFechaCreacion(actividadService.getById(actividadDTO.getId()).get().getFechaCreacion());
            actividad.setUbicacion(actividadDTO.getUbicacion());
            actividad.setNumParticipantes(actividadService.getById(actividadDTO.getId()).get().getNumParticipantes());
            // Verifica si el número de participantes total es menor que el número actual de participantes
            if (actividadDTO.getNumParticipantesTotal() < actividadService.getById(actividadDTO.getId()).get().getNumParticipantes()) {
                return new ResponseEntity<>("La actividad tiene más de " + actividadDTO.getNumParticipantesTotal() + " participantes inscritos", HttpStatus.BAD_REQUEST);
            } else {
                actividad.setNumParticipantesTotal(actividadDTO.getNumParticipantesTotal());
            }
            actividad.setValoracion(actividadService.getById(actividadDTO.getId()).get().getValoracion());
            actividad.setIdOfertante(actividadDTO.getIdOfertante());
            actividad.setIdTipoActividad(actividadDTO.getIdTipoActividad());
            // Actualiza la entidad existente
            actividadService.put(actividad);
            // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
            return new ResponseEntity<>(actividadMapper.mapTo(actividad), HttpStatus.OK);
        }
    }

    // Método para eliminar una actividad por su ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!actividadService.existsById(id)) {
            return new ResponseEntity<>("No se ha encontrado la actividad", HttpStatus.NOT_FOUND);
        } else {
            // Verifica si la actividad tiene participantes inscritos
            if (actividadService.getById(id).get().getNumParticipantes() > 0) {
                return new ResponseEntity<>("La actividad tiene participantes inscritos", HttpStatus.BAD_REQUEST);
            }
            // Borra la relación que tuviera con consumidores en la actividad
            actividadConsumidorService.deleteByIdActividad(id);
            // Finalmente, borra la actividad de la base de datos
            actividadService.deleteById(id);
            // Devuelve una respuesta sin contenido con estado NO CONTENT
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
