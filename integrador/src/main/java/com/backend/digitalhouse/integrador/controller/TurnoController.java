package com.backend.digitalhouse.integrador.controller;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.modificacion.TurnoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.exceptions.BadRequestException;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.service.ITurnoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/turnos")
public class TurnoController {

    private ITurnoService turnoService;

    @Autowired
    public TurnoController(ITurnoService turnoService) {
        this.turnoService = turnoService;
    }

    //POST
    @PostMapping("/registrar")
    public ResponseEntity<TurnoSalidaDto> registrarTurno(@Valid @RequestBody TurnoEntradaDto turnoEntradaDto) throws BadRequestException {
        return new ResponseEntity<>(turnoService.registrarTurno(turnoEntradaDto), HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("actualizar")
    public ResponseEntity<TurnoSalidaDto> actualizarTurno(@Valid @RequestBody TurnoModificacionEntradaDto turnoModificacionEntradaDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(turnoService.actualizarTurno(turnoModificacionEntradaDto), HttpStatus.OK);
    }

    //GET
    @GetMapping("{id}")
    public ResponseEntity<TurnoSalidaDto> obtenerTurnoPorId(@PathVariable Long id){
        return new ResponseEntity<>(turnoService.buscarTurnoPorId(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<TurnoSalidaDto>> listarTurnos(){
        return new ResponseEntity<>(turnoService.listarTurnos(), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarTurno(@PathVariable Long id) throws ResourceNotFoundException{
        turnoService.eliminarTurno(id);

        Map<String, String> message = new HashMap<>();
        message.put("message", "Turno eliminado correctamente");

        //Cambiamos el codigo Http devuelto de 204 a 200 para que al cliente llegue el mensaje que estamos enviado.
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

