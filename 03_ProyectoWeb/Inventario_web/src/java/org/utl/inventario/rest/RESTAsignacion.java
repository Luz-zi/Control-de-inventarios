/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.inventario.controller.ControllerAsignacionEquipo;
import org.utl.inventario.controller.ControllerLogin;
import org.utl.inventario.model.AsignacionEquipo;

/**
 *
 * @author sando
 */

@Path("asignacion")
public class RESTAsignacion {

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosAsignacion") @DefaultValue("") String datosAsignacion,
            @FormParam("token") @DefaultValue("") String token) {
        String out;
        Gson gson = new Gson();
        ControllerLogin cl = new ControllerLogin();
        ControllerAsignacionEquipo cae = new ControllerAsignacionEquipo();

        try {
            System.out.println("Datos de asignación recibidos: " + datosAsignacion);

            if (token != null && !token.isEmpty()) {
                boolean isValid = cl.validarToken(token);

                if (isValid) {
                    AsignacionEquipo asignacion = gson.fromJson(datosAsignacion, AsignacionEquipo.class);

                    if (asignacion.getId() <= 0) {
                        int idGenerado = cae.save(asignacion);
                        asignacion.setId(idGenerado);
                    } else {
                        out = "{\"error\":\"No se permite actualizar registros de asignación.\"}";
                        return Response.status(Response.Status.BAD_REQUEST).entity(out).build();
                    }

                    out = gson.toJson(asignacion);
                } else {
                    out = "{\"error\":\"Token inválido o sin permisos.\"}";
                }
            } else {
                out = "{\"error\":\"Falta el token de autenticación.\"}";
            }

        } catch (JsonParseException jpe) {
            jpe.printStackTrace();
            out = "{\"error\":\"Formato de JSON incorrecto.\"}";
        } catch (Exception e) {
            e.printStackTrace();
            out = "{\"error\":\"Error interno del servidor. Contacta al área de sistemas.\"}";
        }

        return Response.ok(out).build();
    }

}
