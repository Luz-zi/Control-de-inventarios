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
public class AsignacionEquipo {

    private int id;
    private Equipo equipo;
    private Empleado empleado;
    private Date FechaAsignacion;
    private Date FechaFin;
    private int activo;

    public AsignacionEquipo() {
    }

    public AsignacionEquipo(int id, int activo, Equipo equipo, Empleado empleado, Date FechaAsignacion, Date FechaFin) {
        this.id = id;
        this.activo = activo;
        this.equipo = equipo;
        this.empleado = empleado;
        this.FechaAsignacion = FechaAsignacion;
        this.FechaFin = FechaFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
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

    public Date getFechaAsignacion() {
        return FechaAsignacion;
    }

    public void setFechaAsignacion(Date FechaAsignacion) {
        this.FechaAsignacion = FechaAsignacion;
    }

    public Date getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(Date FechaFin) {
        this.FechaFin = FechaFin;
    }

}
