package com.backend.digitalhouse.integrador.service.impl;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.entity.Odontologo;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.repository.OdontologoRepository;
import com.backend.digitalhouse.integrador.service.IOdontologoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {
    private final OdontologoRepository odontologoRepository;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);

    public OdontologoService(OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {
        Odontologo odontologoGuardado = odontologoRepository.save(modelMapper.map(odontologo,Odontologo.class));
        OdontologoSalidaDto odontologoSalidaDto = modelMapper.map(odontologoGuardado, OdontologoSalidaDto.class);
        LOGGER.info("Odontologo creado: {}", odontologoSalidaDto);
        return odontologoSalidaDto;
    }

    @Override
    public OdontologoSalidaDto buscarOdontologoPorId(Long id) {
        Odontologo odontologo = odontologoRepository.findById(id).orElse(null);

        OdontologoSalidaDto odontologoSalidaDto = null;
        if(odontologo != null){
            odontologoSalidaDto = modelMapper.map(odontologo, OdontologoSalidaDto.class);
            LOGGER.info("Odontologo encontrado por id: {}", odontologoSalidaDto);
        }
        else {
            LOGGER.error("El odontologo por id : {} , no se ha encontrado en la base de datos", id);
        }
        return odontologoSalidaDto;
    }

    @Override
    public List<OdontologoSalidaDto> listarOdontologos() {
        List<OdontologoSalidaDto> odontologos = odontologoRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OdontologoSalidaDto.class)).toList();
        LOGGER.info("Listado de odontologos: {}", odontologos);

        return odontologos;
    }

    @Override
    public void eliminarOdontologo(Long id) throws ResourceNotFoundException{
        if (buscarOdontologoPorId(id) != null){
            odontologoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el odontologo con id: {}", id);
        }
        else{
            LOGGER.error("No se pudo eliminar el odontologo por que no se encontró en la base de datos");
            throw new ResourceNotFoundException("No se ha encontrado el odontologo con id: " + id);
        }
    }

    @Override
    public OdontologoSalidaDto actualizarOdontologo(OdontologoModificacionEntradaDto odontologoModificacionEntradaDto) throws ResourceNotFoundException{
        Odontologo odontologoConDatosNuevos = modelMapper.map(odontologoModificacionEntradaDto, Odontologo.class);

        Long idAModificar = odontologoConDatosNuevos.getId();

        Odontologo odontologoActual = odontologoRepository.findById(idAModificar).orElse(null);
        OdontologoSalidaDto odontologoSalidaDto = null;

        if (odontologoActual != null){
            odontologoRepository.save(odontologoConDatosNuevos);

            odontologoSalidaDto = modelMapper.map(odontologoActual, OdontologoSalidaDto.class);
            LOGGER.warn("Odontologo actualizado: {}", odontologoSalidaDto);
        }
        else {
            String mensaje = "No fue posible actualizar el odontologo con id: " + idAModificar + ", por que no existe";
            LOGGER.error(mensaje);
            throw new ResourceNotFoundException(mensaje);
        }
        return odontologoSalidaDto;
    }
}

