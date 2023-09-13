package com.backend.digitalhouse.integrador.service.impl;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.modificacion.TurnoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.OdontologoTurnoSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.PacienteTurnoSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.entity.Odontologo;
import com.backend.digitalhouse.integrador.entity.Paciente;
import com.backend.digitalhouse.integrador.entity.Turno;
import com.backend.digitalhouse.integrador.exceptions.BadRequestException;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
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

    private String mensajeValidacionTurnoEntradaDto;

    private Turno turnoEntradaEntitidadValidada;
    private Paciente pacienteEntradaEntidadValidada;
    private Odontologo odontologoEntradaEntidadValidada;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, OdontologoService odontologoService, PacienteService pacienteService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.odontologoService = odontologoService;
        this.pacienteService = pacienteService;
        configureMapping();
    }

    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException, ResourceNotFoundException{
        TurnoSalidaDto turnoSalidaDto = null;

        PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());

        String pacienteNoEnBdd = "El paciente no se encuentra en nuestra base de datos";
        String odontologoNoEnBdd = "El odontologo no se encuentra en nuestra base de datos";

        if(pacienteSalidaDto == null || odontologoSalidaDto == null){
            if(pacienteSalidaDto == null && odontologoSalidaDto == null){
                LOGGER.error("El paciente y el odontologo no se encuentran en nuestra base de datos");
                throw new BadRequestException("El paciente y el odontologo no se encuentran en nuestra base de datos");
            } else if (pacienteSalidaDto == null) {
                LOGGER.error(pacienteNoEnBdd);
                throw new BadRequestException(pacienteNoEnBdd);
            } else {
                LOGGER.error(odontologoNoEnBdd);
                throw new BadRequestException(odontologoNoEnBdd);
            }
        } else {
            Turno turnoNuevo = turnoRepository.save(modelMapper.map(turnoEntradaDto, Turno.class));
            turnoSalidaDto = modelMapper.map(turnoNuevo, TurnoSalidaDto.class);
            turnoSalidaDto.setPacienteTurnoSalidaDto(modelMapper.map(pacienteSalidaDto, PacienteTurnoSalidaDto.class));
            turnoSalidaDto.setOdontologoTurnoSalidaDto(modelMapper.map(odontologoSalidaDto, OdontologoTurnoSalidaDto.class));
            LOGGER.info("Nuevo turno registrado con exito: {}", turnoSalidaDto);
        }

        return turnoSalidaDto;
    }

    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) throws ResourceNotFoundException{
        Turno turno = turnoRepository.findById(id).orElse(null);

        TurnoSalidaDto turnoSalidaDto = null;
        if(turno != null){
            turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
            LOGGER.info("Turno encontrado por id: {}", turnoSalidaDto);
        }
        else {
            LOGGER.error("El turno por id : {} , no se ha encontrado en la base de datos", id);
            throw new ResourceNotFoundException("No se ha encontrado el turno con id: " + id);
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
    public void eliminarTurno(Long id) throws ResourceNotFoundException{
        if (buscarTurnoPorId(id) != null){
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id: {}", id);
        }
        else{
            LOGGER.error("No se pudo eliminar el turno por que no se encontró en la base de datos");
            throw new ResourceNotFoundException("No se ha encontrado el turno con id: " + id);
        }
    }

    @Override
    public TurnoSalidaDto actualizarTurno(TurnoModificacionEntradaDto turnoModificado) throws ResourceNotFoundException {
        TurnoSalidaDto turnoSalidaDto = null;

        if (this.esValidoTurnoModificacionEntradaDto(turnoModificado)){
            this.turnoEntradaEntitidadValidada.setPaciente(this.pacienteEntradaEntidadValidada);
            this.turnoEntradaEntitidadValidada.setOdontologo(this.odontologoEntradaEntidadValidada);

            Turno turnoNuevo = turnoRepository.save(this.turnoEntradaEntitidadValidada);
            turnoSalidaDto = modelMapper.map(turnoNuevo, TurnoSalidaDto.class);
            LOGGER.info("Truno actualizado: {}", turnoSalidaDto);
        }
        else{
            String mensaje = "No fue posible actualizar el turno por que, " + this.getMensajeValidacionTurnoEntradaDto();
            LOGGER.error(mensaje);
            throw new ResourceNotFoundException(mensaje);
        }

        return turnoSalidaDto;
    }

    private boolean esValidoTurnoModificacionEntradaDto(TurnoModificacionEntradaDto turnoModificacionEntradaDto) throws ResourceNotFoundException{
        Long turnoId = turnoModificacionEntradaDto.getId();
        Long pacienteId = turnoModificacionEntradaDto.getPacienteId();
        Long odontologoId = turnoModificacionEntradaDto.getOdontologoId();

        Turno turno = turnoRepository.findById(turnoId).orElse(null);

        if (turno == null){
            this.mensajeValidacionTurnoEntradaDto = "Turno: " + turnoId + " no existe";
            return false;
        }

        this.turnoEntradaEntitidadValidada = turno;

        PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorId(pacienteId);

        if (pacienteSalidaDto == null){
            this.mensajeValidacionTurnoEntradaDto = "Paciente: " + pacienteId + " no existe";
            return false;
        }

        this.pacienteEntradaEntidadValidada = modelMapper.map(pacienteSalidaDto, Paciente.class);

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorId(odontologoId);

        if (odontologoSalidaDto == null){
            this.mensajeValidacionTurnoEntradaDto = "Odontologo: " + odontologoId + " no existe";
            return false;
        }

        this.odontologoEntradaEntidadValidada = modelMapper.map(odontologoSalidaDto, Odontologo.class);

        return true;
    }

    private String getMensajeValidacionTurnoEntradaDto() {
        return this.mensajeValidacionTurnoEntradaDto;
    }

    private void configureMapping() {
        modelMapper.typeMap(Turno.class, TurnoSalidaDto.class)
                .addMappings(mapper -> mapper.map(Turno::getPaciente, TurnoSalidaDto::setPacienteTurnoSalidaDto))
                .addMappings(mapper -> mapper.map(Turno::getOdontologo, TurnoSalidaDto::setOdontologoTurnoSalidaDto));
    }

}
