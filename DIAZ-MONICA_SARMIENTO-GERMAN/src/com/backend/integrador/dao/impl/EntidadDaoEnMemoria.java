package com.backend.integrador.dao.impl;
import com.backend.integrador.dao.IDao;
import com.backend.integrador.entity.Entidad;
import org.apache.log4j.Logger;

import java.util.List;

public class EntidadDaoEnMemoria implements IDao<Entidad> {

    private static final Logger LOGGER = Logger.getLogger(EntidadDaoEnMemoria.class);
    private List<Entidad> entidadRepository;

    public EntidadDaoEnMemoria(List<Entidad> entidadRepository) {
        this.entidadRepository = entidadRepository;
    }

    @Override
    public Entidad registrar(Entidad entidad) {
        entidadRepository.add(entidad);
        LOGGER.info("Entidad guardada: " + entidad);
        return entidad;
    }

    @Override
    public List<Entidad> listarTodos() {
        LOGGER.info("Listado de todas las entidades: \n" + entidadRepository);
        return entidadRepository;
    }
    @Override
    public Entidad buscarPorId(int id) {
        Entidad entidadBuscado = entidadRepository.get(id - 1);
        if (entidadBuscado != null)
            LOGGER.info("El medicamento de id " + id + " ha sido encontrado: " + entidadBuscado);
        else LOGGER.info("Medicamento no encontrado");
        return entidadBuscado;
    }
}