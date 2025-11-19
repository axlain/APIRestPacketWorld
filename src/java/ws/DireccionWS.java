package ws;

import com.google.gson.Gson;
import dominio.DireccionImp;
import dto.ColoniaDTO;
import dto.DatosCPDTO;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("direccion")
public class DireccionWS {

    @GET
    @Path("obtener-datos-por-cp/{codigoPostal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtenerDatosPorCP(@PathParam("codigoPostal") String codigoPostal) {
        DatosCPDTO datos = DireccionImp.obtenerDatosPorCP(codigoPostal);
        return new Gson().toJson(datos);
    }

    @GET
    @Path("obtener-colonias-por-cp/{codigoPostal}")
    @Produces(MediaType.APPLICATION_JSON)
    public String obtenerColoniasPorCP(@PathParam("codigoPostal") String codigoPostal) {
        List<ColoniaDTO> colonias = DireccionImp.obtenerColoniasPorCP(codigoPostal);
        return new Gson().toJson(colonias);
    }
}
