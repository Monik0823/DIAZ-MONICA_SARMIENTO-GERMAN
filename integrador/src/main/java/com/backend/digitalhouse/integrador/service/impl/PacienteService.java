package com.backend.digitalhouse.integrador.service.impl;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.entity.Paciente;
import com.backend.digitalhouse.integrador.repository.PacienteRepository;
import com.backend.digitalhouse.integrador.service.IPacienteService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class PacienteService implements IPacienteService {
    private final PacienteRepository pacienteRepository;
    private final ModelMapper modelMapper;

    public PacienteService(PacienteRepository pacienteRepository, ModelMapper modelMapper) {
        this.pacienteRepository = pacienteRepository;
        this.modelMapper = modelMapper;
        configureMapping();
    }

    @Override
    public PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente) {
        //convertir Dto de entrada a entidad para poder enviarlo a la capa de persistencia
        Paciente pacienteRecibido = dtoEntradaAEntidad(paciente);
        Paciente pacienteRegistrado = pacienteRepository.registrar(pacienteRecibido);

        return entidadADtoSalida(pacienteRegistrado);
    }

    @Override
    public PacienteSalidaDto modificarPaciente(PacienteModificacionEntradaDto pacienteModificado) {
        PacienteSalidaDto pacienteSalidaDto = null;
        Paciente pacienteAModificar = pacienteRepository.buscarPorId(pacienteModificado.getId());

        if(pacienteAModificar != null){
            pacienteAModificar = dtoModificadoAEntidad(pacienteModificado);
            pacienteSalidaDto = entidadADtoSalida(pacienteRepository.modificar(pacienteAModificar));

        }

        return pacienteSalidaDto;
    }

    @Override
    public PacienteSalidaDto buscarPacientePorId(int id) {
        return entidadADtoSalida(pacienteRepository.buscarPorId(id));
    }

    @Override
    public List<PacienteSalidaDto> listarPacientes() {
        List<Paciente> pacientes = pacienteRepository.listarTodos();

        //List<PacienteSalidaDto> pacienteSalidaDtos = new ArrayList<>();

        //for(Paciente p : pacientes){
        // pacienteSalidaDtos.add(entidadADtoSalida(p));
        //}

        return pacientes.stream()
                .map(this::entidadADtoSalida)
                .toList();
    }

    @Override
    public void eliminarPaciente(int id) {
        pacienteRepository.eliminar(id);

    }


    private void configureMapping() {
        modelMapper.typeMap(PacienteEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteEntradaDto::getDomicilio, Paciente::setDomicilio));
        modelMapper.typeMap(Paciente.class, PacienteSalidaDto.class)
                .addMappings(mapper -> mapper.map(Paciente::getDomicilio, PacienteSalidaDto::setDomicilio));
        modelMapper.typeMap(PacienteModificacionEntradaDto.class, Paciente.class)
                .addMappings(mapper -> mapper.map(PacienteModificacionEntradaDto::getDomicilio, Paciente::setDomicilio));

    }

    public Paciente dtoEntradaAEntidad(PacienteEntradaDto pacienteEntradaDto) {
        return modelMapper.map(pacienteEntradaDto, Paciente.class);
    }

    public PacienteSalidaDto entidadADtoSalida(Paciente paciente) {
        return modelMapper.map(paciente, PacienteSalidaDto.class);
    }

    public Paciente dtoModificadoAEntidad(PacienteModificacionEntradaDto pacienteEntradaDto) {
        return modelMapper.map(pacienteEntradaDto, Paciente.class);
    }


}