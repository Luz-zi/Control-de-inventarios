/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.utl.inventario.controller;

import java.util.List;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.utl.inventario.db.ConexionMySQL;
import org.utl.inventario.model.AsignacionEquipo;
import org.utl.inventario.model.Empleado;
import org.utl.inventario.model.Equipo;
import org.utl.inventario.model.Persona;

/**
 *
 * @author miche
 */
public class ControllerEquipo {

    public int insert(AsignacionEquipo ae) throws Exception {
        // Se define la consulta SQL:
        String sql = "{CALL sp_insertar_equipo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, ae.getEquipo().getNombre());

        cstmt.setString(2, ae.getEquipo().getEdificio());
        cstmt.setString(3, ae.getEquipo().getDepartamento());
        cstmt.setString(4, ae.getEquipo().getMarca());
        cstmt.setString(5, ae.getEquipo().getModelo());
        cstmt.setString(6, ae.getEquipo().getIp());
        cstmt.setString(7, ae.getEquipo().getMac());
        cstmt.setString(8, ae.getEquipo().getNumSerie());
        cstmt.setString(9, ae.getEquipo().getNumUtl());
        cstmt.setString(10, ae.getEquipo().getCapacidad());
        cstmt.setString(11, ae.getEquipo().getRam());
        cstmt.setString(12, ae.getEquipo().getCpu());
        cstmt.setString(13, ae.getEquipo().getEstado());
        cstmt.setString(14, ae.getEquipo().getMantenimiento());
        cstmt.setString(15, ae.getEquipo().getDescripcionEquipo());
        cstmt.setInt(16, ae.getEquipo().getActivo());
        cstmt.setInt(17, ae.getEmpleado().getId());
        // Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();
        // Recuperamos el ID de Producto y de Alimento generados:
        ae.getEquipo().setId(cstmt.getInt(18));
        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();

        // Devolvemos el ID de Alimento que se genero:
        return ae.getId();

    }

   
    
    public int update(AsignacionEquipo ae) throws Exception {
        // Se define la consulta SQL:
        String sql = "{CALL sp_actualizar_equipo(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        // Generamos un CallableStatement para invocar al Stored Procedure:
        CallableStatement cstmt = conn.prepareCall(sql);
        // Colocamos los valores de los parametros de entrada que requiere
        // el Stored Procedure:
        cstmt.setString(1, ae.getEquipo().getNombre());
        cstmt.setString(2, ae.getEquipo().getEdificio());
        cstmt.setString(3, ae.getEquipo().getDepartamento());
        cstmt.setString(4, ae.getEquipo().getMarca());
        cstmt.setString(5, ae.getEquipo().getIp());
        cstmt.setString(6, ae.getEquipo().getMac());
        cstmt.setString(7, ae.getEquipo().getModelo());
        cstmt.setString(8, ae.getEquipo().getNumSerie());
        cstmt.setString(9, ae.getEquipo().getNumUtl());
        cstmt.setString(10, ae.getEquipo().getCapacidad());
        cstmt.setString(11, ae.getEquipo().getRam());
        cstmt.setString(12, ae.getEquipo().getCpu());
        cstmt.setString(13, ae.getEquipo().getEstado());
        cstmt.setString(14, ae.getEquipo().getMantenimiento());
        cstmt.setString(15, ae.getEquipo().getDescripcionEquipo());
        cstmt.setInt(16, ae.getEquipo().getActivo());

        if (ae.getEmpleado() != null && ae.getEmpleado().getId() > 0) {
            cstmt.setInt(17, ae.getEmpleado().getId());
        } else {
            cstmt.setNull(17, java.sql.Types.INTEGER);
        }

        cstmt.setInt(18, ae.getEquipo().getId());
        // Ejecutamos el Stored Procedure:
        cstmt.executeUpdate();

        //Cerramos los objetos de conexion:
        cstmt.close();
        connMySQL.close();

        // Devolvemos el ID de Alimento que se genero:
        return ae.getId();

    }

    public void delete(int id) throws Exception {
        // Se define la consulta SQL:
        String sql = "UPDATE producto SET activo=0 WHERE idProducto=?";

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

    public List<AsignacionEquipo> getAll(String filtro) throws Exception {
        List<AsignacionEquipo> equipos = new ArrayList<>();
        // Se define la consulta SQL:
        String sql = "SELECT * FROM vista_equipo_completo;";

        // Abrimos la conexion con la BD:
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        ResultSet rs = pstmt.executeQuery();

        AsignacionEquipo equipo = null;

        // Recorremos cada registro devuelto por la consulta:
        while (rs.next()) {

            equipo = fill(rs);
            equipos.add(equipo);
        }

        rs.close();
        pstmt.close();
        connMySQL.close();

        return equipos;
    }

    private AsignacionEquipo fill(ResultSet rs) throws Exception {
        Equipo e = new Equipo();
        Empleado emp = new Empleado();
        Persona p = new Persona();
        AsignacionEquipo ae = new AsignacionEquipo();

        //Asignar los objetos relacionados a equipos
        ae.setEquipo(e);
        ae.setEmpleado(emp);
        emp.setPersona(p);

        // Establecemos los valores de cada atributo de
        // los objetos relacionados, extraidos de cada
        // campo del ResultSet:
        e.setId(rs.getInt("cve_equipo"));
        e.setNombre(rs.getString("nombre_equipo"));

        e.setEdificio(rs.getString("edificio"));
        e.setDepartamento(rs.getString("departamento"));

        e.setMarca(rs.getString("marca"));
        e.setIp(rs.getString("direccion_ip"));
        e.setMac(rs.getString("direccion_mac"));
        e.setModelo(rs.getString("modelo"));
        e.setNumSerie(rs.getString("numero_serie"));
        e.setNumUtl(rs.getString("numero_utl"));
        e.setCapacidad(rs.getString("capacidad"));
        e.setRam(rs.getString("ram"));

        e.setCpu(rs.getString("cpu"));
        e.setEstado(rs.getString("estado"));

        e.setMantenimiento(rs.getString("periocidad_mantenimiento"));
         e.setDescripcionEquipo(rs.getString("descripcionEquipo"));
        e.setActivo(rs.getInt("equipo_activo"));

        ae.setId(rs.getInt("cve_asignacion"));
        ae.setFechaAsignacion(rs.getDate("fecha_asignacion"));
        ae.setFechaFin(rs.getDate("fecha_fin"));
        ae.setActivo(rs.getInt("asignacion_activa"));

        emp.setId(rs.getInt("cve_empleado"));
        emp.setNumeroEmpleado(rs.getString("numero_empleado"));
        p.setNombre(rs.getString("nombre_empleado"));
        p.setApellido1(rs.getString("apellido1"));
        p.setApellido2(rs.getString("apellido2"));
        p.setCorreo(rs.getString("correo"));
        p.setTelefono(rs.getString("telefono"));
        return ae;
    }

}
