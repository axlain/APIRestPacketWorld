package ws;

import dominio.ColoniaImp;
import dto.Respuesta;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("distancia")
public class DistanciaWS {

    @GET
    @Path("calcular")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta calcular(@QueryParam("cpOrigen") String cpOrigen, @QueryParam("cpDestino") String cpDestino) {

        try {
            double distancia = ColoniaImp.calcularDistancia(cpOrigen, cpDestino);

            Respuesta respuesta = new Respuesta();
            respuesta.setError(false);
            respuesta.setMensaje("Distancia calculada correctamente. La distancia es: " + distancia + " km");
            return respuesta;

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
