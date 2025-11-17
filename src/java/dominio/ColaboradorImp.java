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
    
    public static Respuesta registrarColaborador(Colaborador colaborador) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
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

                if (colaborador.getIdRol() == 3) { 
                    if (colaborador.getNumeroLicencia() == null || colaborador.getNumeroLicencia().trim().isEmpty()) {
                        respuesta.setMensaje("Debe ingresar un número de licencia para los conductores.");
                        return respuesta;
                    }

                    Integer existeLicencia = conexionBD.selectOne("colaborador.verificar-licencia", colaborador.getNumeroLicencia());
                    if (existeLicencia != null && existeLicencia > 0) {
                        respuesta.setMensaje("Ya existe un conductor con el número de licencia ingresado.");
                        return respuesta;
                    }
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
                // 1️⃣ Verificar si el colaborador existe
                Integer existe = conexionBD.selectOne("colaborador.verificar-existe", colaborador.getIdColaborador());
                if (existe == null || existe == 0) {
                    respuesta.setMensaje("El colaborador no existe en la base de datos.");
                    return respuesta;
                }

                // 2️⃣ Validar sucursal existente y activa
                Integer estatusSucursal = conexionBD.selectOne("sucursal.obtener-estatus-sucursal", colaborador.getIdSucursal());
                if (estatusSucursal == null) {
                    respuesta.setMensaje("La sucursal indicada no existe.");
                    return respuesta;
                }
                if (estatusSucursal != Constantes.ESTATUS_ACTIVO) {
                    respuesta.setMensaje("No se puede asignar una sucursal inactiva al colaborador.");
                    return respuesta;
                }

                // 3️⃣ Validar CURP única (que no pertenezca a otro colaborador)
                Integer curpExistente = conexionBD.selectOne("colaborador.verificar-curp", colaborador.getCurp());
                if (curpExistente != null && curpExistente > 0) {
                    // Verificar si la CURP pertenece a otro colaborador distinto
                    Integer idPorCurp = conexionBD.selectOne(
                        "colaborador.obtener-id-por-curp", colaborador.getCurp()
                    );
                    if (idPorCurp != null && idPorCurp != colaborador.getIdColaborador()) {
                        respuesta.setMensaje("La CURP ingresada ya está registrada por otro colaborador.");
                        return respuesta;
                    }
                }

                // 4️⃣ Validar número de licencia (solo si es conductor)
                // y si proporcionó número de licencia
                if (colaborador.getIdRol() == 3 && colaborador.getNumeroLicencia() != null && !colaborador.getNumeroLicencia().trim().isEmpty()) {
                    Integer licenciaExistente = conexionBD.selectOne("colaborador.verificar-licencia", colaborador.getNumeroLicencia());
                    if (licenciaExistente != null && licenciaExistente > 0) {
                        // Verificar si pertenece a otro colaborador
                        Integer idPorLicencia = conexionBD.selectOne(
                            "colaborador.obtener-id-por-licencia", colaborador.getNumeroLicencia()
                        );
                        if (idPorLicencia != null && idPorLicencia != colaborador.getIdColaborador()) {
                            respuesta.setMensaje("El número de licencia ya está asignado a otro conductor.");
                            return respuesta;
                        }
                    }
                }

                // 5️⃣ Ejecutar actualización
                int filasAfectadas = conexionBD.update("colaborador.editar", colaborador);
                conexionBD.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + Constantes.COLABORADOR + ".");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + Constantes.COLABORADOR + ".");
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

                // 1️⃣ Verificar si el colaborador existe
                Integer existe = conexionBD.selectOne("colaborador.verificar-existe", idColaborador);
                if (existe == null || existe == 0) {
                    respuesta.setMensaje("El colaborador no existe en la base de datos.");
                    return respuesta;
                }

                // 2️⃣ Intentar eliminar
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

    public static Respuesta asignarUnidad(int idColaborador, Integer idUnidad) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                String error = verificarColaboradorExisteYEsConductor(conexionBD, idColaborador);
                if (error != null) {
                    respuesta.setMensaje(error);
                    return respuesta;
                }

                if (idUnidad == null) {
                    return desasignarUnidad(conexionBD, idColaborador);
                }
                error = verificarUnidadActiva(conexionBD, idUnidad);
                if (error != null) {
                    respuesta.setMensaje(error);
                    return respuesta;
                }

                Integer unidadActual = obtenerUnidadActual(conexionBD, idColaborador);
                error = unidadEstaOcupadaPorOtro(conexionBD, idUnidad, unidadActual);
                if (error != null) {
                    respuesta.setMensaje(error);
                    return respuesta;
                }
                respuesta = asignarUnidadAConductor(conexionBD, idColaborador, idUnidad);

            } catch (Exception e) {
                respuesta.setMensaje("Error al asignar unidad: " + e.getMessage());
            } finally {
                conexionBD.close();
            }

        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }



    //metodos secundarios
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
            // Si es el mismo, permitir
            if (unidadActual != null && unidadActual.equals(idUnidad)) {
                return null;
            }

            return "La unidad ya está asignada a otro conductor.";
        }
        return null;
    }
    
    private static Respuesta asignarUnidadAConductor(SqlSession conexionBD, int idColaborador, int idUnidad) {
        Respuesta r = new Respuesta();
        r.setError(true);

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("idColaborador", idColaborador);
            params.put("idUnidad", idUnidad);

            int filas = conexionBD.update("colaborador.asignar-unidad", params);
            conexionBD.commit();

            if (filas > 0) {
                r.setError(false);
                r.setMensaje("Unidad asignada correctamente al conductor.");
            } else {
                r.setMensaje("No se pudo asignar la unidad.");
            }

        } catch (Exception e) {
            r.setMensaje("Error al asignar unidad: " + e.getMessage());
        }

        return r;
    }


}
