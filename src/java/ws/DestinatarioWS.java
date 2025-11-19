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

            // Validación mínima requerida
            if (d.getCodigoPostal() == null || d.getCodigoPostal().trim().isEmpty()) {
                throw new BadRequestException("El código postal es obligatorio.");
            }

            if (d.getIdColonia() == null) {
                throw new BadRequestException("Debe seleccionar una colonia válida.");
            }

            if (d.getCalle() == null || d.getCalle().trim().isEmpty()) {
                throw new BadRequestException("La calle es obligatoria.");
            }

            if (d.getNumero() == null || d.getNumero().trim().isEmpty()) {
                throw new BadRequestException("El número es obligatorio.");
            }

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

            if (d.getIdDestinatario() <= 0) {
                throw new BadRequestException("Falta el ID del destinatario.");
            }

            // Si el usuario intenta cambiar CP, validar datos
            if (d.getCodigoPostal() != null && !d.getCodigoPostal().trim().isEmpty()) {

                if (d.getIdColonia() == null) {
                    throw new BadRequestException("Debe seleccionar una colonia válida.");
                }
            }

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
