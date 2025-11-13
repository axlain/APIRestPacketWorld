package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Sucursal;
import pojo.Unidad;
import utilidades.Constantes;

public class UnidadImp {

    public static List<Unidad> obtenerUnidad() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Unidad> unidades = null;

        if (conexionBD != null) {
            try {
                unidades = conexionBD.selectList("unidad.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
            
        }

        return unidades;
    }

    public static Respuesta registrarUnidad(Unidad unidad) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                // Validar sucursal activa
                if (!esSucursalActiva(conexionBD, unidad.getIdSucursal())) {
                    respuesta.setMensaje("No se puede registrar la unidad. La sucursal seleccionada está inactiva.");
                    conexionBD.close();
                    return respuesta;
                }

                // Generar número interno
                String numeroInternoGenerado = generarNumeroInterno(unidad.getAnio(), unidad.getVin());
                if (numeroInternoGenerado == null) {
                    respuesta.setMensaje("No se pudo generar el número interno. Verifique el VIN o el año.");
                    conexionBD.close();
                    return respuesta;
                }

                unidad.setNumeroInterno(numeroInternoGenerado);
                unidad.setIdEstatusUnidad(Constantes.ESTATUS_ACTIVO);

                int filasAfectadas = conexionBD.insert("unidad.registrar", unidad);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_REGISTRO + Constantes.UNIDAD +
                            " con número interno: " + numeroInternoGenerado);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_REGISTRO + Constantes.UNIDAD);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar la unidad: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta editarUnidad(Unidad unidad) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                if (!esUnidadActiva(conexionBD, unidad.getIdUnidad())) {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_INACTIVA + Constantes.UNIDAD + " inactiva");
                    conexionBD.close();
                    return respuesta;
                }

                if (!esSucursalActiva(conexionBD, unidad.getIdSucursal())) {
                    respuesta.setMensaje("No se puede asignar la unidad a una sucursal inactiva.");
                    conexionBD.close();
                    return respuesta;
                }

                int filasAfectadas = conexionBD.update("unidad.editar", unidad);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + Constantes.UNIDAD);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + Constantes.UNIDAD);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al actualizar la unidad: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta darDeBajaUnidad(int idUnidad, String motivoBaja) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                if (motivoBaja == null || motivoBaja.trim().isEmpty()) {
                    respuesta.setMensaje("Debe especificar un motivo de baja.");
                    conexionBD.close();
                    return respuesta;
                }
                Integer estatusUnidad = conexionBD.selectOne("unidad.obtener-estatus-unidad", idUnidad);
                if (estatusUnidad == null) {
                    respuesta.setMensaje("No se encontró la unidad especificada.");
                    conexionBD.close();
                    return respuesta;
                }

                if (estatusUnidad != Constantes.ESTATUS_ACTIVO) {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + Constantes.UNIDAD + " porque ya está inactiva.");
                    conexionBD.close();
                    return respuesta;
                }

                Integer tieneConductor = conexionBD.selectOne("unidad.unidad-tiene-conductor", idUnidad);
                if (tieneConductor != null && tieneConductor > 0) {
                    respuesta.setMensaje("No se puede dar de baja la unidad porque tiene un conductor asignado.");
                    conexionBD.close();
                    return respuesta;
                }

                Map<String, Object> parametros = new HashMap<>();
                parametros.put("idUnidad", idUnidad);
                parametros.put("motivoBaja", motivoBaja);

                int filasAfectadas = conexionBD.update("unidad.dar-baja", parametros);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_BAJA + Constantes.UNIDAD);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + Constantes.UNIDAD);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al dar de baja la unidad: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    // ?Métodos auxiliares
    private static String generarNumeroInterno(int anio, String vin) {
        try {
            if (vin == null || vin.trim().length() < 4 || anio <= 0) {
                return null;
            }
            String primeros4VIN = vin.trim().toUpperCase().substring(0, 4);
            return anio + "-" + primeros4VIN;
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean esSucursalActiva(SqlSession conexionBD, int idSucursal) {
        try {
            Sucursal sucursal = new Sucursal();
            sucursal.setIdSucursal(idSucursal);
            Integer sucursalActiva = conexionBD.selectOne("unidad.verificar-sucursal-activa", sucursal);
            return sucursalActiva != null && sucursalActiva > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean esUnidadActiva(SqlSession conexionBD, int idUnidad) {
        try {
            Integer estatus = conexionBD.selectOne("unidad.obtener-estatus-unidad", idUnidad);
            return estatus != null && estatus == Constantes.ESTATUS_ACTIVO;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
