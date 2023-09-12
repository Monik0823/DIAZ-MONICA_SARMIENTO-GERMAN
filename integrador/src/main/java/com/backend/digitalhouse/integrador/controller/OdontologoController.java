package com.backend.digitalhouse.integrador.controller;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.service.impl.OdontologoService;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.h2.util.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {
    private final OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    //POST
    @PostMapping("registrar")
    public ResponseEntity<OdontologoSalidaDto> registrarOdontologo(@Valid @RequestBody OdontologoEntradaDto odontologo) {
        return new ResponseEntity<>(odontologoService.registrarOdontologo(odontologo), HttpStatus.CREATED);
    }

    //PUT
    @PutMapping("actualizar")
    public ResponseEntity<OdontologoSalidaDto> actualizarOdontologo(@Valid @RequestBody OdontologoModificacionEntradaDto odontologo) throws ResourceNotFoundException{
        return new ResponseEntity<>(odontologoService.actualizarOdontologo(odontologo), HttpStatus.OK);
    }

    //GET
    @GetMapping("{id}")
    public ResponseEntity<OdontologoSalidaDto> obtenerOdontologoPorId(@PathVariable Long id) {
        return new ResponseEntity<>(odontologoService.buscarOdontologoPorId(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<OdontologoSalidaDto>> listarOdontologos() {
        return new ResponseEntity<>(odontologoService.listarOdontologos(), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        odontologoService.eliminarOdontologo(id);

        Map<String, String> message = new HashMap<>();
        message.put("message", "Odontologo eliminado correctamente");

        //Cambiamos el codigo Http devuelto de 204 a 200 para que al cliente llegue el mensaje que estamos enviado.
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}