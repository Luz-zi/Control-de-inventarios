/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.inventario.controller.ControllerLogin;
import org.utl.inventario.model.Usuario;

/**
 *
 * @author sando
 */
@Path("login")
public class RESTLogin {

    @Path("ingresar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)

    public Response ingresar(
            @FormParam("usuario") @DefaultValue("0") String usuario,
            @FormParam("contrasenia") @DefaultValue("0") String contrasenia
    ) {
        String out;
        System.out.println("Datos de login: " + usuario + contrasenia);

        Usuario u = new Usuario(usuario, contrasenia);

        try {
            ControllerLogin cl = new ControllerLogin();

            Usuario resultado = cl.ingresar(u); // devuelve objeto Usuario con nombreEmpleado

            if (resultado != null) {
                resultado.setToken(); // Genera nuevo token
                cl.almacenarToken(resultado); // Guarda token en BD

                // Convierte el objeto completo a JSON
                Gson gson = new Gson();
                out = gson.toJson(resultado);

                System.out.println("Token generado: " + resultado.getToken());
                System.out.println("ROL: " + resultado.getRol());
            } else {
                out = """
                  {"error":"Usuario o contraseña incorrectos."}
                  """;
                return Response.status(Response.Status.UNAUTHORIZED).entity(out).build();
            }

        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = """
              {"error":"El JSON recibido no es correcto."}
              """;
        } catch (Exception e) {
            e.printStackTrace();
            out = """
              {"error":"Error interno del servidor, comunícate al área de sistemas."}
              """;
        }

        return Response.ok(out).build();
    }

    //ELIMINAR TOKEN CUANDO CIERRA SESIÓN
    @POST
    @Path("loggout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@FormParam("token") @DefaultValue("") String token) throws Exception {
        System.out.println("Token eliminado");
        String out = "";
        ControllerLogin us = new ControllerLogin();

        try {
            us.eliminarToken(token);
            out = """
                 {\"result\": \"Token eliminado %s\", \"token\": \"%s\", \"usuario\": \"%s\"}
                 """;

        } catch (Exception ex) {

            ex.printStackTrace();
            out = """
                 {"result":"Algo salio mal"}
                 """;
        }
        return Response.status(Response.Status.OK).entity(out).build();
    }

    //@POST //utilizamos @FormParam para resivir datos en los metodos POST, DELETE, PUT 
    //@GET usamos el QueryParam para parametros que se colocan el la url (https:)
}
