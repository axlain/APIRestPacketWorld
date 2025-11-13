package dominio;

import dto.Respuesta;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Sucursal;
import utilidades.Constantes;

public class SucursalImp {

    public static List<Sucursal> obtenerSucursal() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Sucursal> sucursales = null;

        if (conexionBD != null) {
            try {
                sucursales = conexionBD.selectList("sucursal.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return sucursales;
    }

    public static Respuesta registrarSucursales(Sucursal sucursal) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                if (!completarDireccionDesdeCP(conexionBD, sucursal, respuesta)) {
                    conexionBD.close();
                    return respuesta;
                }

                String codigoGenerado = generarCodigoSucursal(conexionBD, sucursal);
                sucursal.setCodigo(codigoGenerado);

                int filasAfectadas = conexionBD.insert("sucursal.registrar", sucursal);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_REGISTRO + Constantes.SUCURSAL + 
                            " con código: " + codigoGenerado);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_REGISTRO + Constantes.SUCURSAL);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar la " + Constantes.SUCURSAL + ": " + e.getMessage());
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }
        return respuesta;
    }

    public static Respuesta editarSucursal(Sucursal sucursal) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                if (esSucursalInactiva(conexionBD, sucursal.getIdSucursal())) {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_INACTIVA + Constantes.SUCURSAL + " inactiva");
                    conexionBD.close();
                    return respuesta;
                }

                if (sucursal.getCodigoPostal() != null && !sucursal.getCodigoPostal().isEmpty()) {
                    if (!completarDireccionDesdeCP(conexionBD, sucursal, respuesta)) {
                        conexionBD.close();
                        return respuesta;
                    }
                }

                int filasAfectadas = conexionBD.update("sucursal.editar", sucursal);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + Constantes.SUCURSAL);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + Constantes.SUCURSAL);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al actualizar la " + Constantes.SUCURSAL + ": " + e.getMessage());
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta darDeBajaSucursal(int idSucursal) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                if (esSucursalInactiva(conexionBD, idSucursal)) {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + Constantes.SUCURSAL + 
                            " porque ya está inactiva.");
                    conexionBD.close();
                    return respuesta;
                }

                if (tieneDependencias(conexionBD, idSucursal)) {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_DEPENDENCIAS + Constantes.SUCURSAL +
                            Constantes.MSJ_ERROR_DEPENDENCIAS_MOTIVO);
                    conexionBD.close();
                    return respuesta;
                }

                int filasAfectadas = conexionBD.update("sucursal.dar-baja", idSucursal);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_BAJA + Constantes.SUCURSAL);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + Constantes.SUCURSAL);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al dar de baja la " + Constantes.SUCURSAL + ": " + e.getMessage());
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    // Funciones auxiliares
    private static boolean completarDireccionDesdeCP(SqlSession conexionBD, Sucursal sucursal, Respuesta respuesta) {
        Map<String, Object> direccion = conexionBD.selectOne(
                "sucursal.obtener-datos-direccion-por-codigopostal",
                sucursal.getCodigoPostal());

        if (direccion == null) {
            respuesta.setMensaje("No se encontró ninguna colonia con ese código postal.");
            return false;
        }

        sucursal.setIdColonia((Integer) direccion.get("id_colonia"));
        sucursal.setIdMunicipio((Integer) direccion.get("id_municipio"));
        sucursal.setIdEstado((Integer) direccion.get("id_estado"));
        sucursal.setIdPais((Integer) direccion.get("id_pais"));
        return true;
    }

    private static String generarCodigoSucursal(SqlSession conexionBD, Sucursal sucursal) {
        Map<String, Object> datosMunicipio = conexionBD.selectOne(
                "sucursal.obtener-datos-municipio", sucursal.getIdMunicipio());

        if (datosMunicipio == null) {
            throw new RuntimeException("No se encontró información del municipio.");
        }

        String nombreMunicipio = (String) datosMunicipio.get("nombreMunicipio");
        Integer codigoMunicipio = (Integer) datosMunicipio.get("codigoMunicipio");

        Integer total = conexionBD.selectOne(
                "sucursal.contar-sucursales-por-municipio", sucursal.getIdMunicipio());
        if (total == null) total = 0;

        String nombreCortoMunicipio = nombreMunicipio.replaceAll("\\s+", "").toUpperCase();
        nombreCortoMunicipio = nombreCortoMunicipio.length() >= 3 ?
                nombreCortoMunicipio.substring(0, 3) : nombreCortoMunicipio;

        int contador = total + 1;
        String cont = String.format(Constantes.FORMATO_DOS_DIGITOS, contador);

        return Constantes.PREFIJO_SUCURSAL + nombreCortoMunicipio + codigoMunicipio + "-" + cont;
    }

    private static boolean tieneDependencias(SqlSession conexionBD, int idSucursal) {
        Integer dependencias = conexionBD.selectOne("sucursal.verificar-dependencias-sucursal", idSucursal);
        return dependencias != null && dependencias > 0;
    }

    private static boolean esSucursalInactiva(SqlSession conexionBD, int idSucursal) {
        Integer estatus = conexionBD.selectOne("sucursal.obtener-estatus-sucursal", idSucursal);
        return estatus != null && estatus == Constantes.ESTATUS_INACTIVO;
    }
}
