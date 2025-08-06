/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.inventario.rest;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.utl.inventario.controller.ControllerEquipo;
import org.utl.inventario.model.AsignacionEquipo;

/**
 *
 * @author miche
 */
@Path("equipo")
public class RESTInventario {
@POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(@FormParam("datosEquipo") @DefaultValue("") String datosEquipo)
    {
        String out = null;
        ControllerEquipo ce = new ControllerEquipo();
        AsignacionEquipo ae = null;
        Gson gson = new Gson();
        System.out.println("Datos de Equipo: " + datosEquipo);
        try
        {
            // Convertimos la cadena JSON que llega por HTTP en un objeto 
            // de tipo Alimento:
            
            ae = gson.fromJson(datosEquipo, AsignacionEquipo.class);
           
            // Si todo va bien hasta aqui, revisamos si se hara un
            // INSERT o un UPDATE:
            if (ae.getId() < 1)
                ce.insert(ae);
            else
                ce.update(ae);
            
            // Convertimos a JSON nuestro objeto de tipo Alimento
            // para devolverlo con los IDs que se pudieron haber generado:
            out = gson.toJson(ae);
        }
        catch(JsonParseException jpe)
        {
            jpe.printStackTrace();
            out = "{\"error\":\"El JSON recibido no es correcto.\"}";

        }
        catch (Exception o)
        {
            o.printStackTrace();
            out = "{\"error\":\"El JSON recibido no es correcto.\"}";

        }
        return Response.ok(out).build();
    }
    
   @POST
    @Path("delete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@FormParam("idEquipo") @DefaultValue("0") int idEquipo)
    {
        String out = null;
        ControllerEquipo  ce = new ControllerEquipo();        
        try
        {
            ce.delete(idEquipo);
            out = "{\"error\":\"El JSON recibido no es correcto.\"}";

        }
        catch(JsonParseException jpe)
        {
            jpe.printStackTrace();
            out = "{\"error\":\"El JSON recibido no es correcto.\"}";

        }
        catch (Exception e)
        {
            e.printStackTrace();
            out = "{\"error\":\"El JSON recibido no es correcto.\"}";

        }
        return Response.ok(out).build();
    }      
      @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll()
    {
        String out = null;
        ControllerEquipo ce = new ControllerEquipo();
        List<AsignacionEquipo> equipos = null;
        try
        {
                    
            
            equipos = ce.getAll(null);
            out = new Gson().toJson(equipos);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            
           out = "{\"error\":\"El JSON recibido no es correcto.\"}";

        }
        return Response.ok(out).build();
    }
}
