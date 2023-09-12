package com.backend.digitalhouse.integrador.service;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.modificacion.TurnoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.exceptions.BadRequestException;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;

import java.util.List;

public interface ITurnoService {
    List<TurnoSalidaDto> listarTurnos();

    TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException;

    TurnoSalidaDto buscarTurnoPorId(Long id);

    void eliminarTurno(Long id) throws ResourceNotFoundException;

    TurnoSalidaDto actualizarTurno(TurnoModificacionEntradaDto turnoModificado) throws ResourceNotFoundException;
}
