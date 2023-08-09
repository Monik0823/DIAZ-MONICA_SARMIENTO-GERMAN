package com.backend.integrador.service;
import com.backend.integrador.dao.IDao;
import com.backend.integrador.entity.Entidad;
import java.util.List;

public class EntidadService {
    private final IDao<Entidad> entidadIDao;

    public EntidadService(IDao<Entidad> entidadIDao){
        this.entidadIDao = entidadIDao;
    }

    public Entidad registrarEntidad(Entidad entidad){
        return entidadIDao.registrar(entidad);
    }

    public List<Entidad> listarTodosLosEntidades() {
        return entidadIDao.listarTodos();
    }

    public Entidad buscarEntidadPorId(int id){
        return entidadIDao.buscarPorId(id);
    }
}
