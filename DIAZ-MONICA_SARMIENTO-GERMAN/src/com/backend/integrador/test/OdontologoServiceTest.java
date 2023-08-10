package com.backend.integrador.test;

import com.backend.integrador.dao.impl.OdontologoDaoEnMemoria;
import com.backend.integrador.dao.impl.OdontologoDaoH2;
import com.backend.integrador.entity.Odontologo;
import com.backend.integrador.service.OdontologoService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;

public class OdontologoServiceTest {

    private static Connection connection = null;
    private static final Logger LOGGER = Logger.getLogger(OdontologoServiceTest.class);

    @BeforeAll
    static void init(){
        LOGGER.info("Creación de la tabla ODONTOLOGOS");
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/parcialBack;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
        } catch (Exception e){
            LOGGER.error("Se genera error creando la tabla ODONTOLOGOS");
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex){
                LOGGER.error("Se genera error cerrando la conexión");
                LOGGER.error(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    OdontologoService odontologoService = new OdontologoService(new OdontologoDaoH2());
    OdontologoService odontologoServiceMemoria = new OdontologoService(new OdontologoDaoEnMemoria(new ArrayList<>()));


    @Test
    void deberiaAgregarUnOdontologo(){
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("           Test: deberiaAgregarUnOdontologo       ");
        LOGGER.info("--------------------------------------------------");
        Odontologo ent1 = new Odontologo(50, "Pepito", "Perez");
        Odontologo odontologoInsertado = odontologoService.registrarOdontologo(ent1);

        assertNotNull(odontologoInsertado.getId());
        LOGGER.info("-----------------Fin Test-------------------------");
    }

    @Test
    void deberiaEncontrarElOdontologoConId1(){
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("    Test: deberiaEncontrarElOdontologoConId1      ");
        LOGGER.info("--------------------------------------------------");
        /*
        Odontologo ent1 = new Odontologo(50, "Pepito", "Perez");
        odontologoService.registrarOdontologo(ent1);

        Odontologo ent2 = new Odontologo(25, "Jacinto", "Ramirez");
        odontologoService.registrarOdontologo(ent2);
         */

        assertNotNull(odontologoService.buscarOdontologoPorId(1));
        LOGGER.info("-----------------Fin Test-------------------------");
    }

    @Test
    public void deberiaHaberUnaListaNoVacia(){
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("       Test: deberiaHaberUnaListaNoVacia          ");
        LOGGER.info("--------------------------------------------------");
        /*
        Odontologo ent1 = new Odontologo(50, "Pepito", "Perez");
        odontologoService.registrarOdontologo(ent1);

        Odontologo ent2 = new Odontologo(25, "Jacinto", "Ramirez");
        odontologoService.registrarOdontologo(ent2);
         */

        List<Odontologo> entidadesTest = odontologoService.listarTodosLosOdontologos();
        assertFalse(entidadesTest.isEmpty());
        assertTrue(entidadesTest.size() >= 1);
        LOGGER.info("-----------------Fin Test-------------------------");
    }

    @Test
    public void deberiaHaberUnaListaNoVaciaMemoria(){
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("   Test: deberiaHaberUnaListaNoVaciaMemoria       ");
        LOGGER.info("--------------------------------------------------");

        Odontologo ent1 = new Odontologo(50, "Pepito", "Perez");
        odontologoServiceMemoria.registrarOdontologo(ent1);

        Odontologo ent2 = new Odontologo(25, "Jacinto", "Ramirez");
        odontologoServiceMemoria.registrarOdontologo(ent2);

        List<Odontologo> entidadesTest = odontologoServiceMemoria.listarTodosLosOdontologos();
        assertFalse(entidadesTest.isEmpty());
        assertTrue(entidadesTest.size() >= 1);
        LOGGER.info("-----------------Fin Test-------------------------");
    }
}
