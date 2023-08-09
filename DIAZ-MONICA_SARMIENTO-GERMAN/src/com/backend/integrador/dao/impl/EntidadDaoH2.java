package com.backend.integrador.dao.impl;

import com.backend.integrador.dao.H2Connection;
import com.backend.integrador.dao.IDao;
import com.backend.integrador.entity.Entidad;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.h2.message.Trace.JDBC;


public class EntidadDaoH2 implements IDao<Entidad> {
    private final Logger LOGGER = Logger.getLogger(EntidadDaoH2.class);

    @Override
    public Entidad registrar(Entidad entidad) {
        final String METODO = "<registrar> - ";
        Connection connection = null;
        String insert = "INSERT INTO ENTIDADES (APELLIDO, NOMBRE, MATRICULA) VALUES (?, ?, ?)";
        Entidad entidad1 = null;

        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, entidad.getApellido());
            ps.setString(2, entidad.getNombre());
            ps.setInt(3, entidad.getMatricula());

            int regInsertados = ps.executeUpdate();

            LOGGER.info(METODO + "Registros insertados - " + regInsertados);

            connection.commit();
            //select a la base de datos para obtener lo que se registro mas su id

            entidad1 = new Entidad(entidad.getApellido(), entidad.getNombre(), entidad.getMatricula());
            ResultSet key = ps.getGeneratedKeys();
            while (key.next()){
                entidad1.setId(key.getInt(1));
            }
            LOGGER.info(METODO + "Entidad guardada: " + entidad1);

        } catch (Exception e) {
            LOGGER.error(METODO + "Error ejecutando sentencia");
            LOGGER.error(METODO + e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback();
                    LOGGER.error(METODO + "Se ejecuta rollback");
                    LOGGER.error(METODO + e.getMessage());
                } catch (SQLException exception) {
                    LOGGER.error(METODO + "Error al ejecutar rollback");
                    LOGGER.error(METODO + exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error(METODO + "No se pudo cerrar la conexión: " + ex.getMessage());
            }
        }
        return entidad1;
    }

    @Override
    public Entidad buscarPorId(int id) {
        final String METODO = "<buscarPorId> - ";
        Entidad entidad = null;

        Connection connection = null;
        try{
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ENTIDADES WHERE ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            LOGGER.info(METODO + "Sentencia SELECT <<" + ps+ ">>");

            while (rs.next()){
                entidad = crearObjetoEntidad(rs);
            }

            if (entidad == null){
                LOGGER.info(METODO + "No se ha encontrado la entidad " + id);
            }
            else{
                LOGGER.info(METODO + "Se ha encontrado la entidad " + entidad);
            }

        } catch (Exception e) {
            LOGGER.error(METODO + "Error ejecutando sentencia");
            LOGGER.error(METODO + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error(METODO + "No se pudo cerrar la conexión: " + ex.getMessage());
            }
        }
        return entidad;
    }

    @Override
    public List<Entidad> listarTodos() {
        final String METODO = "<listarTodos> - ";
        Connection connection = null;
        List<Entidad> entidades = new ArrayList<>();
        try {
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ENTIDADES");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                entidades.add(crearObjetoEntidad(rs));
            }
            LOGGER.info(METODO + "Listado de todos las entidades: " + entidades);

        } catch (Exception e) {
            LOGGER.error(METODO + "Error ejecutando sentencia");
            LOGGER.error(METODO + e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error(METODO + "No se pudo cerrar la conexión: " + ex.getMessage());
            }
        }
        return entidades;
    }


    private Entidad crearObjetoEntidad(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String apellido = rs.getString("apellido");
        String nombre = rs.getString("nombre");
        int matricula = rs.getInt("matricula");

        return new Entidad(id, apellido, nombre, matricula);

    }
}