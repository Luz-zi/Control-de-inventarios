/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.model;

import java.util.Date;

/**
 *
 * @author miche
 */
public class Baja {
    private int id;
    private Equipo equipo;
    private Empleado empleado;
    private Date fechaBaja;
    private String motivo;
    private String Observaciones;

    public Baja() {
    }

    public Baja(int id, Equipo equipo, Empleado empleado, Date fechaBaja, String motivo, String Observaciones) {
        this.id = id;
        this.equipo = equipo;
        this.empleado = empleado;
        this.fechaBaja = fechaBaja;
        this.motivo = motivo;
        this.Observaciones = Observaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }
    
    
}
