package com.backend.digitalhouse.integrador.service;

import com.backend.digitalhouse.integrador.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.TurnoSalidaDto;

import java.util.List;

public interface ITurnoService {
    TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto);

    List<TurnoSalidaDto> listarTurnos();

    TurnoSalidaDto buscarTurnoPorId(Long id);

    void eliminarTurno(Long id);
}
