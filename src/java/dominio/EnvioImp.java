package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
import utilidades.Constantes;

public class EnvioImp {

    public static Envio consultarPorGuia(String numeroGuia) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        Envio envio = null;

        if (conexionBD != null) {
            try {
                envio = conexionBD.selectOne("envio.consultar", numeroGuia);
            } catch (Exception e) {
                e.printStackTrace();
            }
            conexionBD.close();
        }
        return envio;
    }

    public static Respuesta registrarEnvio(Envio envio) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                // Validar colaborador creador
                Integer existeCreador = conexionBD.selectOne("colaborador.verificar-existe", envio.getIdCreadoPor());
                if (existeCreador == null || existeCreador == 0) {
                    respuesta.setMensaje("El colaborador que crea el envío no existe.");
                    return respuesta;
                }

                // Validar rol del creador (solo 1 y 2 pueden registrar envíos)
                Integer rolCreador = conexionBD.selectOne("colaborador.obtener-rol-colaborador", envio.getIdCreadoPor());
                if (rolCreador == null || (rolCreador != 1 && rolCreador != 2)) {
                    respuesta.setMensaje("No tienes permisos para registrar envíos.");
                    return respuesta;
                }

                // Validar cliente
                Integer existeCliente = conexionBD.selectOne("cliente.verificar-existe", envio.getIdCliente());
                if (existeCliente == null || existeCliente == 0) {
                    respuesta.setMensaje("El cliente especificado no existe.");
                    return respuesta;
                }

                // Validar sucursal activa
                Integer estatusSucursal = conexionBD.selectOne("sucursal.obtener-estatus-sucursal", envio.getIdSucursal());
                if (estatusSucursal == null) {
                    respuesta.setMensaje("La sucursal indicada no existe.");
                    return respuesta;
                }
                if (estatusSucursal != Constantes.ESTATUS_ACTIVO) {
                    respuesta.setMensaje("No se puede registrar el envío porque la sucursal está inactiva.");
                    return respuesta;
                }

                // Validar destinatario
                Integer existeDestinatario = conexionBD.selectOne("destinatario.verificar-existe", envio.getIdDestinatario());
                if (existeDestinatario == null || existeDestinatario == 0) {
                    respuesta.setMensaje("El destinatario especificado no existe.");
                    return respuesta;
                }

                // Validar conductor (si se envía)
                if (envio.getIdConductor() != null) {
                    Integer existeConductor = conexionBD.selectOne("colaborador.verificar-existe", envio.getIdConductor());
                    if (existeConductor == null || existeConductor == 0) {
                        respuesta.setMensaje("El conductor asignado no existe.");
                        return respuesta;
                    }

                    Integer rolConductor = conexionBD.selectOne("colaborador.obtener-rol-colaborador", envio.getIdConductor());
                    if (rolConductor == null || rolConductor != Constantes.ROL_CONDUCTOR) {
                        respuesta.setMensaje("El colaborador asignado no es un conductor.");
                        return respuesta;
                    }
                }

                // Generar número de guía
                String guia = generarNumeroGuia();
                envio.setNumeroGuia(guia);

                // Estatus inicial automático
                envio.setIdEstatusActual(1); // 1 = recibido en sucursal

                // Registrar en BD
                int filas = conexionBD.insert("envio.registrar", envio);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Envío registrado correctamente. Número de guía: " + guia);
                } else {
                    respuesta.setMensaje("No se pudo registrar el envío.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar el envío: " + e.getMessage());
                e.printStackTrace();
            }

            conexionBD.close();
        }

        return respuesta;
    }

    public static Respuesta editarEnvio(Envio envio) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            // Verificar que existe el envío
            Envio envioBD = conexionBD.selectOne("envio.consultar", envio.getNumeroGuia());
            if (envioBD == null) {
                respuesta.setMensaje("El número de guía no existe.");
                return respuesta;
            }

            // Validar destinatario
            Integer existeDestinatario = conexionBD.selectOne("destinatario.verificar-existe", envio.getIdDestinatario());
            if (existeDestinatario == null || existeDestinatario == 0) {
                respuesta.setMensaje("El destinatario especificado no existe.");
                return respuesta;
            }

            // Validar sucursal activa
            Integer estatusSucursal = conexionBD.selectOne("sucursal.obtener-estatus-sucursal", envio.getIdSucursal());
            if (estatusSucursal == null) {
                respuesta.setMensaje("La sucursal indicada no existe.");
                return respuesta;
            }
            if (estatusSucursal != Constantes.ESTATUS_ACTIVO) {
                respuesta.setMensaje("La sucursal está inactiva. No se puede asignar.");
                return respuesta;
            }

            // Validar conductor (si se envía)
            if (envio.getIdConductor() != null) {
                Integer existeConductor = conexionBD.selectOne("colaborador.verificar-existe", envio.getIdConductor());
                if (existeConductor == null || existeConductor == 0) {
                    respuesta.setMensaje("El conductor asignado no existe.");
                    return respuesta;
                }

                Integer rolConductor = conexionBD.selectOne("colaborador.obtener-rol-colaborador", envio.getIdConductor());
                if (rolConductor == null || rolConductor != Constantes.ROL_CONDUCTOR) {
                    respuesta.setMensaje("El colaborador asignado no es un conductor.");
                    return respuesta;
                }

                Integer tieneUnidad = conexionBD.selectOne("colaborador.conductor-tiene-unidad", envio.getIdConductor());
                if (tieneUnidad == null || tieneUnidad == 0) {
                    respuesta.setMensaje("El conductor asignado no tiene una unidad asignada.");
                    return respuesta;
                }
            }


            int filasAfectadas = conexionBD.update("envio.editar", envio);
            conexionBD.commit();

            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + " el envío.");
            } else {
                respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + " el envío.");
            }

        } catch (Exception e) {
            respuesta.setMensaje("Error al editar el envío: " + e.getMessage());
        } 
        conexionBD.close();

        return respuesta;
    }

    public static Respuesta actualizarEstatus(String guia, int nuevoEstatus) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            // Verificar que el envío existe
            Envio envio = conexionBD.selectOne("envio.consultar", guia);
            if (envio == null) {
                respuesta.setMensaje("El envío no existe.");
                return respuesta;
            }

            // Verificar que el estatus existe
            Integer existe = conexionBD.selectOne("estatus-envio.verificar-existe", nuevoEstatus);
            if (existe == null || existe == 0) {
                respuesta.setMensaje("El estatus especificado no existe.");
                return respuesta;
            }

            // Ejecutar actualización
            Map<String, Object> params = new HashMap<>();
            params.put("guia", guia);
            params.put("estatus", nuevoEstatus);

            int filasAfectadas = conexionBD.update("envio.actualizar-estatus", params);
            conexionBD.commit();

            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("El estatus del envío ha sido actualizado correctamente.");
            } else {
                respuesta.setMensaje("No se pudo actualizar el estatus del envío.");
            }

        } catch (Exception e) {
            respuesta.setMensaje("Error al actualizar estatus: " + e.getMessage());
        } 
        conexionBD.close();
        
        return respuesta;
    }
    
    //metodo secundario
    private static String generarNumeroGuia() {
        String fecha = java.time.LocalDate.now().toString().replace("-", "");  // YYYYMMDD
        String random = java.util.UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return "PW-" + fecha + "-" + random;
    }

}
