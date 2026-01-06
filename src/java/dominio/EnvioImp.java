package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
import pojo.HistorialEstatusEnvio;
import pojo.Paquete;
import utilidades.Constantes;
import utilidades.Distancia;

public class EnvioImp {
    
    public static List<Envio> obtenerEnvios() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Envio> envios = null;

        if (conexionBD != null) {
            try {
                envios = conexionBD.selectList("envio.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            }
            conexionBD.close();
        }

        return envios;
    }
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
    
    public static List<Envio> obtenerEnviosPorConductor(int idConductor) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Envio> envios = null;

        if (conexionBD != null) {
            try {
                envios = conexionBD.selectList("envio.obtener-envios-por-conductor",idConductor);
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
        }
        return envios;
    }
    
    public static Respuesta registrarEnvio(Envio envio) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            // Validar colaborador creador
            Integer existeCreador = conexionBD.selectOne(
                    "colaborador.verificar-existe",
                    envio.getIdCreadoPor()
            );
            if (existeCreador == null || existeCreador == 0) {
                respuesta.setMensaje("El colaborador que crea el envío no existe.");
                return respuesta;
            }

            // Validar rol creador
            Integer rolCreador = conexionBD.selectOne(
                    "colaborador.obtener-rol-colaborador",
                    envio.getIdCreadoPor()
            );
            if (rolCreador == null || (rolCreador != 1 && rolCreador != 2)) {
                respuesta.setMensaje("No tienes permisos para registrar envíos.");
                return respuesta;
            }

            // Validar cliente
            Integer existeCliente = conexionBD.selectOne(
                    "cliente.verificar-existe",
                    envio.getIdCliente()
            );
            if (existeCliente == null || existeCliente == 0) {
                respuesta.setMensaje("El cliente especificado no existe.");
                return respuesta;
            }

            // Validar sucursal
            Integer estatusSucursal = conexionBD.selectOne(
                    "sucursal.obtener-estatus-sucursal",
                    envio.getIdSucursal()
            );
            if (estatusSucursal == null) {
                respuesta.setMensaje("La sucursal indicada no existe.");
                return respuesta;
            }
            if (estatusSucursal != Constantes.ESTATUS_ACTIVO) {
                respuesta.setMensaje("No se puede registrar el envío porque la sucursal está inactiva.");
                return respuesta;
            }

            // Validar destinatario
            Integer existeDestinatario = conexionBD.selectOne(
                    "destinatario.verificar-existe",
                    envio.getIdDestinatario()
            );
            if (existeDestinatario == null || existeDestinatario == 0) {
                respuesta.setMensaje("El destinatario especificado no existe.");
                return respuesta;
            }

            // Validar conductor (opcional)
            if (envio.getIdConductor() != null) {
                Integer existeConductor = conexionBD.selectOne(
                        "colaborador.verificar-existe",
                        envio.getIdConductor()
                );
                if (existeConductor == null || existeConductor == 0) {
                    respuesta.setMensaje("El conductor asignado no existe.");
                    return respuesta;
                }

                Integer rolConductor = conexionBD.selectOne(
                        "colaborador.obtener-rol-colaborador",
                        envio.getIdConductor()
                );
                if (rolConductor == null || rolConductor != Constantes.ROL_CONDUCTOR) {
                    respuesta.setMensaje("El colaborador asignado no es un conductor.");
                    return respuesta;
                }

                // VALIDACIÓN NUEVA: evitar conductor repetido con envío activo (1-4)
                Integer tieneEnvioActivo = conexionBD.selectOne(
                        "envio.conductor-tiene-envio-activo",
                        envio.getIdConductor()
                );

                if (tieneEnvioActivo != null && tieneEnvioActivo > 0) {
                    respuesta.setMensaje("No se puede asignar el conductor: ya tiene un envío activo.");
                    return respuesta;
                }
            }

            // Generar guía
            envio.setNumeroGuia(generarNumeroGuia());

            // Estatus inicial
            envio.setIdEstatusActual(1);

            // Costo inicial en 0 (se calcula después con paquetes)
            envio.setCostoTotal(0);

            int filas = conexionBD.insert("envio.registrar", envio);

            if (filas > 0) {
                conexionBD.commit();

                HistorialEstatusEnvio historial = new HistorialEstatusEnvio();
                historial.setIdEnvio(envio.getIdEnvio());
                historial.setIdEstatus(envio.getIdEstatusActual());
                historial.setIdColaborador(envio.getIdCreadoPor());
                historial.setComentario("Registro inicial del envío");

                // 3) Registrar historial usando HistorialEnvioImp (sesión aparte)
                Respuesta respHistorial = HistorialEnvioImp.registrarHistorialEnvio(historial);

                if (!respHistorial.isError()) {
                    respuesta.setError(false);
                    respuesta.setMensaje(
                        "Envío registrado correctamente. Número de guía: " + envio.getNumeroGuia()
                    );
                } else {
                    // Envío ya está guardado, pero falló historial
                    respuesta.setError(false);
                    respuesta.setMensaje(
                        "Envío registrado correctamente, pero NO se pudo registrar el historial. "
                        + respHistorial.getMensaje()
                    );
                }

            } else {
                conexionBD.rollback();
                respuesta.setMensaje("No se pudo registrar el envío.");
            }

        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error al registrar el envío: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }


