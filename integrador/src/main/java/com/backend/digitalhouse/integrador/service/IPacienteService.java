package com.backend.digitalhouse.integrador.service;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;

import java.util.List;

public interface IPacienteService {
    List<PacienteSalidaDto> listarPacientes();

    PacienteSalidaDto registrarPaciente(PacienteEntradaDto paciente);

    PacienteSalidaDto buscarPacientePorId(Long id);

    void eliminarPaciente(Long id);

    PacienteSalidaDto actualizarPaciente(PacienteModificacionEntradaDto pacienteModificado);


}
