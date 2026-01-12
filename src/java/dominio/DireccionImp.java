package dominio;

import dto.RSColonia;
import dto.RSDatosCodigoPostal;
import dto.Respuesta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Municipio;
import utilidades.Constantes;

public class DireccionImp {

    public static RSDatosCodigoPostal obtenerDatosPorCP(String codigoPostal) {
        RSDatosCodigoPostal respuesta = new RSDatosCodigoPostal();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // 1) Validar si existe el CP
                Integer existeCP = conexionBD.selectOne("direccion.validar-cp-existe", codigoPostal);

                if (existeCP == null || existeCP == 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró el CP " + codigoPostal);
                    return respuesta;
                }

                // 2) Si existe, traer los datos
                RSDatosCodigoPostal datos = conexionBD.selectOne("direccion.obtener-datos-por-cp", codigoPostal);

                if (datos != null) {
                    datos.setError(false);
                    datos.setMensaje("Datos encontrados.");
                    respuesta = datos;
                } else {
                    // Caso raro: existe el CP pero no devolvió datos (por joins / datos inconsistentes)
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontraron datos para el CP " + codigoPostal);
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al consultar CP: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }


    public static List<RSColonia> obtenerColoniasPorCP(String codigoPostal) {

        SqlSession conexionBD = MyBatisUtil.getSession();
        List<RSColonia> lista = null;

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("direccion.obtener-colonias-por-cp", codigoPostal);

                if (lista == null || lista.isEmpty()) {

                    RSColonia c = new RSColonia();
                    c.setError(true);
                    c.setMensaje("No existen colonias registradas para el CP " + codigoPostal);
                    List<RSColonia> listaError = new ArrayList<>();
                    listaError.add(c);

                    return listaError;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            conexionBD.close();
        }

        return lista;
    }
    
    public static Municipio obtenerMunicipioPorId(int idMunicipio) {
        Municipio municipio = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                municipio = conexionBD.selectOne("direccion.obtener-municipio", idMunicipio);
            } catch (Exception e) {
                e.printStackTrace();
            }
            conexionBD.close();
        }

        return municipio;
    }
    
    public static Respuesta validarPais(int idPais) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                Integer existe = conexionBD.selectOne("direccion.validar-pais-existe", idPais);

                if (existe != null && existe > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("País válido.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("El país seleccionado no existe.");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al validar país: " + e.getMessage());
            }
            conexionBD.close();

        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta validarEstadoExiste(int idEstado) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Integer existe = conexion.selectOne("direccion.validar-estado-existe", idEstado);

                if (existe != null && existe > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Estado válido.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("El estado no existe.");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al validar estado: " + e.getMessage());
            }
            conexion.close();
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    
    
    public static Respuesta validarMunicipioExiste(int idMunicipio) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Integer existe = conexion.selectOne("direccion.validar-municipio-existe", idMunicipio);

                if (existe != null && existe > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Municipio válido.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("El municipio no existe.");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al validar municipio: " + e.getMessage());
            }
            conexion.close();
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta validarColoniaExiste(int idColonia) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Integer existe = conexion.selectOne("direccion.validar-colonia-existe", idColonia);

                if (existe != null && existe > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Colonia válida.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("La colonia no existe.");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al validar colonia: " + e.getMessage());
            }
            conexion.close();
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta validarEstadoPais(int idEstado, int idPais) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                Map<String, Object> params = new HashMap<>();
                params.put("idEstado", idEstado);
                params.put("idPais", idPais);

                Integer existe = conexionBD.selectOne("direccion.validar-estado-pais", params);

                if (existe != null && existe > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Estado válido para el país.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("El estado no pertenece al país seleccionado.");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al validar estado y país: " + e.getMessage());
            }
            conexionBD.close();

        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta validarMunicipioEstado(int idMunicipio, int idEstado) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                Map<String, Object> params = new HashMap<>();
                params.put("idMunicipio", idMunicipio);
                params.put("idEstado", idEstado);

                Integer existe = conexionBD.selectOne("direccion.validar-municipio-estado", params);

                if (existe != null && existe > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Municipio válido para el estado.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("El municipio no pertenece al estado seleccionado.");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al validar municipio y estado: " + e.getMessage());
            }
            conexionBD.close();

        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta validarColoniaMunicipio(int idColonia, int idMunicipio) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                
                Integer codigoMunicipio = conexionBD.selectOne(
                    "direccion.obtener-codigo-municipio", 
                    idMunicipio
                );

                if (codigoMunicipio == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El municipio no existe.");
                    return respuesta;
                }

                Map<String, Object> params = new HashMap<>();
                params.put("idColonia", idColonia);
                params.put("codigoMunicipio", codigoMunicipio);

                Integer existe = conexionBD.selectOne("direccion.validar-colonia-municipio", params);

                if (existe != null && existe > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje("Colonia válida para el municipio.");
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("La colonia no pertenece al municipio seleccionado.");
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al validar colonia y municipio: " + e.getMessage());
            }
            conexionBD.close();

        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta validarDireccionCompleta(int idPais, int idEstado, int idMunicipio, int idColonia) {

        // Validar que el pais existe
        Respuesta pais = validarPais(idPais);
        if (pais.isError()) return pais;

        // Validar que el estado existe
        Respuesta estadoExiste = validarEstadoExiste(idEstado);
        if (estadoExiste.isError()) return estadoExiste;

        // Validar que el estado pertenezca al pais
        Respuesta estadoPais = validarEstadoPais(idEstado, idPais);
        if (estadoPais.isError()) return estadoPais;

        // Validar que el municipio existe
        Respuesta municipioExiste = validarMunicipioExiste(idMunicipio);
        if (municipioExiste.isError()) return municipioExiste;

        // Validar que el municipio pertenezca al estado
        Respuesta municipioEstado = validarMunicipioEstado(idMunicipio, idEstado);
        if (municipioEstado.isError()) return municipioEstado;

        // Validar que la colonia existe
        Respuesta coloniaExiste = validarColoniaExiste(idColonia);
        if (coloniaExiste.isError()) return coloniaExiste;

        // Validar que la colonia pertenezca al municipio 
        Respuesta coloniaMunicipio = validarColoniaMunicipio(idColonia, idMunicipio);
        if (coloniaMunicipio.isError()) return coloniaMunicipio;

        Respuesta direccionValida = new Respuesta();
        direccionValida.setError(false);
        direccionValida.setMensaje("Dirección válida.");
        return direccionValida;
    }
    
}
