package dominio;

import dto.RSAutentificarAdmin;
import java.util.HashMap;
import java.util.LinkedHashMap;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;


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
                    respuesta.setMensaje("Credenciales correctas del usuario: "+colaborador.getNombre());
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
           respuesta.setMensaje ("No hay conexion a la base de datos");
        }
        return respuesta; 
    }
    
    
}
