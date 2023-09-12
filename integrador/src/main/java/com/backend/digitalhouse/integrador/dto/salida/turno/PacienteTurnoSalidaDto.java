package com.backend.digitalhouse.integrador.dto.salida.turno;

public class PacienteTurnoSalidaDto {
    private Long id;
    private String nombre;
    private String apellido;

    public PacienteTurnoSalidaDto() {
    }

    public PacienteTurnoSalidaDto(Long id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String toString(){
        return  "\n" + "{" + "\n" +
                "  Id: " + this.id + "\n" +
                "  Nombre: " + this.nombre + "\n" +
                "  Apellido: " + this.apellido + "\n" +
                "}"
                ;
    }
}
