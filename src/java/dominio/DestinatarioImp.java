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

    public static Respuesta registrarDestinatario(Destinatario d) {
        Respuesta r = new Respuesta();
        r.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                int filas = conexion.insert("destinatario.registrar", d);
                conexion.commit();

                if (filas > 0) {
                    r.setError(false);
                    r.setMensaje(Constantes.MSJ_EXITO_REGISTRO + " del destinatario.");
                } else {
                    r.setMensaje(Constantes.MSJ_ERROR_REGISTRO + " del destinatario.");
                }

            } catch (Exception e) {
                r.setMensaje("Error al registrar destinatario: " + e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            r.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return r;
    }

    public static Respuesta editarDestinatario(Destinatario d) {
        Respuesta r = new Respuesta();
        r.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Integer existe = conexion.selectOne("destinatario.verificar-existe", d.getIdDestinatario());
                if (existe == null || existe == 0) {
                    r.setMensaje("El destinatario no existe.");
                    return r;
                }

                int filas = conexion.update("destinatario.editar", d);
                conexion.commit();

                if (filas > 0) {
                    r.setError(false);
                    r.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + " del destinatario.");
                } else {
                    r.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + " del destinatario.");
                }

            } catch (Exception e) {
                r.setMensaje("Error al actualizar destinatario: " + e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            r.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return r;
    }

    public static Respuesta eliminarDestinatario(int idDestinatario) {
        Respuesta r = new Respuesta();
        r.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Integer existe = conexion.selectOne("destinatario.verificar-existe", idDestinatario);
                if (existe == null || existe == 0) {
                    r.setMensaje("El destinatario no existe.");
                    return r;
                }

                int filas = conexion.delete("destinatario.eliminar", idDestinatario);
                conexion.commit();

                if (filas > 0) {
                    r.setError(false);
                    r.setMensaje(Constantes.MSJ_EXITO_BAJA + " del destinatario.");
                } else {
                    r.setMensaje(Constantes.MSJ_ERROR_BAJA + " del destinatario.");
                }

            } catch (Exception e) {
                r.setMensaje("Error al eliminar destinatario: " + e.getMessage());
            } finally {
                conexion.close();
            }
        } else {
            r.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return r;
    }
}
