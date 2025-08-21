/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.inventario.controller.ControllerDatosEquipo;
import org.utl.inventario.controller.ControllerLogin;
import org.utl.inventario.model.Baja;

@Path("baja")
public class RESTBaja {

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosBaja") @DefaultValue("") String datosBaja,
                         @FormParam("token") @DefaultValue("") String token) {
        String out;
        Gson gson = new Gson();
        ControllerLogin cl = new ControllerLogin();
        ControllerDatosEquipo cde = new ControllerDatosEquipo();

        try {
            System.out.println("Datos de baja recibidos: " + datosBaja);

            if (token != null && !token.isEmpty()) {
                boolean isValid = cl.validarToken(token);

                if (isValid) {
                    Baja baja = gson.fromJson(datosBaja, Baja.class);

                    if (baja.getId() <= 0) {
                        cde.save(baja);
                    } else {
                        out = "{\"error\":\"No se permite actualizar registros de baja.\"}";
                        return Response.status(Response.Status.BAD_REQUEST).entity(out).build();
                    }

                    out = gson.toJson(baja);
                } else {
                    out = "{\"error\":\"Token inválido o sin permisos\"}";
                }
            } else {
                out = "{\"error\":\"Falta el token de autenticación\"}";
            }

        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = "{\"error\":\"Formato de JSON incorrecto\"}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Error interno del servidor. Contacta al área de sistemas.\"}";
        }

        return Response.ok(out).build();
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("token") @DefaultValue("") String token) {
        String out;
        ControllerLogin cl = new ControllerLogin();
        ControllerDatosEquipo cde = new ControllerDatosEquipo();

        try {
            if (token != null && !token.isEmpty()) {
                boolean isValid = cl.validarToken(token);

                if (isValid) {
                    List<Baja> bajas = cde.getAll();
                    out = new Gson().toJson(bajas);
                } else {
                    out = "{\"error\":\"Token inválido o sin permisos\"}";
                }
            } else {
                out = "{\"error\":\"Falta el token de autenticación\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Error interno del servidor. Contacta al área de sistemas.\"}";
        }

        return Response.ok(out).build();
    }
    
    

}
