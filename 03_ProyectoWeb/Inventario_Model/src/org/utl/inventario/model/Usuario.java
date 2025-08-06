/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.model;

import java.util.Date;
import org.apache.commons.codec.digest.DigestUtils;

public class Usuario {
    private int id;
    private String usuario;
    private String rol;
    private String token;
    private String contrasenia;
    private int activo;
    private String nombreEmpleado;

    public Usuario() {
    }

    public Usuario(int id, String usuario, String rol, String token, String contrasenia, int activo) {
        this.id = id;
        this.usuario = usuario;
        this.rol = rol;
        this.token = token;
        this.contrasenia = contrasenia;
        this.activo = activo;
    }

    public Usuario(String usuario, String contrasenia) {
        this.usuario = usuario;
        this.contrasenia = contrasenia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public void setToken() {
        String p1 = this.usuario;
        String p2 = "Clave";
        Date fecha = new Date();
        String p3 = fecha.toString();
        String cadena = p1 + ":" + p2 + ":" + p3;
        String t = DigestUtils.md5Hex(cadena);
        this.token = t;
    }

}
