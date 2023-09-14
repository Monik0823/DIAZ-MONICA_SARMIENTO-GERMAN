package com.backend.digitalhouse.integrador;


import com.backend.digitalhouse.integrador.dto.entrada.modificacion.OdontologoModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.modificacion.PacienteModificacionEntradaDto;
import com.backend.digitalhouse.integrador.dto.entrada.odontologo.OdontologoEntradaDto;
import com.backend.digitalhouse.integrador.dto.salida.odontologo.OdontologoSalidaDto;
import com.backend.digitalhouse.integrador.exceptions.ResourceNotFoundException;
import com.backend.digitalhouse.integrador.service.impl.OdontologoService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class OdontologoServiceTest {

    @Autowired
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    void debeInsertarUnOdontologoNuevoDeNombreGermanConId1(){
        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto(12354, "German", "Sarmiento");

        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);

        assertEquals("German", odontologoSalidaDto.getNombre());
        assertEquals(1, odontologoSalidaDto.getId());
    }

    @Test
    @Order(2)
    void deberiaRetornarUnaListaNoVaciaDeOdontologo(){
        assertTrue(odontologoService.listarOdontologos().size() > 0);
    }

    @Test
    void alIntentarActualizarELOdontologoId2_deberiaLanzarseUnaResourceNotFoundException(){
        OdontologoModificacionEntradaDto odontologModificacionEntradaDto = new OdontologoModificacionEntradaDto();
        odontologModificacionEntradaDto.setId(2L);
        assertThrows(ResourceNotFoundException.class, () -> odontologoService.actualizarOdontologo(odontologModificacionEntradaDto));
    }

    @Test
    @Order(3)
    void alIntentarEliminarUnOdontologoYaEliminado_deberiaLanzarseUnResourceNotFoundException(){
        try{
            odontologoService.eliminarOdontologo(1L);
        } catch (Exception e){
            e.printStackTrace();
        }
        assertThrows(ResourceNotFoundException.class, () -> odontologoService.eliminarOdontologo(1L));
    }

}


