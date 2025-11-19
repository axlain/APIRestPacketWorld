package dominio;

import dto.ColoniaDTO;
import dto.DatosCodigoPostalDTO;
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
                // Validar CP + COLONIA y completar datos faltantes
                if (!completarDireccionDesdeCP(destinatario, r)) {
                    return r;
                }

                int filas = conexion.insert("destinatario.registrar", destinatario);
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

    public static Respuesta editarDestinatario(Destinatario destinatario) {
        Respuesta r = new Respuesta();
        r.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {

                Integer existe = conexion.selectOne("destinatario.verificar-existe", destinatario.getIdDestinatario());
                if (existe == null || existe == 0) {
                    r.setMensaje("El destinatario no existe.");
                    return r;
                }

                // Si envía un nuevo CP, validar y completar datos
                if (destinatario.getCodigoPostal() != null && !destinatario.getCodigoPostal().isEmpty()) {
                    if (!completarDireccionDesdeCP(destinatario, r)) {
                        return r;
                    }
                }

                int filas = conexion.update("destinatario.editar", destinatario);
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

    private static boolean completarDireccionDesdeCP(Destinatario destinatario, Respuesta respuesta) {

        // Obtener datos base (país, estado, municipio)
        DatosCodigoPostalDTO datos = DireccionImp.obtenerDatosPorCP(destinatario.getCodigoPostal());

        if (datos == null || datos.isError()) {
            respuesta.setMensaje(datos != null ? datos.getMensaje() :
                    "No se encontró información del código postal ingresado.");
            return false;
        }

        // Obtener todas las colonias del CP
        List<ColoniaDTO> colonias = DireccionImp.obtenerColoniasPorCP(destinatario.getCodigoPostal());

        if (colonias == null || colonias.isEmpty()) {
            respuesta.setMensaje("El código postal no tiene colonias registradas.");
            return false;
        }

        // Validar colonia
        boolean coloniaValida = colonias.stream()
                .anyMatch(c -> c.getIdColonia().equals(destinatario.getIdColonia()));

        if (!coloniaValida) {
            respuesta.setMensaje("La colonia seleccionada NO pertenece al código postal ingresado.");
            return false;
        }

        // Asignar a POJO
        destinatario.setIdPais(datos.getIdPais());
        destinatario.setIdEstado(datos.getIdEstado());
        destinatario.setIdMunicipio(datos.getIdMunicipio());

        return true;
    }
}
