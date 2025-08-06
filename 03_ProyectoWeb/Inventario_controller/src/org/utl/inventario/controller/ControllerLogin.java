/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.controller;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.utl.inventario.db.ConexionMySQL;

import org.utl.inventario.model.Usuario;

public class ControllerLogin {

    public void almacenarToken(Usuario usuario) throws Exception {
    String query = "UPDATE usuario SET token = ? WHERE cve_usuario = ?";

    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection conn = connMySQL.open();
    PreparedStatement pstmt = conn.prepareStatement(query);

    pstmt.setString(1, usuario.getToken());
    pstmt.setInt(2, usuario.getId()); // <-- usar el ID único, no usuario + contraseña

    int rowsAffected = pstmt.executeUpdate();

    pstmt.close();
    conn.close();
    connMySQL.close();

    if (rowsAffected > 0) {
        System.out.println("✅ Token actualizado para: " + usuario.getUsuario());
    } else {
        System.out.println("⚠️ No se actualizó el token. Verifica ID del usuario.");
    }
}



//COPEA TOKEN DE ELIMINAR
    public void eliminarToken(String token) throws Exception {
        System.out.println("Token eliminado");
        String query = """
 UPDATE usuario SET token='' WHERE token='%s';
 """;
        query = String.format(query, token);
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(query);
        cstmt.execute(query);
        cstmt.close();
        conn.close();
        connMySQL.close();
    }

   
    public boolean validarToken(String token) throws Exception {
        boolean respuesta = false;
        System.out.println("Entro al servicio validar token");
        String query = "SELECT * FROM usuario WHERE token='" + token + "';";
        System.out.println(token);
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(query);
        ResultSet rs = cstmt.executeQuery(query);
        if (rs.next()) {
            respuesta = true;
        }
        rs.close();
        cstmt.close();
        conn.close();
        connMySQL.close();

        return respuesta;
    }

    public String toJson(Usuario usuario) {
        // Creamos una instancia de Gson
        Gson gson = new Gson();

        // Convertimos el objeto UsuarioLucero a una cadena JSON
        return gson.toJson(usuario);
    }

    public Usuario ingresar(Usuario usuario) throws Exception {
        System.out.println("Llego al servicio de login con nombre completo");

        // Consulta con JOINs para obtener también el nombre completo
        String sql = """
        SELECT 
            u.cve_usuario, u.usuario, u.rol, u.token,u.activo,
            p.nombre, p.apellido1, p.apellido2
        FROM usuario u
        JOIN empleado e ON u.cve_usuario = e.cve_usuario
        JOIN persona p ON e.cve_persona = p.cve_persona
        WHERE u.usuario = ? AND u.contrasenia = ? AND u.activo = 1;
    """;

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, usuario.getUsuario());
        pstmt.setString(2, usuario.getContrasenia());

        ResultSet rs = pstmt.executeQuery();

        Usuario u = null;

        if (rs.next()) {
            u = new Usuario();
            u.setId(rs.getInt("cve_usuario"));
            u.setUsuario(rs.getString("usuario"));
            u.setRol(rs.getString("rol"));
            u.setToken(rs.getString("token"));

            // Aquí armamos el nombre completo
            String nombreCompleto = rs.getString("nombre") + " "
                    + rs.getString("apellido1") + " "
                    + rs.getString("apellido2");
            u.setNombreEmpleado(nombreCompleto); 
            System.out.println("Bienvenido " + nombreCompleto);
            almacenarToken(u);
        } else {
            System.out.println("Credenciales incorrectas.");
        }

        rs.close();
        pstmt.close();
        conn.close();
        connMySQL.close();

        return u;
    }

    public boolean getToken() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static class Gson {

        public Gson() {
        }

        private String toJson(Usuario usuario) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

    }

}
