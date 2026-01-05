package ws;

import dominio.CatalogoImp;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Colonia;
import pojo.Estado;
import pojo.EstatusEnvio;
import pojo.Municipio;
import pojo.Pais;
import pojo.Rol;
import pojo.TipoUnidad;

@Path ("catalogo")
public class CatalogoWS {
    
    @Path ("obtener-roles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rol> obtenerRolesSistema(){
        return CatalogoImp.obtenerRolesSistema();
    }
    
    @Path ("obtener-tipo-de-unidades")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TipoUnidad> obtenerTipoDeUnidades(){
        return CatalogoImp.obtenerTipoDeUnidades();
    }
    
    @Path ("obtener-paises")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pais> obtenerPaises(){
        return CatalogoImp.obtenerPais();
    }
    @Path ("obtener-estados")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Estado> obtenerEstados(){
        return CatalogoImp.obtenerEstado();
    }
    @Path ("obtener-municipios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Municipio> obtenerMunicipios(){
        return CatalogoImp.obtenerMunicipio();
    }
    @Path ("obtener-colonias")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Colonia> obtenerColonias(){
        return CatalogoImp.obtenerColonia();
    }
    
    @Path ("obtener-estatus-envio")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EstatusEnvio> obtenerEstatusEnvio(){
        return CatalogoImp.obtenerEstatusEnvio();
    }
}
