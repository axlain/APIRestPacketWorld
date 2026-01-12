package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Sucursal;
import utilidades.Constantes;

public class SucursalImp {

    public static List<Sucursal> obtenerSucursales() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Sucursal> sucursales = null;

        if (conexionBD != null) {
            try {
                sucursales = conexionBD.selectList("sucursal.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            }
            conexionBD.close();
        }
        return sucursales;
    }
    
    public static Sucursal obtenerSucursalPorId(int idSucursal) {
        Sucursal sucursal = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                sucursal = conexionBD.selectOne("sucursal.obtener-por-id",idSucursal);
                conexionBD.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sucursal;
    }

    public static Respuesta registrarSucursal(Sucursal sucursal) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                
                String codigoGenerado = generarCodigoSucursal(conexionBD, sucursal.getIdMunicipio());
                sucursal.setCodigo(codigoGenerado);
                
                // Validar la dirección antes de registrar
                Respuesta validacion = DireccionImp.validarDireccionCompleta(
                        sucursal.getIdPais(),
                        sucursal.getIdEstado(),
                        sucursal.getIdMunicipio(),
                        sucursal.getIdColonia()
                );

                if (validacion.isError()) {
                    return validacion;  
                }
                
                int filas = conexionBD.insert("sucursal.registrar", sucursal);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_REGISTRO +  Constantes.SUCURSAL +
                            " con código: " + codigoGenerado);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_REGISTRO + Constantes.SUCURSAL);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar la sucursal: " + e.getMessage());
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
                    respuesta.setMensaje(Constantes.MSJ_ERROR_INACTIVA + " " +Constantes.SUCURSAL + " inactiva");
                    return respuesta;
                }
                
                // Validar la dirección antes de registrar
                Respuesta validacion = DireccionImp.validarDireccionCompleta(
                        sucursal.getIdPais(),
                        sucursal.getIdEstado(),
                        sucursal.getIdMunicipio(),
                        sucursal.getIdColonia()
                );

                if (validacion.isError()) {
                    return validacion;  
                }

                int filas = conexionBD.update("sucursal.editar", sucursal);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + Constantes.SUCURSAL);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + Constantes.SUCURSAL);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al actualizar sucursal: " + e.getMessage());
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
                    return respuesta;
                }

                if (tieneDependencias(conexionBD, idSucursal)) {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_DEPENDENCIAS + Constantes.SUCURSAL +
                            Constantes.MSJ_ERROR_DEPENDENCIAS_MOTIVO);
                    return respuesta;
                }

                int filas = conexionBD.update("sucursal.dar-baja", idSucursal);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_BAJA + Constantes.SUCURSAL);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + Constantes.SUCURSAL);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al dar de baja la sucursal: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    private static String generarCodigoSucursal(SqlSession conexionBD, int idMunicipio) {

        Integer maxConsecutivo = conexionBD.selectOne(
                "sucursal.obtener-maximo-consecutivo",
                idMunicipio
        );

        if (maxConsecutivo == null) {
            maxConsecutivo = 0;
        }

        int contador = maxConsecutivo + 1;
        String cont = String.format("%02d", contador);

        return Constantes.PREFIJO_SUCURSAL + idMunicipio + "-" + cont;
    }

    private static boolean tieneDependencias(SqlSession conexionBD, int idSucursal) {
        Integer dependencias = conexionBD.selectOne(
                "sucursal.verificar-dependencias-sucursal",
                idSucursal
        );
        return dependencias != null && dependencias > 0;
    }

    private static boolean esSucursalInactiva(SqlSession conexionBD, int idSucursal) {
        Integer estatus = conexionBD.selectOne(
                "sucursal.obtener-estatus-sucursal",
                idSucursal
        );
        return estatus != null && estatus == Constantes.ESTATUS_INACTIVO;
    }
    
    public static List<Sucursal> buscarSucursal(String filtro) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Sucursal> lista = null;

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("sucursal.buscar-sucursal",filtro);
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
        }

        return lista;
    }
}
