package dominio;

import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Rol;
import pojo.TipoUnidad;
import utilidades.Constantes;

public class CatalogoImp {
    public static List<Rol> obtenerRolesSistema() {
        List<Rol> roles = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if (conexionBD != null) {
            try {
                roles = conexionBD.selectList("catalogo.obtener-roles");
                conexionBD.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Constantes.MSJ_ERROR_BD);
        }
        
        return roles;
    }
    
    public static List<TipoUnidad> obtenerTipoDeUnidades() {
        List<TipoUnidad> tipoDeUnidades = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if (conexionBD != null) {
            try {
                tipoDeUnidades = conexionBD.selectList("catalogo.obtener-tipo-unidades");
                conexionBD.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Constantes.MSJ_ERROR_BD);
        }
        
        return tipoDeUnidades;
    }
}
