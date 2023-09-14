package com.backend.digitalhouse.integrador;


import com.backend.digitalhouse.integrador.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.paciente.DomicilioEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.paciente.PacienteEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.turno.TurnoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.turno.TurnoSalidaDto;
import com.backend.digitalhouse.integrador.exceptions.BadRequestException;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.service.impl.OdontologoService;
import com.backend.digitalhouse.integrador.service.impl.PacienteService;
import com.backend.digitalhouse.integrador.service.impl.TurnoService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TurnoServiceTest {

    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    void debePermitirGenerarUnTurnoConId1yFecha20230926() throws BadRequestException, ResourceNotFoundException {
        DomicilioEntradaDto domicilioEntradaDto = new DomicilioEntradaDto("SanJuan", 26, "Medellin", "Antioquia");

        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Maria", "Moreno", 89546, LocalDate.of(2020, 12, 31), domicilioEntradaDto);
        pacienteService.registrarPaciente(pacienteEntradaDto);

        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto(12354, "Antonio", "Sarmiento");
        odontologoService.registrarOdontologo(odontologoEntradaDto);

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto(1L,1L, LocalDateTime.of(2023,9,26,9,3,0));
        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);

        assertEquals(LocalDateTime.of(2023,9,26,9,3,0), turnoSalidaDto.getFechaYHora());
        assertEquals(1, turnoSalidaDto.getId());
    }

    @Test
    @Order(2)
    void deberiaRetornarUnaListaNoVaciaDeTurnos() throws BadRequestException, ResourceNotFoundException{
        DomicilioEntradaDto domicilioEntradaDto = new DomicilioEntradaDto("San Antonio", 36, "Sabaneta", "Antioquia");

        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Gloria", "Moreno", 215739, LocalDate.of(2020, 12, 29), domicilioEntradaDto);
        pacienteService.registrarPaciente(pacienteEntradaDto);

        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto(8358, "Mario", "Moreno");
        odontologoService.registrarOdontologo(odontologoEntradaDto);

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto(1L,1L, LocalDateTime.of(2023,10,26,9,3,0));
        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);

        assertTrue(turnoService.listarTurnos().size() > 0);
    }

    @Test
    @Order(3)
    void alIntentarEliminarUnTurnoYaEliminado_deberiaLanzarseUnResourceNotFoundException() throws BadRequestException, ResourceNotFoundException{
        DomicilioEntradaDto domicilioEntradaDto = new DomicilioEntradaDto("Calle larga", 20, "Envigado", "Antioquia");

        PacienteEntradaDto pacienteEntradaDto = new PacienteEntradaDto("Milena", "Zapata", 45698, LocalDate.of(2020, 11, 20), domicilioEntradaDto);
        pacienteService.registrarPaciente(pacienteEntradaDto);

        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto(45128, "Tomas", "Rendon");
        odontologoService.registrarOdontologo(odontologoEntradaDto);

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto(1L,1L, LocalDateTime.of(2023,12,26,9,3,0));
        TurnoSalidaDto turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);

        try{
            turnoService.eliminarTurno(1L);
        } catch (Exception e){
            e.printStackTrace();
        }
        assertThrows(ResourceNotFoundException.class, () -> turnoService.eliminarTurno(1L));
    }
}
