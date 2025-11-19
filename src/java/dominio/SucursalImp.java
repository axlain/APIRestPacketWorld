package dominio;

import dto.ColoniaDTO;
import dto.DatosCodigoPostalDTO;
import dto.DireccionCompletaDTO;
import dto.Respuesta;
import java.util.List;
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
                // Validar CP y colonia
                if (!completarDireccionDesdeCP(sucursal, respuesta)) {
                    return respuesta;
                }

                // Generar código de sucursal
                String codigoGenerado = generarCodigoSucursal(conexionBD, sucursal);
                sucursal.setCodigo(codigoGenerado);

                int filas = conexionBD.insert("sucursal.registrar", sucursal);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_REGISTRO + Constantes.SUCURSAL +
                            " con código: " + codigoGenerado);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_REGISTRO + Constantes.SUCURSAL);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar la sucursal: " + e.getMessage());
            } finally {
                conexionBD.close();
            }

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
                    respuesta.setMensaje(Constantes.MSJ_ERROR_INACTIVA + Constantes.SUCURSAL);
                    return respuesta;
                }

                // Validar CP solo si se envió un nuevo valor
                if (sucursal.getCodigoPostal() != null && !sucursal.getCodigoPostal().isEmpty()) {
                    if (!completarDireccionDesdeCP(sucursal, respuesta)) {
                        return respuesta;
                    }
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

    private static boolean completarDireccionDesdeCP(Sucursal sucursal, Respuesta respuesta) {

        // Obtener la dirección base del CP (país, estado, municipio)
        DatosCodigoPostalDTO datos = DireccionImp.obtenerDatosPorCP(sucursal.getCodigoPostal());

        if (datos == null || datos.isError()) {
            respuesta.setMensaje(datos != null ? datos.getMensaje() :
                    "No se encontró información del código postal ingresado.");
            return false;
        }

        // Obtener todas las colonias del CP
        List<ColoniaDTO> colonias = DireccionImp.obtenerColoniasPorCP(sucursal.getCodigoPostal());

        if (colonias == null || colonias.isEmpty()) {
            respuesta.setMensaje("El código postal no tiene colonias registradas.");
            return false;
        }

        // Validar si la colonia seleccionada pertenece al CP
        boolean coloniaValida = colonias.stream()
                .anyMatch(c -> c.getIdColonia().equals(sucursal.getIdColonia()));

        if (!coloniaValida) {
            respuesta.setMensaje("La colonia seleccionada NO pertenece al código postal ingresado.");
            return false;
        }

        // Asignar datos al POJO sucursal
        sucursal.setIdPais(datos.getIdPais());
        sucursal.setIdEstado(datos.getIdEstado());
        sucursal.setIdMunicipio(datos.getIdMunicipio());

        return true;
    }

    private static String generarCodigoSucursal(SqlSession conexionBD, Sucursal sucursal) {

        DatosCodigoPostalDTO municipio = DireccionImp.obtenerMunicipioPorId(sucursal.getIdMunicipio());

        if (municipio == null || municipio.isError()) {
            throw new RuntimeException("No se encontró información del municipio.");
        }

        String nombreMunicipio = municipio.getMunicipio();
        int codigoMunicipio = municipio.getIdMunicipio();

        // Obtener el consecutivo REAL
        Integer maxConsecutivo = conexionBD.selectOne(
                "sucursal.obtener-maximo-consecutivo",
                sucursal.getIdMunicipio()
        );

        if (maxConsecutivo == null) maxConsecutivo = 0;

        int contador = maxConsecutivo + 1;
        String cont = String.format("%02d", contador);

        String nombreCorto = nombreMunicipio.replaceAll("\\s+", "").toUpperCase();
        if (nombreCorto.length() >= 3) {
            nombreCorto = nombreCorto.substring(0, 3);
        }

        return Constantes.PREFIJO_SUCURSAL + nombreCorto + codigoMunicipio + "-" + cont;
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
