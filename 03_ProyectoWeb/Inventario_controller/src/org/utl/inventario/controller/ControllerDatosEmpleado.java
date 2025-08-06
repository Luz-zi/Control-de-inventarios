/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.controller;

import com.mysql.cj.jdbc.CallableStatement;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.utl.inventario.db.ConexionMySQL;
import org.utl.inventario.model.AsignacionEquipo;
import org.utl.inventario.model.Empleado;
import org.utl.inventario.model.Equipo;
import org.utl.inventario.model.Persona;

/**
 *
 * @author miche
 */
public class ControllerDatosEmpleado {
       public Empleado buscarPorNumeroEmpleado(String numeroEmpleado) throws Exception {
        Empleado empleado = null;

        ConexionMySQL conn = new ConexionMySQL();
        Connection con = conn.open();

        String query = "CALL sp_buscar_empleado_por_numeroEmpleado(?)";
           java.sql.CallableStatement cstmt = con.prepareCall(query);
        cstmt.setString(1, numeroEmpleado);


        
        
        ResultSet rs = cstmt.executeQuery();

        // 🔍 Siempre imprime los nombres de columnas, incluso si no hay datos
           java.sql.ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        System.out.println("Columnas recibidas:");
        for (int i = 1; i <= count; i++) {
            System.out.println("- " + meta.getColumnLabel(i));
        }

        // ⚠️ OJO: esto solo si hay datos
        if (rs.next()) {
            Persona persona = new Persona();
            persona.setId(rs.getInt("idPersona"));
           
            
            persona.setNombre(rs.getString("nombre"));
            persona.setApellido1(rs.getString("apellido1"));
            persona.setApellido2(rs.getString("apellido2"));
            persona.setTelefono(rs.getString("telefono"));
            persona.setCorreo(rs.getString("correo"));

            empleado = new Empleado();
            empleado.setId(rs.getInt("id"));

            // ⚠️ Asegúrate que coincide con el alias en el SP
            empleado.setNumeroEmpleado(rs.getString("numeroEmpleado")); // o "numero_empleado" si cambiaste el alias
            empleado.setActivo(rs.getInt("activo"));
            empleado.setPersona(persona);
        }

        rs.close();
        cstmt.close();
        con.close();

        return empleado;
    }

    
}
