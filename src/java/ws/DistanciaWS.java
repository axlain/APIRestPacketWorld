package ws;

import dominio.ColoniaImp;
import dto.RSDistancia;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/distancia")
public class DistanciaWS {

    @POST
    @Path("/calcular")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calcularDistancia(
        @FormParam("cp1") String cp1,
        @FormParam("cp2") String cp2) {

        // Validación básica de entrada
        if (cp1 == null || cp2 == null || cp1.isEmpty() || cp2.isEmpty()) {
             return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Los códigos postales son obligatorios\"}").build();
        }

        try {
            double distancia = ColoniaImp.calcularDistancia(cp1, cp2);
            RSDistancia respuesta = new RSDistancia(cp1, cp2, distancia);
            return Response.ok(respuesta, MediaType.APPLICATION_JSON).build();

        } catch (Exception e) {
            e.printStackTrace(); // Ver esto en la consola del servidor (Netbeans/IntelliJ)
            // Usar un valor por defecto si el mensaje es null
            String msg = e.getMessage() != null ? e.getMessage() : "Error interno desconocido (Null Pointer)";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + msg + "\"}").build();
        }
    }
}
