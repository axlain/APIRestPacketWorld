package ws;

import com.google.gson.Gson;
import dominio.ColaboradorImp;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Colaborador;

@Path ("colaborador")
public class ColaboradorWS {
    
    @Path ("obtener-todos")
    @GET 
    @Produces (MediaType.APPLICATION_JSON)
    public List<Colaborador> obtenerColaborador(){
         return ColaboradorImp.obtenerColaborador();
    }
    
    @Path ("obtener-conductores")
    @GET 
    @Produces (MediaType.APPLICATION_JSON)
    public List<Colaborador> obtenerConductoresr(){
         return ColaboradorImp.obtenerConductores();
    }
    
    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Colaborador colaborador = gson.fromJson(json, Colaborador.class);
            return ColaboradorImp.registrarColaborador(colaborador);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path("editar")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        Gson gson = new Gson();
        try {
            Colaborador colaborador = gson.fromJson(json, Colaborador.class);
            return ColaboradorImp.editarColaborador(colaborador);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path ("eliminar/{idColaborador}")
    @DELETE
    @Produces (MediaType.APPLICATION_JSON)
    public Respuesta eliminar(@PathParam("idColaborador")Integer idColaborador){
        try{
            return ColaboradorImp.eliminarColaborador(idColaborador);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @PUT
    @Path("{idColaborador}/asignar-unidad/{idUnidad}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta asignarUnidad(
            @PathParam("idColaborador") int idColaborador,
            @PathParam("idUnidad") int idUnidad) {

        return ColaboradorImp.asignarUnidad(idColaborador, idUnidad);
    }

    @PUT
    @Path("{idColaborador}/desasignar-unidad")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta desasignarUnidad(@PathParam("idColaborador") int idColaborador) {
        return ColaboradorImp.asignarUnidad(idColaborador, null);
    }
    
    @Path("buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Colaborador> buscarColaborador(@QueryParam("filtro") String filtro) {
        try {
            return ColaboradorImp.buscarColaborador(filtro);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path("subir-foto/{idColaborador}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta subirFoto(@PathParam("idColaborador")Integer idColaborador, byte[] foto){
        if(idColaborador != null && idColaborador > 0 && foto.length > 0){
            return ColaboradorImp.guardarFoto(idColaborador, foto);
        }
        throw new BadRequestException();
    }
            
    @Path("obtener-foto/{idColaborador}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Colaborador obtenerFoto(@PathParam("idColaborador") Integer idColaborador) {
        if (idColaborador != null && idColaborador > 0) {
            return ColaboradorImp.obtenerFoto(idColaborador);
        }
        throw new BadRequestException();
    }

}
