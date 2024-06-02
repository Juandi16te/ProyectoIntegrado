package com.example.proyecto_integrado.controller;

import com.example.proyecto_integrado.dto.ActividadConsumidorDTO;
import com.example.proyecto_integrado.dto.OfertanteDTO;
import com.example.proyecto_integrado.dto.TipoActividadDTO;
import com.example.proyecto_integrado.entity.ActividadConsumidorEntity;
import com.example.proyecto_integrado.entity.ActividadEntity;
import com.example.proyecto_integrado.entity.OfertanteEntity;
import com.example.proyecto_integrado.entity.TipoActividadEntity;
import com.example.proyecto_integrado.mapper.impl.ActividadConsumidorMapperImpl;
import com.example.proyecto_integrado.services.impl.ActividadConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.ActividadServiceImpl;
import com.example.proyecto_integrado.services.impl.ConsumidorServiceImpl;
import com.example.proyecto_integrado.services.impl.OfertanteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/actividadConsumidor/")
@CrossOrigin(origins = "http://localhost:4200")
public class ActividadConsumidorController {
    @Autowired
    private ActividadConsumidorServiceImpl actividadConsumidorService;
    @Autowired
    private ActividadServiceImpl actividadService;
    @Autowired
    private OfertanteServiceImpl ofertanteService;
    @Autowired
    private ConsumidorServiceImpl consumidorService;
    @Autowired
    private ActividadConsumidorMapperImpl actividadConsumidorMapper;

    // Método para obtener todas las actividades de consumidores
    @GetMapping("get")
    public ResponseEntity<List<ActividadConsumidorDTO>> get() {
        List<ActividadConsumidorEntity> actividadesConsumidores = actividadConsumidorService.getAll();
        List<ActividadConsumidorDTO> actividadesConsumidoresDTO = new ArrayList<>();
        // Mapea cada entidad a su DTO correspondiente y los agrega a la lista
        for (ActividadConsumidorEntity x : actividadesConsumidores) {
            ActividadConsumidorDTO actividadConsumidorDTO = actividadConsumidorMapper.mapTo(x);
            actividadesConsumidoresDTO.add(actividadConsumidorDTO);
        }
        // Devuelve la lista de DTOs en la respuesta con estado OK
        return new ResponseEntity<>(actividadesConsumidoresDTO, HttpStatus.OK);
    }

    // Método para obtener una actividad de consumidor por IDs de actividad y consumidor
    @GetMapping("get/{idActividad}/{idConsumidor}")
    public ResponseEntity<ActividadConsumidorDTO> getById(@PathVariable("idActividad") Long idActividad, @PathVariable("idConsumidor") Long idConsumidor) {
        Optional<ActividadConsumidorEntity> actividadConsumidor = actividadConsumidorService.getByIdActividadAndIdConsumidor(idActividad, idConsumidor);
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!actividadConsumidorService.existsByIdActividadAndIdConsumidor(idActividad, idConsumidor)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(actividadConsumidorMapper.mapTo(actividadConsumidor.get()), HttpStatus.OK);
    }

    // Método para verificar la existencia de una actividad de consumidor por IDs de actividad y consumidor
    @GetMapping("exist/{idActividad}/{idConsumidor}")
    public ResponseEntity exist(@PathVariable("idActividad") Long idActividad, @PathVariable("idConsumidor") Long idConsumidor) {
        // Devuelve true si existe, false si no, con estado OK
        if (actividadConsumidorService.existsByIdActividadAndIdConsumidor(idActividad, idConsumidor)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    // Método para crear una nueva actividad de consumidor
    @PostMapping("post")
    public ResponseEntity<ActividadConsumidorDTO> post(@RequestBody ActividadConsumidorDTO actividadConsumidorDTO) {
        // Verifica si la actividad existe, si no, devuelve un estado NOT FOUND
        if (!actividadService.existsById(actividadConsumidorDTO.getIdActividad())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Verifica si el consumidor existe, si no, devuelve un estado NOT FOUND
        if (!consumidorService.existsById(actividadConsumidorDTO.getIdConsumidor())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Obtiene la actividad y verifica si ya alcanzó el número máximo de participantes
        ActividadEntity actividad = actividadService.getById(actividadConsumidorDTO.getIdActividad()).get();
        if (actividad.getNumParticipantes() >= actividad.getNumParticipantesTotal()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Incrementa el número de participantes en la actividad
        actividad.setNumParticipantes(actividad.getNumParticipantes() + 1);
        actividadService.put(actividad);
        // Crea una nueva entidad de actividad de consumidor y establece sus propiedades
        ActividadConsumidorEntity actividadConsumidor = new ActividadConsumidorEntity();
        actividadConsumidor.setIdActividad(actividadConsumidorDTO.getIdActividad());
        actividadConsumidor.setIdConsumidor(actividadConsumidorDTO.getIdConsumidor());
        actividadConsumidor.setValoracion(null);
        // Guarda la nueva entidad
        actividadConsumidorService.post(actividadConsumidor);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(actividadConsumidorMapper.mapTo(actividadConsumidor), HttpStatus.OK);
    }

    // Método para actualizar una actividad de consumidor existente
    @PutMapping("put")
    public ResponseEntity put(@RequestBody ActividadConsumidorDTO actividadConsumidorDTO) {
        // Verifica si la actividad existe, si no, devuelve un estado NOT FOUND
        if (!actividadService.existsById(actividadConsumidorDTO.getIdActividad())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Verifica si el consumidor existe, si no, devuelve un estado NOT FOUND
        if (!consumidorService.existsById(actividadConsumidorDTO.getIdConsumidor())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Obtiene la actividad y el ofertante para actualizar sus medias
        ActividadEntity actividad = actividadService.getById(actividadConsumidorDTO.getIdActividad()).get();
        OfertanteEntity ofertante = ofertanteService.getById(actividad.getIdOfertante()).get();
        // Crea una nueva entidad de actividad de consumidor y establece sus propiedades
        ActividadConsumidorEntity actividadConsumidor = new ActividadConsumidorEntity();
        actividadConsumidor.setIdActividad(actividadConsumidorDTO.getIdActividad());
        actividadConsumidor.setIdConsumidor(actividadConsumidorDTO.getIdConsumidor());
        actividadConsumidor.setValoracion(actividadConsumidorDTO.getValoracion());
        // Actualiza la entidad existente
        actividadConsumidorService.put(actividadConsumidor);
        // Se actualiza la actividad y el ofertante para que se actualicen las medias
        actividadService.put(actividad);
        ofertanteService.put(ofertante);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(actividadConsumidorMapper.mapTo(actividadConsumidor), HttpStatus.OK);
    }

    // Método para eliminar una actividad de consumidor por IDs de actividad y consumidor
    @DeleteMapping("delete/{idActividad}/{idConsumidor}")
    public ResponseEntity delete(@PathVariable("idActividad") Long idActividad, @PathVariable("idConsumidor") Long idConsumidor) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!actividadConsumidorService.existsByIdActividadAndIdConsumidor(idActividad, idConsumidor)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Obtiene la actividad y decrementa el número de participantes
            ActividadEntity actividad = actividadService.getById(idActividad).get();
            actividad.setNumParticipantes(actividad.getNumParticipantes() - 1);
            actividadService.put(actividad);
            // Elimina la entidad de actividad de consumidor
            actividadConsumidorService.deleteByIdActividadAndIdConsumidor(idActividad, idConsumidor);
            // Devuelve una respuesta sin contenido con estado NO CONTENT
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
