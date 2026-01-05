package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.HistorialEstatusEnvio;
import utilidades.Constantes;

public class HistorialEnvioImp {
    public static Respuesta registrarHistorialEnvio(HistorialEstatusEnvio historial) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            int filas = conexionBD.insert("historialEnvio.registrar", historial);
            conexionBD.commit();

            if (filas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("Historial registrado correctamente.");
            } else {
                respuesta.setMensaje("No se pudo registrar el historial.");
            }
        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error: " + e.getMessage());
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }
     public static List<HistorialEstatusEnvio> consultarPorEnvio(int idEnvio) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            return null;
        }

        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("idEnvio", idEnvio);

            return conexionBD.selectList("historialEnvio.consultar-por-envio", params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            conexionBD.close();
        }
    }

}
