package com.backend.digitalhouse.integrador.service.impl;

import com.backend.digitalhouse.integrador.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.dao.IDao;
import com.backend.digitalhouse.integrador.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.entity.Odontologo;
import com.backend.digitalhouse.integrador.service.IOdontologoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {
    private final IDao<Odontologo> odontologoIDao;
    private final ModelMapper modelMapper;

    public OdontologoService(IDao<Odontologo> odontologoIDao, ModelMapper modelMapper) {
        this.odontologoIDao = odontologoIDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {
        return modelMapper.map(odontologoIDao.registrar(modelMapper.map(odontologo, Odontologo.class)), OdontologoSalidaDto.class);
    }

    @Override
    public OdontologoSalidaDto buscarOdontologoPorId(int id) {
        return modelMapper.map(odontologoIDao.buscarPorId(id), OdontologoSalidaDto.class);
    }

    @Override
    public List<OdontologoSalidaDto> listarOdontologos() {
        return odontologoIDao.listarTodos().stream().map(o -> modelMapper.map(o, OdontologoSalidaDto.class)).toList();
    }

    @Override
    public void eliminarOdontologo(int id) {
        odontologoIDao.eliminar(id);
    }
}

