/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.controller;

import java.sql.ResultSet;
import java.sql.Connection;
import org.utl.inventario.db.ConexionMySQL;
import org.utl.inventario.model.AsignacionEquipo;
import org.utl.inventario.db.ConexionMySQL;
import org.utl.inventario.model.AsignacionEquipo;

/**
 *
 * @author sando
 */
public class ControllerAsignacionEquipo {

    public int save(AsignacionEquipo asignacion) throws Exception {
        String sql = "{CALL sp_insertar_asignacion_equipo(?, ?, ?, ?, ?)}";

        // Conexión a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = (Connection) connMySQL.open();

        // Crear CallableStatement
        java.sql.CallableStatement cstmt = conn.prepareCall(sql);

        // Parámetros de entrada
        cstmt.setInt(1, asignacion.getEquipo().getId());
        cstmt.setInt(2, asignacion.getEmpleado().getId());

        // Fecha asignación (si viene null, el SP usará NOW())
        if (asignacion.getFechaAsignacion() != null) {
            cstmt.setTimestamp(3, new java.sql.Timestamp(asignacion.getFechaAsignacion().getTime()));
        } else {
            cstmt.setNull(3, java.sql.Types.TIMESTAMP);
        }

        // Fecha fin puede ser null
        if (asignacion.getFechaFin() != null) {
            cstmt.setTimestamp(4, new java.sql.Timestamp(asignacion.getFechaFin().getTime()));
        } else {
            cstmt.setNull(4, java.sql.Types.TIMESTAMP);
        }

        cstmt.setInt(5, asignacion.getActivo());

        // Ejecutar y obtener el ID generado (devuelto por el SELECT LAST_INSERT_ID())
        boolean hasResultSet = cstmt.execute();

        int idGenerado = 0;
        if (hasResultSet) {
            try ( ResultSet rs = cstmt.getResultSet()) {
                if (rs.next()) {
                    idGenerado = rs.getInt("idAsignacion");
                    asignacion.setId(idGenerado);
                }
            }
        }

        cstmt.close();
        connMySQL.close();

        return idGenerado;
    }
}
