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
        if (conexionBD == null) return null;

        try {
            List<Sucursal> sucursales = conexionBD.selectList("sucursal.obtener-todos");
            conexionBD.close();
            return sucursales;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Respuesta registrarSucursales(Sucursal sucursal) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            if (!completarDireccionDesdeCP(conexionBD, sucursal, respuesta)) {
                return respuesta;
            }
            String codigoGenerado = generarCodigoSucursal(conexionBD, sucursal);
            sucursal.setCodigo(codigoGenerado);
            int filasAfectadas = conexionBD.insert("sucursal.registrar", sucursal);
            conexionBD.commit();
            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("Sucursal registrada correctamente con código: " + codigoGenerado);
            } else {
                respuesta.setMensaje("No se pudo registrar la sucursal.");
            }

        } catch (Exception e) {
            respuesta.setMensaje("Error al registrar la sucursal: " + e.getMessage());
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }

    public static Respuesta editarSucursal(Sucursal sucursal) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD == null) {
            respuesta.setMensaje("Error de conexión con la base de datos.");
            return respuesta;
        }

        try {
            if (esSucursalInactiva(conexionBD, sucursal.getIdSucursal())) {
                respuesta.setMensaje("No se puede editar una sucursal inactiva.");
                conexionBD.close();
                return respuesta;
            }
            if (sucursal.getCodigoPostal() != null && !sucursal.getCodigoPostal().isEmpty()) {
                if (!completarDireccionDesdeCP(conexionBD, sucursal, respuesta)) {
                    return respuesta;
                }
            }
            int filasAfectadas = conexionBD.update("sucursal.editar", sucursal);
            conexionBD.commit();
            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("Sucursal " + sucursal.getNombreCorto() + " actualizada correctamente.");
            } else {
                respuesta.setMensaje("No fue posible actualizar la información. Verifique los datos.");
            }

        } catch (Exception e) {
            respuesta.setMensaje("Error al actualizar la sucursal: " + e.getMessage());
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }


    public static Respuesta darDeBajaSucursal(int idSucursal) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD == null) {
            respuesta.setMensaje("Error de conexión con la base de datos.");
            return respuesta;
        }

        try {
            if (tieneDependencias(conexionBD, idSucursal)) {
                respuesta.setMensaje("No se puede dar de baja la sucursal porque tiene colaboradores o unidades asignadas.");
                return respuesta;
            }
            int filasAfectadas = conexionBD.update("sucursal.dar-baja", idSucursal);
            conexionBD.commit();
            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("La sucursal fue dada de baja correctamente.");
            } else {
                respuesta.setMensaje("No se encontró la sucursal o ya estaba inactiva.");
            }
        } catch (Exception e) {
            respuesta.setMensaje("Error al dar de baja la sucursal: " + e.getMessage());
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }
    
    //funciones secundarias 
    // completar los campos de direccion acorde el cp
    private static boolean completarDireccionDesdeCP(SqlSession conexionBD, Sucursal sucursal, Respuesta respuesta) {
        Map<String, Object> direccion = conexionBD.selectOne(
                "sucursal.obtener-datos-direccion-por-codigopostal",
                sucursal.getCodigoPostal());

        if (direccion == null) {
            respuesta.setMensaje("No se encontró ninguna colonia con ese código postal.");
            conexionBD.close();
            return false;
        }

        sucursal.setIdColonia((Integer) direccion.get("id_colonia"));
        sucursal.setIdMunicipio((Integer) direccion.get("id_municipio"));
        sucursal.setIdEstado((Integer) direccion.get("id_estado"));
        sucursal.setIdPais((Integer) direccion.get("id_pais"));
        return true;
    }

    //crear el codigo de sucursal
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
        String cont = String.format("%02d", contador);

        return "SUC" + nombreCortoMunicipio + codigoMunicipio + "-" + cont;
    }

    //comprobar dependencias
    private static boolean tieneDependencias(SqlSession conexionBD, int idSucursal) {
        Integer dependencias = conexionBD.selectOne("sucursal.verificar-dependencias-sucursal", idSucursal);
        return dependencias != null && dependencias > 0;
    }
    
    //comprobar el estatus de sucursal
    private static boolean esSucursalInactiva(SqlSession conexionBD, int idSucursal) {
        Integer estatus = conexionBD.selectOne("sucursal.obtener-estatus-sucursal", idSucursal);
        return estatus != null && estatus == 2;
    }
}
