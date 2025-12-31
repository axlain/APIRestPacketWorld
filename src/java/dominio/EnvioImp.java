package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Envio;
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
            conexionBD.commit();

            if (filas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje(
                        "Envío registrado correctamente. Número de guía: " + envio.getNumeroGuia()
                );
            } else {
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

    // =========================
    // CÁLCULO DE COSTO
    // =========================
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
