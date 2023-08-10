package com.backend.integrador.service;
import com.backend.integrador.dao.IDao;
import com.backend.integrador.entity.Odontologo;

import java.util.List;

public class OdontologoService {
    private final IDao<Odontologo> OdontologoIDao;

    public OdontologoService(IDao<Odontologo> entidadIDao){
        this.OdontologoIDao = entidadIDao;
    }

    public Odontologo registrarOdontologo(Odontologo odontologo){
        return OdontologoIDao.registrar(odontologo);
    }

    public List<Odontologo> listarTodosLosOdontologos() {
        return OdontologoIDao.listarTodos();
    }

    public Odontologo buscarOdontologoPorId(int id){
        return OdontologoIDao.buscarPorId(id);
    }
}