    // ACTUALIZAR ENVÍO
    public static Respuesta actualizarEnvio(Envio envio) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            Integer existeEnvio = conexionBD.selectOne(
                    "envio.verificar-existe",
                    envio.getIdEnvio()
            );
            if (existeEnvio == null || existeEnvio == 0) {
                respuesta.setMensaje("El envío no existe.");
                return respuesta;
            }

            int filas = conexionBD.update("envio.editar", envio);
            conexionBD.commit();

            if (filas > 0) {
                // Recalcular costo SOLO si hay paquetes
                try {
                    actualizarCostoEnvio(envio.getIdEnvio());
                } catch (Exception e) {
                    System.out.println(
                            "Advertencia: no se pudo recalcular el costo: " + e.getMessage()
                    );
                }

                respuesta.setError(false);
                respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + " el envío.");
            } else {
                respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + " el envío.");
            }

        } catch (Exception e) {
            respuesta.setMensaje("Error al editar el envío: " + e.getMessage());
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }
    
    public static Respuesta actualizarEstatusEnvio(Envio envioReq) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (envioReq == null || envioReq.getNumeroGuia() == null || envioReq.getNumeroGuia().trim().isEmpty()) {
            respuesta.setMensaje("La guía es obligatoria.");
            return respuesta;
        }
        if (envioReq.getIdEstatusActual() == null) {
            respuesta.setMensaje("El estatus es obligatorio.");
            return respuesta;
        }

        // Usaremos idCreadoPor como "quien hizo el cambio" (porque Envio no tiene otro campo)
        if (envioReq.getIdCreadoPor() == null) {
            respuesta.setMensaje("El colaborador (idCreadoPor) es obligatorio.");
            return respuesta;
        }

        String numeroGuia = envioReq.getNumeroGuia().trim();
        int nuevoEstatus = envioReq.getIdEstatusActual();
        int idColaborador = envioReq.getIdCreadoPor();

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
            return respuesta;
        }

        try {
            Envio envioActual = conexionBD.selectOne("envio.consultar", numeroGuia);
            if (envioActual == null) {
                respuesta.setMensaje("No se encontró el envío con la guía proporcionada.");
                return respuesta;
            }

            int estatusActual = envioActual.getIdEstatusActual();

            // ===== Validaciones =====
            if (estatusActual == 5) { respuesta.setMensaje("No se puede cambiar: envío entregado."); return respuesta; }
            if (estatusActual == 6) { respuesta.setMensaje("No se puede cambiar: envío cancelado."); return respuesta; }

            if (estatusActual == nuevoEstatus) {
                respuesta.setMensaje("El envío ya tiene ese estatus.");
                return respuesta;
            }

            if (!transicionPermitida(estatusActual, nuevoEstatus)) {
                respuesta.setMensaje(Constantes.MSJ_TRANSICION_INVALIDA);
                return respuesta;
            }

            if (nuevoEstatus == 3 && envioActual.getIdConductor() == null) {
                respuesta.setMensaje("No se puede poner 'en tránsito' sin asignar un conductor.");
                return respuesta;
            }
            // 1) Update en envio
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("guia", numeroGuia);
            params.put("estatus", nuevoEstatus);

            int filas = conexionBD.update("envio.actualizar-estatus", params);
            if (filas <= 0) {
                conexionBD.rollback();
                respuesta.setMensaje("No se pudo actualizar el estatus del envío.");
                return respuesta;
            }
            conexionBD.commit();

            // 2) Insert historial (comentario desde Constantes)
            HistorialEstatusEnvio historial = new HistorialEstatusEnvio();
            historial.setIdEnvio(envioActual.getIdEnvio());
            historial.setIdEstatus(nuevoEstatus);
            historial.setIdColaborador(idColaborador);
            historial.setComentario(obtenerComentarioPorEstatus(nuevoEstatus));

            Respuesta respHist = HistorialEnvioImp.registrarHistorialEnvio(historial);

            respuesta.setError(false);
            if (!respHist.isError()) {
                respuesta.setMensaje("Estatus actualizado y registrado en historial correctamente.");
            } else {
                respuesta.setMensaje("Estatus actualizado, pero NO se pudo registrar el historial. " + respHist.getMensaje());
            }

        } catch (Exception e) {
            conexionBD.rollback();
            respuesta.setMensaje("Error al actualizar estatus: " + e.getMessage());
        } finally {
            conexionBD.close();
        }

        return respuesta;
    }
    
    private static String obtenerComentarioPorEstatus(int idEstatus) {
        switch (idEstatus) {
            case 1: return Constantes.HIST_ENVIO_REGISTRO_INICIAL;
            case 2: return Constantes.HIST_ENVIO_PROCESADO;
            case 3: return Constantes.HIST_ENVIO_EN_TRANSITO;
            case 4: return Constantes.HIST_ENVIO_DETENIDO;
            case 5: return Constantes.HIST_ENVIO_ENTREGADO;
            case 6: return Constantes.HIST_ENVIO_CANCELADO;
            default: return "Cambio de estatus del envío";
        }
    }

    private static boolean transicionPermitida(int estatusActual, int estatusNuevo) {
        // Estados finales
        if (estatusActual == 5 || estatusActual == 6) {
            return false;
        }
        // Recibido en sucursal
        if (estatusActual == 1) {
            return estatusNuevo == 2 || estatusNuevo == 6;
        }
        // Procesado
        if (estatusActual == 2) {
            return estatusNuevo == 3 || estatusNuevo == 6;
        }
        // En tránsito
        if (estatusActual == 3) {
            return estatusNuevo == 4 || estatusNuevo == 5;
        }
        // Detenido
        if (estatusActual == 4) {
            return estatusNuevo == 3 || estatusNuevo == 6;
        }
        return false;
    }

    // CÁLCULO DE COSTO
    private static double calcularCostoEnvio(int idEnvio) {

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            throw new RuntimeException(Constantes.MSJ_ERROR_BD);
        }

        try {
            List<Paquete> paquetes = conexionBD.selectList("paquete.consultar-por-envio", idEnvio);

            if (paquetes == null || paquetes.isEmpty()) {
                throw new RuntimeException("No se puede calcular el costo: el envío no tiene paquetes.");
            }

            int numPaquetes = paquetes.size();

            String cpOrigen = conexionBD.selectOne("envio.obtener-cp-origen", idEnvio);
            String cpDestino = conexionBD.selectOne("envio.obtener-cp-destino", idEnvio);

            if (cpOrigen == null || cpDestino == null) {
                throw new RuntimeException("No se pudo obtener el código postal del envío.");
            }

            double distanciaKm = Distancia.obtenerDistancia(cpOrigen, cpDestino);
            double costoPorKm = obtenerCostoPorKilometro(distanciaKm);
            double costoAdicional = obtenerCostoAdicionalPorPaquetes(numPaquetes);

            return (distanciaKm * costoPorKm) + costoAdicional;

        } catch (Exception e) {
            throw new RuntimeException("Error al calcular el costo: " + e.getMessage());
        } finally {
            conexionBD.close();
        }
    }
    
    public static void actualizarCostoEnvio(int idEnvio) {

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD == null) {
            throw new RuntimeException(Constantes.MSJ_ERROR_BD);
        }

        try {
            double costo = calcularCostoEnvio(idEnvio);

            Envio envio = new Envio();
            envio.setIdEnvio(idEnvio);
            envio.setCostoTotal(costo);

            conexionBD.update("envio.actualizar-costo", envio);
            conexionBD.commit();

        } finally {
            conexionBD.close();
        }
    }

    private static String generarNumeroGuia() {
        String fecha = java.time.LocalDate.now().toString().replace("-", "");
        String random = java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return "PW-" + fecha + "-" + random;
    }
    private static int obtenerRangoDistancia(double km) {
        if (km <= 200) {
            return 1;
        }
        if (km <= 500) {
            return 2;
        }
        if (km <= 1000) {
            return 3;
        }
        if (km <= 2000) {
            return 4;
        }
        return 5;
    }
    private static double obtenerCostoPorKilometro(double distanciaKm) {
        int rango = obtenerRangoDistancia(distanciaKm);
        double costo = 0.0;

        switch (rango) {
            case 1:
                costo = 4.00;
                break;
            case 2:
                costo = 3.00;
                break;
            case 3:
                costo = 2.00;
                break;
            case 4:
                costo = 1.00;
                break;
            case 5:
                costo = 0.50;
                break;
            default:
                costo = 0.00;
        }

        return costo;
    }

    private static int obtenerGrupoPaquetes(int numPaquetes) {
        if (numPaquetes <= 0) {
            return 0;
        }
        if (numPaquetes >= 5) {
            return 5;
        }
        return numPaquetes;
    }
    
    private static double obtenerCostoAdicionalPorPaquetes(int numPaquetes) {
        int grupo = obtenerGrupoPaquetes(numPaquetes);
        double costo = 0.0;

        switch (grupo) {
            case 0:
                costo = 0.00;
                break;
            case 1:
                costo = 0.00;
                break;
            case 2:
                costo = 50.00;
                break;
            case 3:
                costo = 80.00;
                break;
            case 4:
                costo = 110.00;
                break;
            case 5:
                costo = 150.00;
                break;
            default:
                costo = 0.00;
                break;
        }

        return costo;
    }



}
