package com.backend.digitalhouse.integrador.service.impl;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.entity.Paciente;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.repository.PacienteRepository;
import com.backend.digitalhouse.integrador.service.IPacienteService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PacienteService implements IPacienteService {
    private final PacienteRepository pacienteRepository;
    private final ModelMapper modelMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(PacienteService.class);

    public PacienteService(PacienteRepository pacienteRepository, ModelMapper modelMapper) {
        this.pacienteRepository = pacienteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente) {
        Paciente pacienteGuardado = pacienteRepository.save(modelMapper.map(paciente,Paciente.class));
        PacienteSalidaDto pacienteSalidaDto = modelMapper.map(pacienteGuardado, PacienteSalidaDto.class);
        LOGGER.info("Paciente creado: {}", pacienteSalidaDto);
        return pacienteSalidaDto;
    }

    @Override
    public PacienteSalidaDto buscarPacientePorId(Long id) throws ResourceNotFoundException{
        Paciente paciente = pacienteRepository.findById(id).orElse(null);

        PacienteSalidaDto pacienteSalidaDto = null;
        if(paciente != null){
            pacienteSalidaDto = modelMapper.map(paciente, PacienteSalidaDto.class);
            LOGGER.info("Paciente encontrado por id: {}", pacienteSalidaDto);
        }
        else {
            LOGGER.error("El paciente por id : {} , no se ha encontrado en la base de datos", id);
            throw new ResourceNotFoundException("No se ha encontrado el paciente con id: " + id);
        }
        return pacienteSalidaDto;
    }

    @Override
    public List<PacienteSalidaDto> listarPacientes() {
        List<PacienteSalidaDto> pacientes = pacienteRepository.findAll().stream()
                .map(o -> modelMapper.map(o, PacienteSalidaDto.class)).toList();
        LOGGER.info("Listado de pacientes: {}", pacientes);

        return pacientes;
    }

    @Override
    public void eliminarPaciente(Long id) throws ResourceNotFoundException{
        if (buscarPacientePorId(id) != null){
            pacienteRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el paciente con id: {}", id);
        }
        else{
            LOGGER.error("No se pudo eliminar el paciente por que no se encontró en la base de datos");
            throw new ResourceNotFoundException("No se ha encontrado el paciente con id: " + id);
        }
    }

    @Override
    public PacienteSalidaDto actualizarPaciente(PacienteModificacionEntradaDto pacienteModificacionEntradaDto) throws ResourceNotFoundException{
        Paciente pacienteConDatosNuevos = modelMapper.map(pacienteModificacionEntradaDto, Paciente.class);

        Long idAModificar = pacienteConDatosNuevos.getId();

        Paciente pacienteActual = pacienteRepository.findById(idAModificar).orElse(null);
        PacienteSalidaDto pacienteSalidaDto = null;

        if (pacienteActual != null){
            pacienteRepository.save(pacienteConDatosNuevos);

            pacienteSalidaDto = modelMapper.map(pacienteActual, PacienteSalidaDto.class);
            LOGGER.warn("Paciente actualizado: {}", pacienteSalidaDto);
        }
        else {
            String mensaje = "No fue posible actualizar el paciente con id: " + idAModificar + ", por que no existe";
            LOGGER.error(mensaje);
            throw new ResourceNotFoundException(mensaje);
        }
        return pacienteSalidaDto;
    }
}