package com.backend.digitalhouse.integrador.dao.impl;

import com.backend.digitalhouse.integrador.dao.IDao;
import com.backend.digitalhouse.integrador.entity.Odontologo;
import com.backend.digitalhouse.integrador.entity.Paciente;
import com.backend.digitalhouse.integrador.entity.Turno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TurnoDaoH2 implements IDao<Turno> {
    private final Logger LOGGER = LoggerFactory.getLogger(TurnoDaoH2.class);



    //se utilizan listas por el momento, queda pendiente la implementacion de base de datos
    private List<Turno> turnos;

    public TurnoDaoH2() {
        turnos = new ArrayList<>();
    }

    @Override
    public Turno registrar(Turno turno) {
        Turno turnoGuardado = turno;

        turnos.add(turnoGuardado);
        LOGGER.info("Turno guardado: {}", turno);

        return turnoGuardado;
    }

    @Override
    public List<Turno> listarTodos() {
        LOGGER.info("Listado de todos los turnos: {}", turnos);
        return turnos;
    }

    @Override
    public Turno buscarPorId(int id) {
        //return (Turno) turnos.stream().filter(t -> t.getId() == id);
        Turno turnoBuscado = null;
        for (Turno t : turnos) {
            if (t.getId() == id) {
                turnoBuscado = t;
                LOGGER.info("Turno encontrado: {}", turnoBuscado);
            } else {
                LOGGER.error("No se ha encontrado turno con ese id");
            }

        }
        //turnoBuscado = turnos.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        //LOGGER.info("Turno encontrado: {}", turnoBuscado);
        return turnoBuscado;
    }

    //eliminar
    @Override
    public void eliminar(int id) {
        Turno t = buscarPorId(id);
        if (t != null) {
            turnos.remove(t);
            LOGGER.info("Se ha eliminado el turno con id: {}", id);
        } else {
            LOGGER.error("No existe un turno con ese id");
        }

    }


    @Override
    public Turno modificar(Turno turno) {
        //alt 1
        //turnos.set(turnos.indexOf(buscar(turno.getId())), turno);
        //return turno;

        //alt2
        eliminar(turno.getId());
        registrar(turno);
        LOGGER.info("Turno actualizado, nuevos datos: {}", turno);
        return turno;
    }
}

