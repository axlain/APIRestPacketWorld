package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Colaborador;
import utilidades.Constantes;

public class ColaboradorImp {
    
    public static List<Colaborador> obtenerColaborador() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Colaborador> colaboradores = null;

        if (conexionBD != null) {
            try {
                colaboradores = conexionBD.selectList("colaborador.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
        }

        return colaboradores;
    }
    
    public static List<Colaborador> obtenerConductores() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Colaborador> conductores = null;

        if (conexionBD != null) {
            try {
                conductores = conexionBD.selectList("colaborador.obtener-conductores");
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
        }

        return conductores;
    }
    
    public static Respuesta registrarColaborador(Colaborador colaborador) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (colaborador.getCurp() == null || colaborador.getCurp().length() != 18) {
                    respuesta.setMensaje("La CURP debe tener exactamente 18 caracteres.");
                    return respuesta;
                }

                String regexCurp = "^[A-Z]{4}\\d{6}[A-Z]{6}[A-Z0-9]{2}$";
                if (!colaborador.getCurp().toUpperCase().matches(regexCurp)) {
                    respuesta.setMensaje("La CURP ingresada no tiene un formato válido.");
                    return respuesta;
                }
                
                Integer existeCurp = conexionBD.selectOne("colaborador.verificar-curp", colaborador.getCurp());
                if (existeCurp != null && existeCurp > 0) {
                    respuesta.setMensaje("Ya existe un colaborador con la CURP ingresada.");
                    return respuesta;
                }

                Integer estatusSucursal = conexionBD.selectOne("sucursal.obtener-estatus-sucursal", colaborador.getIdSucursal());
                if (estatusSucursal == null || estatusSucursal != Constantes.ESTATUS_ACTIVO) {
                    respuesta.setMensaje("No se puede registrar el colaborador. La sucursal seleccionada no está activa.");
                    return respuesta;
                }

                if (colaborador.getIdRol() == Constantes.ROL_CONDUCTOR) {
                    if (colaborador.getNumeroLicencia() == null || colaborador.getNumeroLicencia().trim().isEmpty()) {
                        respuesta.setMensaje("Debe ingresar un número de licencia para los conductores.");
                        return respuesta;
                    }
                    String licencia = colaborador.getNumeroLicencia().trim();
                    Integer existeLicencia = conexionBD.selectOne("colaborador.verificar-licencia", licencia);
                    if (existeLicencia != null && existeLicencia > 0) {
                        respuesta.setMensaje("Ya existe un conductor con el número de licencia ingresado.");
                        return respuesta;
                    }
                    colaborador.setNumeroLicencia(licencia);
                } else {
                    colaborador.setNumeroLicencia(null);
                }

                String numeroPersonal = generarNumeroPersonal(colaborador.getCurp(), colaborador.getContrasena());
                if (numeroPersonal == null) {
                    respuesta.setMensaje("No se pudo generar el número de personal. Verifique los datos.");
                    return respuesta;
                }
                colaborador.setNumeroPersonal(numeroPersonal.toUpperCase());

                int filasAfectadas = conexionBD.insert("colaborador.registrar", colaborador);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_REGISTRO + Constantes.COLABORADOR +
                            " con número personal: " + numeroPersonal);
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_REGISTRO + Constantes.COLABORADOR);
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar el colaborador: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta editarColaborador(Colaborador colaborador) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Verificar si el colaborador existe
                String verificarColaborador = verificarColaboradorExiste(conexionBD, colaborador.getIdColaborador());
                if (verificarColaborador != null) {
                    respuesta.setMensaje(verificarColaborador);
                    return respuesta;
                }

                // ✅ 1) Obtener rol actual en BD (NO se permite cambiar rol)
                Integer rolActual = conexionBD.selectOne(
                        "colaborador.obtener-rol-colaborador",
                        colaborador.getIdColaborador()
                );

                if (rolActual == null) {
                    respuesta.setMensaje("El colaborador no existe.");
                    return respuesta;
                }

                // Bloquear cambio de rol
                if (rolActual.intValue() != colaborador.getIdRol()) {
                    respuesta.setMensaje("No se puede editar el rol del colaborador.");
                    return respuesta;
                }

                // Forzar rol real para evitar inconsistencias
                colaborador.setIdRol(rolActual);

                // Validar CURP
                if (colaborador.getCurp() == null || colaborador.getCurp().length() != 18) {
                    respuesta.setMensaje("La CURP debe tener exactamente 18 caracteres.");
                    return respuesta;
                }

                String regexCurp = "^[A-Z]{4}\\d{6}[A-Z]{6}[A-Z0-9]{2}$";
                if (!colaborador.getCurp().toUpperCase().matches(regexCurp)) {
                    respuesta.setMensaje("La CURP ingresada no tiene un formato válido.");
                    return respuesta;
                }

                // Validar sucursal existente y activa
                Integer estatusSucursal = conexionBD.selectOne(
                        "sucursal.obtener-estatus-sucursal",
                        colaborador.getIdSucursal()
                );

                if (estatusSucursal == null) {
                    respuesta.setMensaje("La sucursal indicada no existe.");
                    return respuesta;
                }
                if (estatusSucursal != Constantes.ESTATUS_ACTIVO) {
                    respuesta.setMensaje("No se puede asignar una sucursal inactiva al colaborador.");
                    return respuesta;
                }

                // Validar CURP duplicada pero permitiendo la misma persona
                Integer curpExistente = conexionBD.selectOne("colaborador.verificar-curp", colaborador.getCurp());
                if (curpExistente != null && curpExistente > 0) {
                    Integer idPorCurp = conexionBD.selectOne("colaborador.obtener-id-por-curp", colaborador.getCurp());
                    if (idPorCurp != null && idPorCurp != colaborador.getIdColaborador()) {
                        respuesta.setMensaje("La CURP ingresada ya está registrada por otro colaborador.");
                        return respuesta;
                    }
                }

                // Validar número de licencia SOLO si el rol real es Conductor
                if (rolActual == Constantes.ROL_CONDUCTOR) {
                    if (colaborador.getNumeroLicencia() == null || colaborador.getNumeroLicencia().trim().isEmpty()) {
                        respuesta.setMensaje("Debe ingresar un número de licencia para los conductores.");
                        return respuesta;
                    }
                    String licencia = colaborador.getNumeroLicencia().trim();
                    Integer existeLicencia = conexionBD.selectOne("colaborador.verificar-licencia", licencia);
                    if (existeLicencia != null && existeLicencia > 0) {
                        Integer idPorLicencia = conexionBD.selectOne("colaborador.obtener-id-por-licencia", licencia);
                        // Si esa licencia pertenece a OTRO colaborador, es duplicada
                        if (idPorLicencia != null && idPorLicencia != colaborador.getIdColaborador()) {
                            respuesta.setMensaje("El número de licencia ya está asignado a otro conductor.");
                            return respuesta;
                        }
                    }
                    colaborador.setNumeroLicencia(licencia);
                } else {
                    colaborador.setNumeroLicencia(null);
                }

                int filasAfectadas = conexionBD.update("colaborador.editar", colaborador);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + Constantes.COLABORADOR + ".");
                } else {
                    respuesta.setMensaje("No se realizaron cambios en la información del colaborador.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al editar el colaborador: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta eliminarColaborador(int idColaborador) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                String verificarColaborador = verificarColaboradorExiste(conexionBD, idColaborador);
                if (verificarColaborador != null) {
                    respuesta.setMensaje(verificarColaborador);
                    return respuesta;
                }
                
                Integer tieneUnidad = conexionBD.selectOne("colaborador.tiene-unidad", idColaborador);
                if (tieneUnidad != null && tieneUnidad > 0) {
                    respuesta.setMensaje("No se puede eliminar al colaborador porque tiene una unidad asignada.");
                    return respuesta;
                }

                Integer envios = conexionBD.selectOne("colaborador.tiene-envios", idColaborador);
                if (envios != null && envios > 0) {
                    respuesta.setMensaje("No se puede eliminar el colaborador porque tiene envíos asociados.");
                    return respuesta;
                }

                int filasAfectadas = conexionBD.delete("colaborador.eliminar", idColaborador);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(
                        Constantes.MSJ_EXITO_BAJA + Constantes.COLABORADOR + " correctamente."
                    );
                } else {
                    respuesta.setMensaje(
                        Constantes.MSJ_ERROR_BAJA + Constantes.COLABORADOR + "."
                    );
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al eliminar el colaborador: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta licenciaDisponible(String licencia, Integer idColaboradorActual) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                Integer existe = conexionBD.selectOne("colaborador.verificar-licencia", licencia);

                if (existe == null || existe == 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Disponible");
                    return respuesta;
                }

                Integer idPorLicencia = conexionBD.selectOne("colaborador.obtener-id-por-licencia", licencia);

                if (idColaboradorActual != null && idPorLicencia != null && idPorLicencia.equals(idColaboradorActual)) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Disponible");
                    return respuesta;
                }

                respuesta.setMensaje("El número de licencia ya está asignado a otro conductor.");
            } catch (Exception e) {
                respuesta.setMensaje("Error al validar licencia: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta asignarUnidad(int idColaborador, Integer idUnidad) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                String verificarColaboradorConductor = verificarColaboradorExisteYEsConductor(conexionBD, idColaborador);
                if (verificarColaboradorConductor != null) {
                    respuesta.setMensaje(verificarColaboradorConductor);
                    return respuesta;
                }
                Integer enviosActivos = conexionBD.selectOne("colaborador.tiene-envios-activos", idColaborador);
                if (enviosActivos != null && enviosActivos > 0) {
                    respuesta.setMensaje("No se puede cambiar la unidad: el conductor tiene envíos en proceso.");
                    return respuesta;
                }


                // Si idUnidad es null => desasignar
                if (idUnidad == null) {
                    return desasignarUnidad(conexionBD, idColaborador);
                }

                verificarColaboradorConductor = verificarUnidadActiva(conexionBD, idUnidad);
                if (verificarColaboradorConductor != null) {
                    respuesta.setMensaje(verificarColaboradorConductor);
                    return respuesta;
                }

                // ✅ Validar misma sucursal ANTES de checar ocupado
                String validacionSucursal = validarMismaSucursal(conexionBD, idColaborador, idUnidad);
                if (validacionSucursal != null) {
                    respuesta.setMensaje(validacionSucursal);
                    return respuesta;
                }

                Integer unidadActual = obtenerUnidadActual(conexionBD, idColaborador);
                verificarColaboradorConductor = unidadEstaOcupadaPorOtro(conexionBD, idUnidad, unidadActual);
                if (verificarColaboradorConductor != null) {
                    respuesta.setMensaje(verificarColaboradorConductor);
                    return respuesta;
                }

                respuesta = asignarUnidadAConductor(conexionBD, idColaborador, idUnidad);

            } catch (Exception e) {
                respuesta.setMensaje("Error al asignar unidad: " + e.getMessage());
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    private static String validarMismaSucursal(SqlSession conexionBD, int idColaborador, int idUnidad) {
        Integer sucursalConductor = conexionBD.selectOne("colaborador.obtener-sucursal-colaborador", idColaborador);
        Integer sucursalUnidad = conexionBD.selectOne("unidad.obtener-sucursal-unidad", idUnidad);

        if (sucursalConductor == null) return "No existe el conductor.";
        if (sucursalUnidad == null) return "No existe la unidad.";
        if (!sucursalConductor.equals(sucursalUnidad)) return "No se puede asignar la unidad: pertenece a otra sucursal.";
        return null;
    }


    public static List<Colaborador> buscarColaborador(String filtro) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Colaborador> lista = null;

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("colaborador.buscar-colaborador",filtro);
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
        }

        return lista;
    }

    public static Respuesta guardarFoto(int idColaborador, byte[] foto){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD != null){
            try{
                
                String verificarColaborador = verificarColaboradorExiste(conexionBD, idColaborador);
                if (verificarColaborador != null) {
                    respuesta.setMensaje(verificarColaborador);
                    return respuesta;
                }
                
                Colaborador colaborador = new Colaborador();
                colaborador.setIdColaborador(idColaborador);
                colaborador.setFoto(foto);
                
                int filasAfectadas = conexionBD.update("colaborador.guardar-foto", colaborador);
                conexionBD.commit();
                
                if(filasAfectadas > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("La fotografía del colaborador ha sido guardad éxiitosamente");
                } else {
                    respuesta.setMensaje("Lo sentimos la fotograía no se logro guardar");
                }
                conexionBD.close();
            } catch (Exception e){
               respuesta.setMensaje(e.getMessage());
           }
       } else {
           respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
       }
        
       return respuesta;
    }
    
    public static Respuesta subirFoto(int idColaborador, byte[] foto){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD != null){
            try{
                Colaborador colaborador = new Colaborador();
                colaborador.setIdColaborador(idColaborador);
                colaborador.setFoto(foto);
                
                int filasAfectadas = conexionBD.update("colaborador.guardar-foto", colaborador);
                conexionBD.commit();
                
                if(filasAfectadas > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("La fotografía del colaborador(a) ha sido guardad éxiitosamente");
                } else {
                    respuesta.setMensaje("Lo sentimos la fotograía no se logro guardar");
                }
                conexionBD.close();
            } catch (Exception e){
               respuesta.setMensaje(e.getMessage());
           }
       } else {
           respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
       }
       return respuesta;
    }

    public static Colaborador obtenerFoto(int idColaborador){
       Colaborador colaborador = null;
       SqlSession conexionBD = MyBatisUtil.getSession();

       if(conexionBD != null){
           try{

               String verificarColaborador = verificarColaboradorExiste(conexionBD, idColaborador);
               if (verificarColaborador != null) {
                   return null;
               }

               colaborador = conexionBD.selectOne("colaborador.obtener-foto", idColaborador);

               if(colaborador != null && colaborador.getFotoBase64() != null){
                   String fotoLimpia = colaborador.getFotoBase64().replaceAll("\\s+", "");
                   colaborador.setFotoBase64(fotoLimpia);
               }

               conexionBD.close();
           } catch (Exception e){
               e.printStackTrace();
           }
      } 

      return colaborador;
   }

    private static String generarNumeroPersonal(String curp, String contrasena) {
        try {
            if (curp == null || contrasena == null || curp.length() < 10 || contrasena.length() < 2) {
                return null;
            }

            String letrasCurp = curp.substring(0, 4).toUpperCase();
            String numerosPw = contrasena.replaceAll("\\D+", "");
            if (numerosPw.length() < 2) {
                numerosPw = "00";
            } else {
                numerosPw = numerosPw.substring(0, 2);
            }

            String letrasExtraCurp = curp.substring(4, 8).toUpperCase();
            String numerosCurp = curp.substring(8, 10);

            String letrasAleatorias = generarLetrasAleatorias(3);
            return letrasCurp + numerosPw + letrasExtraCurp + numerosCurp + letrasAleatorias;

        } catch (Exception e) {
            return null;
        }
    }

    private static String generarLetrasAleatorias(int cantidad) {
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder resultado = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < cantidad; i++) {
            resultado.append(letras.charAt(random.nextInt(letras.length())));
        }

        return resultado.toString();
    }
    
    private static String verificarColaboradorExiste(SqlSession conexionBD, int idColaborador) {
        Integer existe = conexionBD.selectOne("colaborador.verificar-existe", idColaborador);

        if (existe == null || existe == 0) {
            return "El colaborador no existe en la base de datos.";
        }

        return null; 
    }

    private static String verificarColaboradorExisteYEsConductor(SqlSession conexionBD, int idColaborador) {
        Integer rol = conexionBD.selectOne("colaborador.obtener-rol-colaborador", idColaborador);

        if (rol == null) {
            return "El colaborador no existe.";
        }

        if (rol != Constantes.ROL_CONDUCTOR) {
            return "Solo se pueden asignar unidades a conductores.";
        }

        return null; 
    }
    
    private static Respuesta desasignarUnidad(SqlSession conexionBD, int idColaborador) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        try {
            int filas = conexionBD.update("colaborador.desasignar-unidad", idColaborador);
            conexionBD.commit();

            if (filas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("El conductor ha sido desasignado de su unidad.");
            } else {
                respuesta.setMensaje("El conductor no tenía unidad asignada.");
            }

        } catch (Exception e) {
            respuesta.setMensaje("Error al desasignar unidad: " + e.getMessage());
        }
        return respuesta;
    }
    
    private static String verificarUnidadActiva(SqlSession conexionBD, int idUnidad) {
        Integer estatus = conexionBD.selectOne("unidad.obtener-estatus-unidad", idUnidad);
        if (estatus == null) {
            return "La unidad no existe.";
        }
        if (estatus != Constantes.ESTATUS_ACTIVO) {
            return "La unidad está inactiva, no se puede asignar.";
        }
        return null;
    }

    private static Integer obtenerUnidadActual(SqlSession conexionBD, int idColaborador) {
        return conexionBD.selectOne("colaborador.obtener-unidad-asignada", idColaborador);
    }
    
    private static String unidadEstaOcupadaPorOtro(SqlSession conexionBD, int idUnidad, Integer unidadActual) {
        Integer ocupada = conexionBD.selectOne("colaborador.unidad-asignada-a-otro", idUnidad);
        if (ocupada != null && ocupada > 0) {
            if (unidadActual != null && unidadActual.equals(idUnidad)) {
                return null;
            }

            return "La unidad ya está asignada a otro conductor.";
        }
        return null;
    }
    
    private static Respuesta asignarUnidadAConductor(SqlSession conexionBD, int idColaborador, int idUnidad) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        try {
            // 1) Validar que sea conductor
            Integer idRol = conexionBD.selectOne("colaborador.obtener-rol-colaborador", idColaborador);
            if (idRol == null) {
                respuesta.setMensaje("El colaborador no existe.");
                return respuesta;
            }
            if (idRol != Constantes.ROL_CONDUCTOR) {
                respuesta.setMensaje("Solo se puede asignar unidad a un conductor.");
                return respuesta;
            }

            // 2) Validar que NO tenga envíos activos (solo bloquea si NO está entregado/cancelado)
            Integer enviosActivos = conexionBD.selectOne("colaborador.tiene-envios-activos", idColaborador);
            if (enviosActivos != null && enviosActivos > 0) {
                respuesta.setMensaje("No se puede cambiar la unidad: el conductor tiene envíos en proceso.");
                return respuesta;
            }

            // 3) Validar misma sucursal (conductor vs unidad)
            Integer sucursalConductor = conexionBD.selectOne("colaborador.obtener-sucursal-colaborador", idColaborador);
            Integer sucursalUnidad = conexionBD.selectOne("unidad.obtener-sucursal-unidad", idUnidad);

            if (sucursalConductor == null) {
                respuesta.setMensaje("No existe el conductor.");
                return respuesta;
            }
            if (sucursalUnidad == null) {
                respuesta.setMensaje("No existe la unidad.");
                return respuesta;
            }
            if (!sucursalConductor.equals(sucursalUnidad)) {
                respuesta.setMensaje("No se puede asignar la unidad: pertenece a otra sucursal.");
                return respuesta;
            }

            // 4) Validar que la unidad no esté asignada a otro conductor (permitir si ya la tiene el mismo)
            Integer unidadActual = conexionBD.selectOne("colaborador.obtener-unidad-asignada", idColaborador);

            Integer ocupada = conexionBD.selectOne("colaborador.unidad-asignada-a-otro", idUnidad);
            if (ocupada != null && ocupada > 0) {
                // Si la unidad que quiere asignar es la misma que ya tiene, se permite
                if (unidadActual == null || !unidadActual.equals(idUnidad)) {
                    respuesta.setMensaje("No se puede asignar la unidad: ya está asignada a otro conductor.");
                    return respuesta;
                }
            }

            // 5) Actualizar
            Map<String, Object> params = new HashMap<>();
            params.put("idColaborador", idColaborador);
            params.put("idUnidad", idUnidad);

            int filas = conexionBD.update("colaborador.asignar-unidad", params);
            conexionBD.commit();

            if (filas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("Unidad asignada correctamente al conductor.");
            } else {
                respuesta.setMensaje("No se pudo asignar la unidad.");
            }

        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error al asignar unidad: " + e.getMessage());
        }

        return respuesta;
    }


        
}