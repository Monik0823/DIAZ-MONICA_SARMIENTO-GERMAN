package com.backend.integrador.dao.impl;
import com.backend.integrador.dao.IDao;
import com.backend.integrador.entity.Odontologo;
import org.apache.log4j.Logger;

import java.util.List;

public class OdontologoDaoEnMemoria implements IDao<Odontologo> {

    private static final Logger LOGGER = Logger.getLogger(OdontologoDaoEnMemoria.class);
    private List<Odontologo> odontologoRepository;

    public OdontologoDaoEnMemoria(List<Odontologo> odontologoRepository) {
        this.odontologoRepository = odontologoRepository;
    }

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        odontologo.setId(odontologoRepository.size() + 1);
        odontologoRepository.add(odontologo);
        LOGGER.info("Odontologo guardada: " + odontologo);
        return odontologo;
    }

    @Override
    public List<Odontologo> listarTodos() {
        LOGGER.info("Listado de todos los odontologos: \n" + odontologoRepository);
        return odontologoRepository;
    }
    @Override
    public Odontologo buscarPorId(int id) {
        Odontologo odontologoBuscado = odontologoRepository.get(id - 1);
        if (odontologoBuscado != null){
            LOGGER.info("El odontologo de id " + id + " ha sido encontrado: " + odontologoBuscado);
        }
        else {
            LOGGER.info("El odontologo no se ha encontrado");
        }
        return odontologoBuscado;
    }
}