/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.controller;

import com.mysql.cj.jdbc.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.utl.inventario.db.ConexionMySQL;
import org.utl.inventario.model.AsignacionEquipo;
import org.utl.inventario.model.Baja;
import org.utl.inventario.model.Empleado;
import org.utl.inventario.model.Equipo;
import org.utl.inventario.model.Persona;

/**
 *
 * @author sando
 */
public class ControllerDatosEquipo {
    
    public int save(Baja baja) throws Exception {
    // Consulta SQL actualizada con 5 parámetros, el 5° será el OUT (id)
    String sql = "{CALL sp_insertar_baja(?, ?, ?, ?, ?)}";

    // Conexión a la base de datos
    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();

    // Crear CallableStatement
    CallableStatement cstmt = (CallableStatement) conn.prepareCall(sql);

    // Establecer los parámetros de entrada
    cstmt.setInt(1, baja.getEquipo().getId());
    cstmt.setInt(2, baja.getEmpleado().getId());
    cstmt.setString(3, baja.getMotivo());
    cstmt.setString(4, baja.getObservaciones());

    // Registrar el parámetro de salida (id generado)
    cstmt.registerOutParameter(5, java.sql.Types.INTEGER);

    // Ejecutar el procedimiento
    cstmt.executeUpdate();

    // Recuperar el ID generado y asignarlo al objeto
    int idGenerado = cstmt.getInt(5);
    baja.setId(idGenerado);

    // Cerrar recursos
    cstmt.close();
    connMySQL.close();

    // Devolver el ID de la baja recién insertada
    return idGenerado;
}


    public List<Baja> getAll() throws Exception {
    List<Baja> bajas = new ArrayList<>();

    String sql = "SELECT * FROM v_baja";

        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        
        ResultSet rs = pstmt.executeQuery();

    while (rs.next()) {
        Baja baja = new Baja();

        // Datos de la baja
        baja.setId(rs.getInt("cve_baja"));
        baja.setFechaBaja(rs.getTimestamp("fecha_baja"));
        baja.setMotivo(rs.getString("motivo"));
        baja.setObservaciones(rs.getString("observaciones"));

        // Datos del equipo
        Equipo equipo = new Equipo();
        equipo.setId(rs.getInt("cve_equipo"));
        equipo.setNombre(rs.getString("nombre_equipo"));
        equipo.setMarca(rs.getString("marca"));
        equipo.setModelo(rs.getString("modelo"));
        equipo.setNumSerie(rs.getString("numero_serie"));
        equipo.setNumUtl(rs.getString("numero_utl"));

        // Datos del empleado
        Empleado emp = new Empleado();
        emp.setId(rs.getInt("cve_empleado"));
        emp.setNumeroEmpleado(rs.getString("numero_empleado"));

        Persona p = new Persona();
        p.setNombre(rs.getString("nombre"));
        p.setApellido1(rs.getString("apellido1"));
        p.setApellido2(rs.getString("apellido2"));
        p.setCorreo(rs.getString("correo"));

        // Relacionamos objetos
        emp.setPersona(p);
        baja.setEmpleado(emp);
        baja.setEquipo(equipo);

        bajas.add(baja);
    }

    rs.close();
    pstmt.close();
    connMySQL.close();

    return bajas;
}

    
   

}
