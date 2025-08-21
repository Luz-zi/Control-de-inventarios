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
import java.util.List;
import org.utl.inventario.controller.ControllerEmpleado;
import org.utl.inventario.controller.ControllerLogin;
import org.utl.inventario.model.Empleado;

@Path("usuario")
public class RESTUsuario {

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    // el @FormParam es para recibir los datos en el método POST
    public Response save(@FormParam("datosUsuario") @DefaultValue("") String datosEmpleado, @FormParam("token") @DefaultValue("") String token) throws Exception{

       
        String out = null;
        ControllerEmpleado cu = new ControllerEmpleado();
        Empleado e = null;
        ControllerLogin cl = new ControllerLogin();
        Gson gson = new Gson();
        System.out.println("Datos de Usuario: " + datosEmpleado);
        
        if (!token.equals("") && !token.equals(null)) {
                boolean r = cl.validarToken(token);
                if (r == true) {
        
            e = gson.fromJson(datosEmpleado, Empleado.class);

            // Si todo va bien hasta aqui, revisamos si se hara un
            // INSERT o un UPDATE:
            if (e.getId() < 1) {
                
              
                cu.insert(e);
            } else {
              
                cu.update(e);

            }

            // para devolverlo con los IDs que se pudieron haber generado:
            out = gson.toJson(e);
                } else {
                    out = """
                     {"error": "Error de acceso, no tienes permiso"}
                     """;
                }
            } else {
                out = """
                     {"error": "Error de acceso, no tienes permiso"}
                     """;
            }

        return Response.ok(out).build();
    }
    
    
@POST
@Path("delete")
@Produces(MediaType.APPLICATION_JSON)
public Response delete(@FormParam("cve_usuario") @DefaultValue("0") int idUsuario, 
                       @FormParam("token") @DefaultValue("") String token) {
    String out = null;
    ControllerEmpleado ce = new ControllerEmpleado();   
    ControllerLogin cl = new ControllerLogin();
    
    try {
        if (token != null && !token.equals("")) {
            boolean isValid = cl.validarToken(token);
            if (isValid) {
                ce.delete(idUsuario);
                out = """
                      {"result":"Registro eliminado de forma correcta."}
                      """;
            } else {
                out = """
                      {"error": "Error de acceso, no tienes permiso"}
                      """;
            }
        } else {
            out = """
                  {"error": "Error de acceso, no tienes permiso"}
                  """;
        }
    } catch (JsonParseException jpe) {
        jpe.printStackTrace();
        out = """
              {"error":"El JSON recibido no es correcto."}
              """;
    } catch (Exception e) {
        e.printStackTrace();
        out = """
              {"error":"Error interno del servidor, comunícate al area de sistemas de El Zarape."}
              """;
    }

    return Response.ok(out).build();
}

    
    @GET
@Path("getAll")
@Produces(MediaType.APPLICATION_JSON)
public Response getAll(@QueryParam("token") @DefaultValue("") String token) {
    String out = null;
    ControllerLogin cl = new ControllerLogin();
    ControllerEmpleado ce = new ControllerEmpleado();
    List<Empleado> empleados = null;

    try {
        if (token != null && !token.equals("")) {
            boolean isValid = cl.validarToken(token);
            if (isValid) {
                empleados = ce.getAll(null);
                out = new Gson().toJson(empleados);
            } else {
                out = """
                      {"error": "Error de acceso, no tienes permiso"}
                      """;
            }
        } else {
            out = """
                  {"error": "Error de acceso, no tienes permiso"}
                  """;
        }
    } catch (Exception e) {
        e.printStackTrace();
        out = """
              {"error" : "Error interno del Servidor, comunicate al area de Sistemas"}
              """;
    }

    return Response.ok(out).build();
}

    
    

}
