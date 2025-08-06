/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.model;

/**
 *
 * @author miche
 */
public class Equipo {
    private int id;
    private String nombre;
    private String edificio;
    private String departamento;
    private String marca;
    private String modelo;
    private String ip;
    private String mac; 
    private String numSerie;
    private String numUtl;
    private String capacidad;
    private String ram;
    private String cpu;
    private String estado;
    private String mantenimiento;
     private String descripcionEquipo;
    private int activo;

    public Equipo() {
    }

    public Equipo(int id, String nombre, String marca,  String edificio,  String departamento,  String modelo, String ip, String mac, String numSerie, String numUtl, String capacidad, String ram,  String cpu, String estado, String mantenimiento, String descripcionEquipo, int activo) {
        this.id = id;
        this.nombre = nombre;
        this.edificio = edificio;
        this.departamento = departamento;
        this.marca = marca;
        this.modelo = modelo;
        this.ip = ip;
        this.mac = mac;
        this.numSerie = numSerie;
        this.numUtl = numUtl;
        this.capacidad = capacidad;
        this.ram = ram;
        this.cpu = cpu;
        this.estado = estado;
        this.mantenimiento = mantenimiento;
        this.descripcionEquipo = descripcionEquipo;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getNumUtl() {
        return numUtl;
    }

    public void setNumUtl(String numUtl) {
        this.numUtl = numUtl;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(String mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public String getDescripcionEquipo() {
        return descripcionEquipo;
    }

    public void setDescripcionEquipo(String descripcionEquipo) {
        this.descripcionEquipo = descripcionEquipo;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }
    
    
       
}
