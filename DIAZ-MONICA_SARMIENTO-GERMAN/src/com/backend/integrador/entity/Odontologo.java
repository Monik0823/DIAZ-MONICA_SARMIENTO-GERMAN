package com.backend.integrador.entity;

public class Odontologo {
    //Propiedades
    private int id;
    private int matricula;
    private String nombre;
    private String apellido;

    public Odontologo(int id, int matricula, String nombre, String apellido) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.matricula = matricula;
    }

    public Odontologo(int matricula, String nombre, String apellido) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.matricula = matricula;
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMatricula() {
        return this.matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "\nId: " + id + " - Odontologo: " + matricula +  " - " + nombre + " - " + apellido;
    }
}