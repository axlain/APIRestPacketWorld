package ws;

import com.google.gson.Gson;
import dominio.EnvioImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Envio;

@Path("envio")
public class EnvioWS {

    @GET
    @Path("consultar/{guia}")
    @Produces(MediaType.APPLICATION_JSON)
    public Envio consultarEnvioPorGuia(@PathParam("guia") String guia) {
        return EnvioImp.consultarPorGuia(guia);
    }
    
    @GET
    @Path("obtener-por-conductor/{idConductor}")
    @Produces(MediaType.APPLICATION_JSON)
    public Envio obtenerEnvioPorIdConductor(@PathParam("idConductor") int idConductor) {
        if (idConductor > 0) {
            return EnvioImp.obtenerEnvioPorConductor(idConductor);
        }
        throw new BadRequestException();
    }
    
    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Envio> obtenerTodos() {
        return EnvioImp.obtenerEnvios();
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrarEnvio(String json) {
        Gson gson = new Gson();
        try {
            Envio envio = gson.fromJson(json, Envio.class);
            return EnvioImp.registrarEnvio(envio);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PUT
    @Path("actualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta actualizarEnvio(String json) {
        Gson gson = new Gson();
        try {
            Envio envio = gson.fromJson(json, Envio.class);
            return EnvioImp.actualizarEnvio(envio);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PUT
    @Path("estatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta actualizarEstatusEnvio(String json) {
        Gson gson = new Gson();
        try {
            Envio envio = gson.fromJson(json, Envio.class);
            return EnvioImp.actualizarEstatusEnvio(envio);

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
