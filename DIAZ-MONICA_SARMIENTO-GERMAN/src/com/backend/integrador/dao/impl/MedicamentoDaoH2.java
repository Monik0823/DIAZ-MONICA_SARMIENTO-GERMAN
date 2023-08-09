package com.backend.integrador.dao.impl;

import com.backend.integrador.dao.H2Connection;
import com.backend.integrador.dao.IDao;
import com.backend.integrador.entity.Medicamento;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MedicamentoDaoH2 implements IDao<Medicamento> {
    private final Logger LOGGER = Logger.getLogger(MedicamentoDaoH2.class);

    @Override
    public Medicamento registrar(Medicamento medicamento) {
        Connection connection = null;
        String insert = "INSERT INTO MEDICAMENTOS (CODIGO, NOMBRE, LABORATORIO, CANTIDAD, PRECIO) VALUES (?, ?, ?, ?, ?)";
        Medicamento medicamento1 = null;

        try{
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, medicamento.getCodigo());
            ps.setString(2, medicamento.getNombre());
            ps.setString(3, medicamento.getLaboratorio());
            ps.setInt(4, medicamento.getCantidad());
            ps.setDouble(5, medicamento.getPrecio());
            ps.execute();

            connection.commit();

            //select a la base de datos para obtener lo que se registro mas su id
            medicamento1 = new Medicamento(medicamento.getCodigo(), medicamento.getNombre(), medicamento.getLaboratorio(), medicamento.getCantidad(), medicamento.getPrecio());
            ResultSet key = ps.getGeneratedKeys();
            while (key.next()){
                medicamento1.setId(key.getInt(1));
            }
            LOGGER.info("Medicamento guardado: " + medicamento1);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Tuvimos un problema");
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("No se pudo cerrar la conexion: " + ex.getMessage());
            }
        }


        return medicamento1;
    }

    @Override
    public Medicamento buscarPorId(int id) {
        Medicamento medicamento = null;

        Connection connection = null;
        try{
            connection = H2Connection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM MEDICAMENTOS WHERE ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                medicamento = crearObjetoMedicamento(rs);

            }
            LOGGER.info("Se ha encontrado el medicamento " + medicamento);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. " + ex.getMessage());
                ex.printStackTrace();
            }
        }


        return medicamento;
    }

    @Override
    public List<Medicamento> listarTodos() {
        Connection connection = null;
        List<Medicamento> medicamentos = new ArrayList<>();
        try {
            connection = H2Connection.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM MEDICAMENTOS");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                medicamentos.add(crearObjetoMedicamento(rs));
            }
            connection.commit();
            LOGGER.info("Listado de todos los medicamentos: " + medicamentos);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                    System.out.println("Tuvimos un problema");
                    e.printStackTrace();
                } catch (SQLException exception) {
                    LOGGER.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
        } finally {
            try {
                connection.close();
            } catch (Exception ex) {
                LOGGER.error("Ha ocurrido un error al intentar cerrar la bdd. " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return medicamentos;
    }


    private Medicamento crearObjetoMedicamento(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        int codigo = rs.getInt("codigo");
        String laboratorio = rs.getString("laboratorio");
        int cantidad = rs.getInt("cantidad");
        double precio = rs.getDouble("precio");

        return new Medicamento(id, codigo, nombre, laboratorio, cantidad, precio);

    }
}