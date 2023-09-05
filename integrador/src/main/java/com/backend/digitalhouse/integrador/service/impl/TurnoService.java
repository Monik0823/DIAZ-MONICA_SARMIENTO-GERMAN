package com.backend.digitalhouse.integrador.service.impl;

import com.backend.digitalhouse.integrador.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.entity.Turno;
import com.backend.digitalhouse.integrador.repository.TurnoRepository;
import com.backend.digitalhouse.integrador.service.ITurnoService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService implements ITurnoService {

    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);

    private final TurnoRepository turnoRepository;
    private final ModelMapper modelMapper;
    private final OdontologoService odontologoService;
    private final PacienteService pacienteService;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, OdontologoService odontologoService, PacienteService pacienteService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.odontologoService = odontologoService;
        this.pacienteService = pacienteService;
    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) {
        TurnoSalidaDto turnoSalidaDto;

        PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());

        String pacienteNoEnBdd = "El paciente no se encuentra en nuestra base de datos";
        String odontologoNoEnBdd = "El odontologo no se encuentra en nuestra base de datos";

        if(paciente == null || odontologo == null){
            if(paciente == null && odontologo == null){
                LOGGER.error("El paciente y el odontologo no se encuentran en nuestra base de datos");
                throw new RuntimeException("El paciente y el odontologo no se encuentran en nuestra base de datos");
            } else if (paciente == null) {
                LOGGER.error(pacienteNoEnBdd);
                throw new RuntimeException(pacienteNoEnBdd);
            } else {
                LOGGER.error(odontologoNoEnBdd);
                throw new RuntimeException(odontologoNoEnBdd);
            }
        } else {
            Turno turnoNuevo = turnoRepository.save(modelMapper.map(turnoEntradaDto, Turno.class));
            turnoSalidaDto = modelMapper.map(turnoNuevo, TurnoSalidaDto.class);
            LOGGER.info("Nuevo turno registrado con exito: {}", turnoSalidaDto);
        }

        return turnoSalidaDto;
    }

    @Override
    public List<TurnoSalidaDto> listarTurnos() {
        List<TurnoSalidaDto> turnos = turnoRepository.findAll().stream()
                .map(o -> modelMapper.map(o, TurnoSalidaDto.class)).toList();
        LOGGER.info("Listado de turnos: {}", turnos);

        return turnos;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turno = turnoRepository.findById(id).orElse(null);

        TurnoSalidaDto turnoSalidaDto = null;
        if(turno != null){
            turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
            LOGGER.info("Turno encontrado por id: {}", turnoSalidaDto);
        }
        else {
            LOGGER.error("El turno por id : {} , no se ha encontrado en la base de datos", id);
        }
        return turnoSalidaDto;
    }

    @Override
    public void eliminarTurno(Long id) {
        if (buscarTurnoPorId(id) != null){
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id: {}", id);
        }
        else{
            LOGGER.error("No se pudo eliminar el turno por que no se encontró en la base de datos");
        }
    }
}
