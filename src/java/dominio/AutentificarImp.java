package dominio;

import dto.RSAutentificarAdmin;
import java.util.HashMap;
import java.util.LinkedHashMap;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;
import utilidades.Constantes;


public class AutentificarImp {
    
    public static RSAutentificarAdmin autentificarAdministrador (String numero_personal, String contrasena){
        RSAutentificarAdmin respuesta = new RSAutentificarAdmin ();
        SqlSession conexionBD = MyBatisUtil.getSession(); 
        if (conexionBD != null){
            try{
                HashMap<String, String> parametros = new LinkedHashMap<>();
                parametros.put("numero_personal", numero_personal);
                parametros.put("contrasena", contrasena);
                Colaborador colaborador = conexionBD.selectOne("autentificar.administrador", parametros);
                if (colaborador != null){
                    respuesta.setError(false);
                    respuesta.setMensaje("Credenciales correctas del usuario: " + colaborador.getNombre());
                    respuesta.setColaborador(colaborador); 
                }else{
                    respuesta.setError(true);
                    respuesta.setMensaje("Credenciales incorrectas");
                }
            }catch(Exception e){
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage()); 
            }
        }else{
           respuesta.setError(true);
           respuesta.setMensaje (Constantes.MSJ_ERROR_BD);
        }
        return respuesta; 
    }
    
    public static RSAutentificarAdmin autentificarConductor(String numero_personal, String contrasena) {
        RSAutentificarAdmin respuesta = new RSAutentificarAdmin();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                HashMap<String, String> parametros = new LinkedHashMap<>();
                parametros.put("numero_personal", numero_personal);
                parametros.put("contrasena", contrasena);

                // Mapper exclusivo para conductor
                Colaborador colaborador = conexionBD.selectOne("autentificar.conductor", parametros);

                if (colaborador != null) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Bienvenido conductor: " + colaborador.getNombre());
                    respuesta.setColaborador(colaborador);
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("Credenciales incorrectas o el usuario no es conductor");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje(e.getMessage());
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

}
