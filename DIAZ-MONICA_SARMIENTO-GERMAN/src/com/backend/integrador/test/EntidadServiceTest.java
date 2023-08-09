package com.backend.integrador.test;

import com.backend.integrador.dao.impl.EntidadDaoH2;
import com.backend.integrador.entity.Entidad;
import com.backend.integrador.service.EntidadService;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class EntidadServiceTest {

    private static Connection connection = null;
    private static final Logger LOGGER = Logger.getLogger(EntidadServiceTest.class);

    @BeforeAll
    static void init(){
        LOGGER.info("Creación de tabla de la entidad");
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/parcialBack;INIT=RUNSCRIPT FROM 'create.sql'", "sa", "sa");
        } catch (Exception e){
            LOGGER.error("Se genera error creando la tabla de la entidad");
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
    //seteamos la implementacion de la IDao que queremos que se use, sea H2, en memoria, MySql, o la que tengamos implementada

    EntidadService entidadService = new EntidadService(new EntidadDaoH2());


    @Test
    void deberiaAgregarUnEntidad(){
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("           Test: deberiaAgregarUnEntidad          ");
        LOGGER.info("--------------------------------------------------");
        Entidad ent1 = new Entidad("Perez", "Pepito", 50);
        Entidad entidadInsertado = entidadService.registrarEntidad(ent1);

        assertNotNull(entidadInsertado.getId());
        LOGGER.info("-----------------Fin Test-------------------------");
    }

    @Test
    void deberiaEncontrarElEntidadConId1(){
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("     Test: deberiaEncontrarElEntidadConId1        ");
        LOGGER.info("--------------------------------------------------");
        Entidad ent1 = new Entidad("Perez", "Pepito", 50);
        entidadService.registrarEntidad(ent1);

        Entidad ent2 = new Entidad("Ramirez", "Jacinto", 25);
        entidadService.registrarEntidad(ent2);

        assertNotNull(entidadService.buscarEntidadPorId(1));
        LOGGER.info("-----------------Fin Test-------------------------");
    }

    @Test
    public void deberiaHaberUnaListaNoVacia(){
        LOGGER.info("--------------------------------------------------");
        LOGGER.info("       Test: deberiaHaberUnaListaNoVacia          ");
        LOGGER.info("--------------------------------------------------");
        Entidad ent1 = new Entidad("Perez", "Pepito", 50);
        entidadService.registrarEntidad(ent1);

        Entidad ent2 = new Entidad("Ramirez", "Jacinto", 25);
        entidadService.registrarEntidad(ent2);

        List<Entidad> entidadesTest = entidadService.listarTodosLosEntidades();
        assertFalse(entidadesTest.isEmpty());
        assertTrue(entidadesTest.size() >= 1);
        LOGGER.info("-----------------Fin Test-------------------------");
    }
}
