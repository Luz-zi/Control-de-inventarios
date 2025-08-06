/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.utl.inventario.db.ConexionMySQL;
import org.utl.inventario.model.Empleado;
import org.utl.inventario.model.Persona;
import org.utl.inventario.model.Usuario;

public class ControllerEmpleado {

    public int insert(Empleado e) throws Exception {
        // Se define la consulta SQL:
        String sql = "{CALL insertarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);

        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, e.getNumeroEmpleado());
        cstmt.setString(2, e.getPersona().getNombre());
        cstmt.setString(3, e.getPersona().getApellido1());
        cstmt.setString(4, e.getPersona().getApellido2());
        cstmt.setString(5, e.getCampus());
        cstmt.setString(6, e.getEdificio());
        cstmt.setString(7, e.getPersona().getCorreo());
        cstmt.setString(8, e.getUsuario().getUsuario());
        cstmt.setString(9, e.getUsuario().getContrasenia());
        cstmt.setString(10, e.getUsuario().getRol());

        cstmt.setInt(11, e.getPersona().getId());
        cstmt.setInt(12, e.getUsuario().getId());
        cstmt.setInt(13, e.getId());
        // e.setId(cstmt.getInt(4));
        // Ejecutamos el Stored Procedure:

        cstmt.executeUpdate();

        // Recuperamos el ID de Persona y de Alumno generados:
        e.getPersona().setId(cstmt.getInt(11));
        e.getUsuario().setId(cstmt.getInt(12));
        e.setId(cstmt.getInt(13));

        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();

        return e.getId();
    }

    public void update(Empleado e) throws Exception {
        String sql = "{CALL actualizarEmpleado(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, e.getPersona().getNombre());
        cstmt.setString(2, e.getPersona().getApellido1());
        cstmt.setString(3, e.getPersona().getApellido2());
        cstmt.setString(4, e.getPersona().getCorreo());

        cstmt.setString(5, e.getUsuario().getUsuario());
        cstmt.setString(6, e.getUsuario().getContrasenia());
        cstmt.setString(7, e.getUsuario().getRol());

        cstmt.setString(8, e.getCampus());
        cstmt.setString(9, e.getEdificio());
        
        
        cstmt.setInt(10, e.getPersona().getId());
        cstmt.setInt(11, e.getUsuario().getId());
        cstmt.setInt(12, e.getId());

        cstmt.executeUpdate();

        cstmt.close();
        connMySQL.close();
    }

    public void delete(int id) throws Exception {
         if (id <= 0) {
throw new IllegalArgumentException("ID de usuario inválido para eliminar");
             
    }
        // Se define la consulta SQL:
        String sql = "UPDATE Usuario SET activo=0 WHERE cve_usuario=?";

        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Llenamos los datos del PreparedStatement:
        pstmt.setInt(1, id);

        // Ejecutamos la consulta:
        pstmt.executeUpdate();

        // Cerramos los objetos de conexion:
        pstmt.close();
        connMySQL.close();
    }

    public List<Empleado> getAll(String filtro) throws Exception {
        List<Empleado> empleados = new ArrayList<>();
        // Se define la consulta SQL:
        String sql = "SELECT * FROM v_empleado";

        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        Empleado emp = null;

        // Recorremos cada registro devuelto por la consulta:
        while (rs.next()) {
            emp = fill(rs);
            empleados.add(emp);
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return empleados;
    }

    private Empleado fill(ResultSet rs) throws Exception {
        Empleado e = new Empleado();
        Persona p = new Persona();
        Usuario u = new Usuario();

        //Asignar los objetos relacionados a empleados
        e.setPersona(p);
        e.setUsuario(u);

        // Establecemos los valores de cada atributo de
        // los objetos relacionados, extraidos de cada
        // campo del ResultSet:
        // Asignar los valores de los atributos de Empleados desde ResultSet
        e.setId(rs.getInt("cve_empleado"));
        e.setNumeroEmpleado(rs.getString("numero_empleado"));
        e.setCampus(rs.getString("campus"));
        e.setEdificio(rs.getString("edificio"));
        
        e.setActivo(rs.getInt("empleadoActivo"));

        // Asignar los valores de los atributos de Persona desde ResultSet
        p.setId(rs.getInt("cve_persona"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido1(rs.getString("apellido1"));
        p.setApellido2(rs.getString("apellido2"));
        p.setCorreo(rs.getString("correo"));

        // Asignar los valores de los atributos de Usuario desde ResultSet
        u.setId(rs.getInt("cve_usuario"));
        u.setUsuario(rs.getString("nombreUsuario"));
        u.setContrasenia(rs.getString("contrasenia"));
        
        u.setRol(rs.getString("rol"));
        u.setActivo(rs.getInt("activo"));

        return e;
    }
}
