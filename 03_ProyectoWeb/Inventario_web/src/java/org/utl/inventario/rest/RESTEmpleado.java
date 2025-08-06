/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.inventario.controller.ControllerDatosEmpleado;
import org.utl.inventario.model.Empleado;

/**
 *
 * @author miche
 */
@Path("empleado")
public class RESTEmpleado {
@GET
@Produces(MediaType.APPLICATION_JSON)
public Response buscarPorNumero(@QueryParam("numeroEmpleado") String numeroEmpleado) {
    String out;
    ControllerDatosEmpleado cde = new ControllerDatosEmpleado();
    try {
        Empleado emp = cde.buscarPorNumeroEmpleado(numeroEmpleado);
        out = new Gson().toJson(emp);
    } catch (Exception ex) {
        ex.printStackTrace();
        out = "{\"error\":\"No se pudo buscar el empleado.\"}";
    }

    return Response.ok(out).build();
}  }







   
