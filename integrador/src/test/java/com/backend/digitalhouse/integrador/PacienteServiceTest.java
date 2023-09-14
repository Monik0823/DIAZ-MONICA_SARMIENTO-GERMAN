package com.backend.digitalhouse.integrador;

import com.backend.digitalhouse.integrador.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.paciente.DomicilioEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.paciente.PacienteSalidaDto;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.service.impl.PacienteService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class PacienteServiceTest {

    @Autowired
    private PacienteService pacienteService;

    @Test
    @Order(1)
    void debeInsertarUnPacienteNuevoDeNombreMonicaConId1(){
        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Monica", "Diaz", 125478, LocalDate.of(2020, 12, 31), new DomicilioEntradaDto("Palmas", 3826, "Medellin", "Antioquia" ));

        PacienteSalidaDto pacienteSalidaDto = pacienteService.registrarPaciente(pacienteEntradaDto);

        assertEquals("Monica", pacienteSalidaDto.getNombre());
        assertEquals(1, pacienteSalidaDto.getId());
    }

    @Test
    @Order(2)
    void deberiaRetornarUnaListaNoVaciaDePacientes(){
        assertTrue(pacienteService.listarPacientes().size() > 0);
    }

    @Test
    void alIntentarActualizarELPacienteId2_deberiaLanzarseUnaResourceNotFoundException(){
        PacienteModificacionEntradaDto pacienteModificacionEntradaDto = new PacienteModificacionEntradaDto();
        pacienteModificacionEntradaDto.setId(2L);
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.actualizarPaciente(pacienteModificacionEntradaDto));
    }

    @Test
    @Order(3)
    void alIntentarEliminarUnPacienteYaEliminado_deberiaLanzarseUnResourceNotFoundException(){
        try{
            pacienteService.eliminarPaciente(1L);
        } catch (Exception e){
            e.printStackTrace();
        }
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.eliminarPaciente(1L));
    }
}

