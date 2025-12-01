package dominio;

import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colonia;
import pojo.Estado;
import pojo.Municipio;
import pojo.Pais;
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
    public static List<Pais> obtenerPais() {
        List<Pais> paises = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if (conexionBD != null) {
            try {
                paises = conexionBD.selectList("catalogo.obtener-paises");
                conexionBD.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Constantes.MSJ_ERROR_BD);
        }
        
        return paises;
    }
    
    public static List<Estado> obtenerEstado() {
        List<Estado> estados = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if (conexionBD != null) {
            try {
                estados = conexionBD.selectList("catalogo.obtener-estados");
                conexionBD.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Constantes.MSJ_ERROR_BD);
        }
        
        return estados;
    }
    public static List<Municipio> obtenerMunicipio() {
        List<Municipio> municipios = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if (conexionBD != null) {
            try {
                municipios = conexionBD.selectList("catalogo.obtener-municipios");
                conexionBD.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Constantes.MSJ_ERROR_BD);
        }
        
        return municipios;
    }
    public static List<Colonia> obtenerColonia() {
        List<Colonia> colonias = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if (conexionBD != null) {
            try {
                colonias = conexionBD.selectList("catalogo.obtener-colonias");
                conexionBD.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(Constantes.MSJ_ERROR_BD);
        }
        
        return colonias;
    }
}
