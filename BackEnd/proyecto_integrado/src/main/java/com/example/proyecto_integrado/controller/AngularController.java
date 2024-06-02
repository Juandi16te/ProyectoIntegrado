package com.example.proyecto_integrado.controller;

import com.example.proyecto_integrado.dto.TipoActividadDTO;
import com.example.proyecto_integrado.entity.TipoActividadEntity;
import com.example.proyecto_integrado.mapper.impl.TipoActividadMapperImpl;
import com.example.proyecto_integrado.services.impl.TipoActividadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipoActividad/")
@CrossOrigin(origins = "http://localhost:4200")
public class TipoActividadController {
    @Autowired
    private TipoActividadServiceImpl tipoActividadService;
    @Autowired
    private TipoActividadMapperImpl tipoActividadMapper;

    // Método para obtener todos los tipos de actividad
    @GetMapping("get")
    public ResponseEntity<List<TipoActividadDTO>> get() {
        // Obtiene todas las entidades de tipo actividad
        List<TipoActividadEntity> TiposActividades = tipoActividadService.getAll();
        List<TipoActividadDTO> tiposActividadesDTO = new ArrayList<>();
        // Mapea cada entidad a su DTO correspondiente y los agrega a la lista
        for (TipoActividadEntity tipo : TiposActividades) {
            TipoActividadDTO tipoDTO = tipoActividadMapper.mapTo(tipo);
            tiposActividadesDTO.add(tipoDTO);
        }
        // Devuelve la lista de DTOs en la respuesta con estado OK
        return new ResponseEntity<>(tiposActividadesDTO, HttpStatus.OK);
    }

    // Método para obtener un tipo de actividad por su ID
    @GetMapping("get/{id}")
    public ResponseEntity<TipoActividadDTO> getById(@PathVariable("id") Long id) {
        // Intenta obtener la entidad de tipo actividad por su ID
        Optional<TipoActividadEntity> tipoActividad = tipoActividadService.getById(id);
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!tipoActividadService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(tipoActividadMapper.mapTo(tipoActividad.get()), HttpStatus.OK);
    }

    // Método para crear un nuevo tipo de actividad
    @PostMapping("post")
    public ResponseEntity<TipoActividadDTO> post(@RequestBody TipoActividadDTO tipoActividadDTO) {
        // Crea una nueva entidad de tipo actividad y establece sus propiedades
        TipoActividadEntity tipoActividad = new TipoActividadEntity();
        tipoActividad.setTipo(tipoActividadDTO.getTipo());
        // Guarda la nueva entidad
        tipoActividadService.post(tipoActividad);
        // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
        return new ResponseEntity<>(tipoActividadMapper.mapTo(tipoActividad), HttpStatus.OK);
    }

    // Método para actualizar un tipo de actividad existente
    @PutMapping("put")
    public ResponseEntity<TipoActividadDTO> put(@RequestBody TipoActividadDTO tipoActividadDTO) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!tipoActividadService.existsById(tipoActividadDTO.getId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Crea una nueva entidad de tipo actividad y establece sus propiedades
            TipoActividadEntity tipoActividad = new TipoActividadEntity();
            tipoActividad.setTipo(tipoActividadDTO.getTipo());
            // Actualiza la entidad existente
            tipoActividadService.put(tipoActividad);
            // Devuelve la entidad mapeada a DTO en la respuesta con estado OK
            return new ResponseEntity<>(tipoActividadMapper.mapTo(tipoActividad), HttpStatus.OK);
        }
    }

    // Método para eliminar un tipo de actividad por su ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        // Verifica si la entidad existe, si no, devuelve un estado NOT FOUND
        if (!tipoActividadService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            // Elimina la entidad por su ID
            tipoActividadService.deleteById(id);
            // Devuelve una respuesta sin contenido con estado NO CONTENT
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
