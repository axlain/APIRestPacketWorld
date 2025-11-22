package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Destinatario;
import utilidades.Constantes;

public class DestinatarioImp {

    public static List<Destinatario> obtenerDestinatarios() {
        SqlSession conexion = MyBatisUtil.getSession();
        List<Destinatario> lista = null;

        if (conexion != null) {
            try {
                lista = conexion.selectList("destinatario.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexion.close();
            }
        }
        return lista;
    }

    public static Respuesta registrarDestinatario(Destinatario destinatario) {
        Respuesta r = new Respuesta();
        r.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                
                int filasAfectadas = conexion.insert("destinatario.registrar", destinatario);
                conexion.commit();

                if (filasAfectadas > 0) {
                    r.setError(false);
                    r.setMensaje(Constantes.MSJ_EXITO_REGISTRO + " el destinatario.");
                } else {
                    r.setMensaje(Constantes.MSJ_ERROR_REGISTRO + " el destinatario.");
                }

            } catch (Exception e) {
                r.setMensaje("Error al registrar destinatario: " + e.getMessage());
            }
            conexion.close();
        } else {
            r.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return r;
    }

    public static Respuesta editarDestinatario(Destinatario destinatario) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {

                Integer existe = conexion.selectOne("destinatario.verificar-existe", destinatario.getIdDestinatario());
                if (existe == null || existe == 0) {
                    respuesta.setMensaje("El destinatario no existe.");
                    return respuesta;
                }

                int filasAfectadas = conexion.update("destinatario.editar", destinatario);
                conexion.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + " el destinatario.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + " el destinatario.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al actualizar destinatario: " + e.getMessage());
            }
            conexion.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta eliminarDestinatario(int idDestinatario) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Integer existe = conexion.selectOne("destinatario.verificar-existe", idDestinatario);
                if (existe == null || existe == 0) {
                    respuesta.setMensaje("El destinatario no existe.");
                    return respuesta;
                }

                int filasAfectadas = conexion.delete("destinatario.eliminar", idDestinatario);
                conexion.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_BAJA + " el destinatario.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + " el destinatario.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al eliminar destinatario: " + e.getMessage());
            } 
            conexion.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
}
