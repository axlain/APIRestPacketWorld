package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Paquete;
import utilidades.Constantes;

public class PaqueteImp {
    
    public static List<Paquete> obtenerTodos() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Paquete> paquetes = null;

        if (conexionBD != null) {
            try {
                paquetes = conexionBD.selectList("paquete.obtener-todos");
            } finally {
                conexionBD.close();
            }
        }
        return paquetes;
    }

    public static List<Paquete> consultarPorEnvio(int idEnvio) {
        SqlSession conexion = MyBatisUtil.getSession();
        List<Paquete> paquetes = null;

        if (conexion != null) {
            try {
                
                Integer existeEnvio = conexion.selectOne("envio.verificar-existe",idEnvio);
                if (existeEnvio == null || existeEnvio == 0) {
                    return null;
                }

                paquetes = conexion.selectList("paquete.consultar-por-envio",idEnvio);

            } catch (Exception e) {
                e.printStackTrace();
            }
            conexion.close();
        }

        return paquetes;
    }

    public static Respuesta registrarPaquete(Paquete paquete) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Verificar que el envío exista
                Integer existeEnvio = conexionBD.selectOne("envio.verificar-existe",paquete.getIdEnvio());

                if (existeEnvio == null || existeEnvio == 0) {
                    respuesta.setMensaje("El envío al que se desea agregar el paquete no existe.");
                    return respuesta;
                }

                int filasAfectadas = conexionBD.insert("paquete.registrar",paquete);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    EnvioImp.actualizarCostoEnvio(paquete.getIdEnvio());
                    respuesta.setError(false);
                    respuesta.setMensaje("Paquete registrado correctamente.");
                } else {
                    respuesta.setMensaje("No se pudo registrar el paquete.");
                }

            } catch (Exception e) {
                respuesta.setMensaje(Constantes.MSJ_ERROR_PAQUETES + e.getMessage());
                e.printStackTrace();
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta actualizarPaquete(Paquete paquete) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Verificar que el paquete exista
                Integer existePaquete = conexionBD.selectOne("paquete.verificar-existe",paquete.getIdPaquete());

                if (existePaquete == null || existePaquete == 0) {
                    respuesta.setMensaje("El paquete no existe.");
                    return respuesta;
                }
                
                Integer idEnvio = conexionBD.selectOne("paquete.obtener-id-envio",paquete.getIdPaquete());

                int filasAfectadas = conexionBD.update("paquete.actualizar",paquete);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    EnvioImp.actualizarCostoEnvio(idEnvio);

                    respuesta.setError(false);
                    respuesta.setMensaje("Paquete actualizado correctamente y costo actualizado.");
                } else {
                    respuesta.setMensaje("No se pudo actualizar el paquete.");
                }

            } catch (Exception e) {
                respuesta.setMensaje(Constantes.MSJ_ERROR_PAQUETES + e.getMessage());
                e.printStackTrace();
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta eliminarPaquete(int idPaquete) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Verificar que el paquete exista
                Integer existePaquete = conexionBD.selectOne("paquete.verificar-existe",idPaquete);

                if (existePaquete == null || existePaquete == 0) {
                    respuesta.setMensaje("El paquete no existe.");
                    return respuesta;
                }
                
                Integer idEnvio = conexionBD.selectOne("paquete.obtener-id-envio",idPaquete);

                int filasAfectadas = conexionBD.delete( "paquete.eliminar",idPaquete);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    try {
                        EnvioImp.actualizarCostoEnvio(idEnvio);
                        respuesta.setMensaje("Paquete eliminado correctamente y costo actualizado.");
                    } catch (Exception ex) {
                        respuesta.setMensaje("Paquete eliminado correctamente, pero no se pudo recalcular el costo: " + ex.getMessage());
                    }
                    respuesta.setError(false);
                } else {
                    respuesta.setMensaje("No se pudo eliminar el paquete.");
                }


            } catch (Exception e) {
                respuesta.setMensaje(Constantes.MSJ_ERROR_PAQUETES + e.getMessage());
                e.printStackTrace();
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
}
