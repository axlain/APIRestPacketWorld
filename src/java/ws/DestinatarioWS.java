package ws;

import com.google.gson.Gson;
import dominio.DestinatarioImp;
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
import pojo.Destinatario;

@Path("destinatario")
public class DestinatarioWS {

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Destinatario> obtenerDestinatarios() {
        return DestinatarioImp.obtenerDestinatarios();
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Destinatario d = gson.fromJson(json, Destinatario.class);
            return DestinatarioImp.registrarDestinatario(d);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        Gson gson = new Gson();
        try {
            Destinatario d = gson.fromJson(json, Destinatario.class);
            return DestinatarioImp.editarDestinatario(d);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DELETE
    @Path("eliminar/{idDestinatario}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminar(@PathParam("idDestinatario") int idDestinatario) {
        return DestinatarioImp.eliminarDestinatario(idDestinatario);
    }
}
