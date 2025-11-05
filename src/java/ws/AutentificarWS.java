package ws;

import dominio.AutentificarImp;
import dto.RSAutentificarAdmin;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path ("autentificar")
public class AutentificarWS {
    //Tod implemenatacion de incio sesion administracion
    @Path("administracion")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutentificarAdmin autentificacionProfesor(
            @FormParam("numero_personal")String numero_personal, 
            @FormParam("contrasena")String contrasena){
        //validar q no este vacio, limites en la longitud de datos
        if ((numero_personal != null && !numero_personal.isEmpty()) 
                && (contrasena != null && !contrasena.isEmpty())) {
            return AutentificarImp.autentificarAdministrador(numero_personal, contrasena); 
        }
        
        throw new BadRequestException(); 
    }
}
