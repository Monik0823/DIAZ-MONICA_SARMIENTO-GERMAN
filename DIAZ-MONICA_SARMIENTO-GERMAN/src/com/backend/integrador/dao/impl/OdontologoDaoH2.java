package com.backend.integrador.dao.impl;

import com.backend.integrador.dao.H2Connection;
import com.backend.integrador.dao.IDao;
import com.backend.integrador.entity.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OdontologoDaoH2 implements IDao<Odontologo> {
    private final Logger LOGGER = Logger.getLogger(OdontologoDaoH2.class);

    @Override
    public Odontologo registrar(Odontologo odontologo) {
        final String METODO = "<registrar> - ";
        Connection connection = null;
        String insert = "INSERT INTO ODONTOLOGOS (MATRICULA, NOMBRE, APELLIDO) VALUES (?, ?, ?)";
        Odontologo odontologo1 = null;

        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, odontologo.getMatricula());
            ps.setString(2, odontologo.getNombre());
            ps.setString(3, odontologo.getApellido());


            int regInsertados = ps.executeUpdate();

            LOGGER.info(METODO + "Registros insertados - " + regInsertados);

            connection.commit();

            odontologo1 = new Odontologo(odontologo.getMatricula(), odontologo.getNombre(), odontologo.getApellido());
            ResultSet key = ps.getGeneratedKeys();
            while (key.next()){
                odontologo1.setId(key.getInt(1));
            }
            LOGGER.info(METODO + "Odontologo guardado: " + odontologo1);

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
        return odontologo1;
    }

    @Override
    public Odontologo buscarPorId(int id) {
        final String METODO = "<buscarPorId> - ";
        Odontologo odontologo = null;

        Connection connection = null;
        try{
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ODONTOLOGOS WHERE ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            LOGGER.info(METODO + "Sentencia SELECT <<" + ps+ ">>");

            while (rs.next()){
                odontologo = crearObjetoOdontologo(rs);
            }

            if (odontologo == null){
                LOGGER.info(METODO + "No se ha encontrado el odontologo " + id);
            }
            else{
                LOGGER.info(METODO + "Se ha encontrado el odontologo " + odontologo);
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
        return odontologo;
    }

    @Override
    public List<Odontologo> listarTodos() {
        final String METODO = "<listarTodos> - ";
        Connection connection = null;
        List<Odontologo> odontologos = new ArrayList<>();
        try {
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ODONTOLOGOS");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                odontologos.add(crearObjetoOdontologo(rs));
            }
            LOGGER.info(METODO + "Listado de todos los odontologos: " + odontologos);

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
        return odontologos;
    }


    private Odontologo crearObjetoOdontologo(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        int matricula = rs.getInt("matricula");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");

        return new Odontologo(id, matricula, nombre, apellido);

    }
}