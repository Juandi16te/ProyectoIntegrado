package com.example.proyecto_integrado.services.impl;

import com.example.proyecto_integrado.entity.ActividadEntity;
import com.example.proyecto_integrado.repository.ActividadConsumidorRepository;
import com.example.proyecto_integrado.repository.ActividadRepository;
import com.example.proyecto_integrado.services.ActividadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActividadServiceImpl implements ActividadService {
    @Autowired
    ActividadRepository actividadRepository;
    @Autowired
    ActividadConsumidorRepository actividadConsumidorRepository;

    @Override
    public Boolean existsById(Long id) {
        return actividadRepository.existsById(id);
    }

    @Override
    public List<ActividadEntity> getByIdConsumidor(Long id) {
        return actividadRepository.getByIdConsumidor(id);
    }

    @Override
    public List<ActividadEntity> getAll() {
        return actividadRepository.getAll();
    }

    @Override
    public Optional<ActividadEntity> getById(Long id) {
        return actividadRepository.findById(id);
    }

    @Override
    public List<ActividadEntity> getByCriteria(Long idTipoActividad, Long idOfertante, String nombre) {
        boolean noIdTipoActividad = (idTipoActividad == null || idTipoActividad == 0);
        boolean noIdOfertante = (idOfertante == null || idOfertante == 0);
        boolean noNombre = (nombre == null || nombre.isEmpty());

        if (noIdTipoActividad && noIdOfertante && noNombre) {
            return actividadRepository.getAll();
        } else if (noIdTipoActividad && noIdOfertante) {
            return actividadRepository.findDistinctByNombreContainingOrderByFechaInicio(nombre);
        } else if (noIdTipoActividad && noNombre) {
            return actividadRepository.findDistinctByIdOfertanteOrderByFechaInicio(idOfertante);
        } else if (noIdOfertante && noNombre) {
            return actividadRepository.findDistinctByIdTipoActividadIsOrderByFechaInicio(idTipoActividad);
        } else if (noIdTipoActividad) {
            return actividadRepository.findDistinctByIdOfertanteIsAndNombreContainingOrderByFechaInicio(idOfertante, nombre);
        } else if (noIdOfertante) {
            return actividadRepository.findDistinctByIdTipoActividadIsAndNombreContainingOrderByFechaInicio(idTipoActividad, nombre);
        } else if (noNombre) {
            return actividadRepository.findDistinctByIdTipoActividadIsAndIdOfertanteIsOrderByFechaInicio(idTipoActividad, idOfertante);
        } else {
            return actividadRepository.findDistinctByIdTipoActividadIsAndIdOfertanteIsAndNombreContainingOrderByFechaInicio(idTipoActividad, idOfertante, nombre);
        }
    }

    @Override
    public void post(ActividadEntity actividad) {
        if (actividad != null) {
            actividadRepository.save(actividad);
        }
    }

    @Override
    public void put(ActividadEntity actividad) {
        if (actividad != null) {
            //se actualiza su valoración con la media de valoraciones que tenga
            if (actividadConsumidorRepository.findAverageValoracionByIdActividad(actividad.getId()) != null) {
                actividad.setValoracion(actividadConsumidorRepository.findAverageValoracionByIdActividad(actividad.getId()));
            }
            actividadRepository.save(actividad);
        }
    }

    @Override
    public void unassignOfertante(Long idOfertante) {
        actividadRepository.unassignOfertante(idOfertante);
    }

    @Override
    public void deleteById(Long id) {
        actividadRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void restNumParticipantesByIdConsumidorAndActividadNotFinished(Long idConsumidor) {
        actividadRepository.restNumParticipantesByIdConsumidorAndActividadNotFinished(idConsumidor);
    }

}
