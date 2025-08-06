/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {

    private Connection conn;

    public Connection open() throws Exception {
        String ruta = "jdbc:mysql://localhost:3306/Inventario";
        String usuario = "root";
        String password = "Lucero2117";

        //Registramos el Driver de MySQL para que este disponible
        //y nos podamos conectar con MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");

        //Abrimos la conexión con MySQL 
        
         conn = DriverManager.getConnection(ruta, usuario, password);
        
       

        //Devolvemos la conexion 
        return conn;
    }

    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
        
    }
/*
    public void cerrar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/
}
