package ws;

import dominio.HistorialEnvioImp;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.HistorialEstatusEnvio;

@Path("historial-envio")
public class HistorialEnvioWS {
    @GET
    @Path("consultar-por-envio/{idEnvio}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HistorialEstatusEnvio> consultarPorEnvio(@PathParam("idEnvio") int idEnvio) {
        return HistorialEnvioImp.consultarPorEnvio(idEnvio);
    }
}
