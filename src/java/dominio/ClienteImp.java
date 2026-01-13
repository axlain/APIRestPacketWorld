package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Cliente;
import utilidades.Constantes;

public class ClienteImp {

    public static List<Cliente> obtenerClientes() {
        SqlSession conexion = MyBatisUtil.getSession();
        List<Cliente> clientes = null;

        if (conexion != null) {
            try {
                clientes = conexion.selectList("cliente.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            }
            conexion.close();
        }
        return clientes;
    }
    
    public static Cliente obtenerClientePorId(int idCliente) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        Cliente cliente = null;

        if (conexionBD != null) {
            try {
                cliente = conexionBD.selectOne("cliente.obtener-por-id",idCliente);
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
        }
        return cliente;
    }

    public static Respuesta registrarCliente(Cliente cliente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {

                // Validar la dirección antes de registrar
                Respuesta validacion = DireccionImp.validarDireccionCompleta(
                        cliente.getIdPais(),
                        cliente.getIdEstado(),
                        cliente.getIdMunicipio(),
                        cliente.getIdColonia()
                );

                if (validacion.isError()) {
                    return validacion;  
                }
                
                // Validar teléfono único
String telefono = (cliente.getTelefono() != null) ? cliente.getTelefono().trim() : "";
if (telefono.isEmpty()) {
    respuesta.setMensaje("El teléfono es obligatorio.");
    return respuesta;
}

Integer existeTel = conexion.selectOne("cliente.telefono-existe", telefono);
if (existeTel != null && existeTel > 0) {
    respuesta.setMensaje("El teléfono ya está registrado por otro cliente.");
    return respuesta;
}
// Validar correo único
String correo = (cliente.getCorreo() != null) ? cliente.getCorreo().trim() : "";
if (correo.isEmpty()) {
    respuesta.setMensaje("El correo es obligatorio.");
    return respuesta;
}

Integer existeCorreo = conexion.selectOne("cliente.correo-existe", correo);
if (existeCorreo != null && existeCorreo > 0) {
    respuesta.setMensaje("El correo ya está registrado por otro cliente.");
    return respuesta;
}

                int filasAfectadas = conexion.insert("cliente.registrar", cliente);
                conexion.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_REGISTRO + " el cliente.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_REGISTRO + " el cliente.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar cliente: " + e.getMessage());
            }
            conexion.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta editarCliente(Cliente cliente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {

                Integer existe = conexion.selectOne("cliente.verificar-existe", cliente.getIdCliente());
                if (existe == null || existe == 0) {
                    respuesta.setMensaje("El cliente no existe.");
                    return respuesta;
                }
                
                // Validar la dirección antes de registrar
                Respuesta validacion = DireccionImp.validarDireccionCompleta(
                        cliente.getIdPais(),
                        cliente.getIdEstado(),
                        cliente.getIdMunicipio(),
                        cliente.getIdColonia()
                );

                if (validacion.isError()) {
                    return validacion;  
                }
       String telefono = (cliente.getTelefono() != null) ? cliente.getTelefono().trim() : "";
if (telefono.isEmpty()) {
    respuesta.setMensaje("El teléfono es obligatorio.");
    return respuesta;
}

java.util.HashMap<String, Object> params = new java.util.HashMap<>();
params.put("telefono", telefono);
params.put("idCliente", cliente.getIdCliente());

Integer existeTelOtro = conexion.selectOne("cliente.telefono-existe-otro", params);
if (existeTelOtro != null && existeTelOtro > 0) {
    respuesta.setMensaje("El teléfono ya está registrado por otro cliente.");
    return respuesta;
}
// Validar correo único (excepto el mismo cliente)
String correo = (cliente.getCorreo() != null) ? cliente.getCorreo().trim() : "";
if (correo.isEmpty()) {
    respuesta.setMensaje("El correo es obligatorio.");
    return respuesta;
}

java.util.HashMap<String, Object> paramsCorreo = new java.util.HashMap<>();
paramsCorreo.put("correo", correo);
paramsCorreo.put("idCliente", cliente.getIdCliente());

Integer existeCorreoOtro = conexion.selectOne("cliente.correo-existe-otro", paramsCorreo);
if (existeCorreoOtro != null && existeCorreoOtro > 0) {
    respuesta.setMensaje("El correo ya está registrado por otro cliente.");
    return respuesta;
}

                int filasAfectadas = conexion.update("cliente.editar", cliente);
                conexion.commit();

                if (filasAfectadas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + " información del cliente.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + " información del cliente.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al editar cliente: " + e.getMessage());
            } 
            conexion.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta eliminarCliente(int idCliente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                Integer existe = conexion.selectOne(
                        "cliente.verificar-existe",
                        idCliente
                );
                if (existe == null || existe == 0) {
                    respuesta.setMensaje("El cliente no existe.");
                    return respuesta;
                }
                Integer dependencias = conexion.selectOne(
                        "cliente.tiene-dependencias",
                        idCliente
                );

                if (dependencias != null && dependencias > 0) {
                    respuesta.setMensaje(
                            "No es posible eliminar el cliente porque tiene envíos registrados."
                    );
                    return respuesta;
                }
                int filas = conexion.delete("cliente.eliminar", idCliente);
                conexion.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(
                            Constantes.MSJ_EXITO_BAJA + " el cliente."
                    );
                } else {
                    respuesta.setMensaje(
                            Constantes.MSJ_ERROR_BAJA + " el cliente."
                    );
                }

            } catch (Exception e) {
                respuesta.setMensaje(
                        "Error al eliminar cliente: " + e.getMessage()
                );
            } finally {
                conexion.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    
    public static List<Cliente> buscarCliente(String filtro) {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Cliente> lista = null;

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("cliente.buscar-cliente", filtro);
            } catch (Exception e) {
                e.printStackTrace();
            }
            conexionBD.close();
        }

        return lista;
    }

}
