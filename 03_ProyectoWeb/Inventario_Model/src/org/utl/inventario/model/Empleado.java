/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.model;

/**
 *
 * @author miche
 */
public class Empleado {

    private int id;
    private Persona persona;
    private Usuario usuario;
    private String numeroEmpleado;
    private String campus;
    private String edificio;
    private int activo;

    public Empleado() {
    }

    public Empleado(int id, Persona persona, Usuario usuario, String numeroEmpleado, String campus, String edificio, int activo) {
        this.id = id;
        this.persona = persona;
        this.usuario = usuario;
        this.numeroEmpleado = numeroEmpleado;
        this.campus = campus;
        this.edificio = edificio;
        this.activo = activo;
    }

    public Empleado(int id, Persona persona, Usuario usuario, String numeroEmpleado, int activo) {
        this.id = id;
        this.persona = persona;
        this.usuario = usuario;
        this.numeroEmpleado = numeroEmpleado;
        this.activo = activo;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

}
