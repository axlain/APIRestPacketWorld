package ws;

import com.google.gson.Gson;
import dominio.PaqueteImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Paquete;

@Path("paquete")
public class PaqueteWS {

    @GET
    @Path("consultar-por-envio/{idEnvio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paquete> consultarPaquetePorEnvio(@PathParam("idEnvio") int idEnvio) {
        return PaqueteImp.consultarPorEnvio(idEnvio);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrarPaquete(String json) {
        Gson gson = new Gson();
        try {
            Paquete paquete = gson.fromJson(json, Paquete.class);
            return PaqueteImp.registrarPaquete(paquete);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PUT
    @Path("actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta actualizarPaquete(String json) {
        Gson gson = new Gson();
        try {
            Paquete paquete = gson.fromJson(json, Paquete.class);
            return PaqueteImp.actualizarPaquete(paquete);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DELETE
    @Path("eliminar/{idPaquete}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminarPaquete(@PathParam("idPaquete") int idPaquete) {
        try {
            return PaqueteImp.eliminarPaquete(idPaquete);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
