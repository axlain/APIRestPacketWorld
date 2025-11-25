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

    public static Respuesta registrarCliente(Cliente cliente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
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

                Integer existe = conexion.selectOne("cliente.verificar-existe", idCliente);
                if (existe == null || existe == 0) {
                    respuesta.setMensaje("El cliente no existe.");
                    return respuesta;
                }

                int filas = conexion.delete("cliente.eliminar", idCliente);
                conexion.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_BAJA + " el cliente.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + " el cliente.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al eliminar cliente: " + e.getMessage());
            }
            conexion.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
}
