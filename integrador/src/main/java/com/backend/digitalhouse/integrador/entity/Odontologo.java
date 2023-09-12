package com.backend.digitalhouse.integrador.entity;

import javax.persistence.*;

@Entity
@Table(name = "ODONTOLOGOS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"matricula"})
})
public class Odontologo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int matricula;

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    public Odontologo() {
    }

    public Odontologo(int matricula, String nombre, String apellido) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return "Id: " + id + " - Nombre: " + nombre + " - Apellido: " + apellido + " - Matricula: " + matricula;
    }
}
