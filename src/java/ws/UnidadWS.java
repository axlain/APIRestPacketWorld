package ws;

import com.google.gson.Gson;
import dominio.UnidadImp;
import dto.Respuesta;
import java.util.List;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.client.Entity.json;
import javax.ws.rs.core.MediaType;
import pojo.Unidad;

@Path("unidad")
public class UnidadWS {
    @Path ("obtener-todos")
    @GET 
    @Produces (MediaType.APPLICATION_JSON)
    public List<Unidad> obtenerUnidades(){
        return UnidadImp.obtenerUnidad();
    }
    
    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Unidad unidad = gson.fromJson(json, Unidad.class);
            return UnidadImp.registrarUnidad(unidad);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path("editar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        Gson gson = new Gson();
        try {
            Unidad unidad = gson.fromJson(json, Unidad.class);
            return UnidadImp.editarUnidad(unidad);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path("dar-de-baja")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darDeBaja(String json) {
       Gson gson = new Gson();
        try {
            Unidad unidad = gson.fromJson(json, Unidad.class);
            return UnidadImp.darDeBajaUnidad(unidad.getIdUnidad(), unidad.getMotivoBaja());
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path("buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Unidad> buscarUnidad(@QueryParam("filtro") String filtro) {
        try {
            return UnidadImp.buscarUnidad(filtro);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
