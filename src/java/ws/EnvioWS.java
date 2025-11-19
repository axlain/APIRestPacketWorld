package ws;

import com.google.gson.Gson;
import dominio.EnvioImp;
import dto.Respuesta;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import pojo.Envio;

@Path("envio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnvioWS {

    @GET
    @Path("consultar/{guia}")
    public String consultarEnvio(@PathParam("guia") String guia) {
        Envio envio = EnvioImp.consultarPorGuia(guia);
        return new Gson().toJson(envio);
    }

    @POST
    @Path("registrar")
    public String registrarEnvio(String json) {
        Envio envio = new Gson().fromJson(json, Envio.class);
        Respuesta respuesta = EnvioImp.registrarEnvio(envio);
        return new Gson().toJson(respuesta);
    }

    @PUT
    @Path("editar")
    public String editarEnvio(String json) {
        Envio envio = new Gson().fromJson(json, Envio.class);
        Respuesta respuesta = EnvioImp.editarEnvio(envio);
        return new Gson().toJson(respuesta);
    }

    @PUT
    @Path("estatus")
    public String actualizarEstatus(@QueryParam("guia") String numeroGuia,@QueryParam("estatus") int idEstatus
    ) {
        Respuesta respuesta = EnvioImp.actualizarEstatus(numeroGuia, idEstatus);
        return new Gson().toJson(respuesta);
    }
}
