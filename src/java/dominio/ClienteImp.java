package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Cliente;
import utilidades.Constantes;


public class ClienteImp {
    public static List<Cliente> obtenerCliente() {
        SqlSession conexionBD = MyBatisUtil.getSession();
        List<Cliente> clientes = null;

        if (conexionBD != null) {
            try {
                clientes = conexionBD.selectList("cliente.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } 
            conexionBD.close();
            
        }
        return clientes;
    }
    
    public static Respuesta registrarCliente(Cliente cliente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                int filas = conexionBD.insert("cliente.registrar", cliente);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_REGISTRO + "cliente.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_REGISTRO + "cliente.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al registrar cliente: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta editarCliente(Cliente cliente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                int filas = conexionBD.update("cliente.editar", cliente);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_ACTUALIZAR + "cliente.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_ACTUALIZAR + "cliente.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al editar cliente: " + e.getMessage());
            } 
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }


    public static Respuesta eliminarCliente(int idCliente) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                int filas = conexionBD.delete("cliente.eliminar", idCliente);
                conexionBD.commit();

                if (filas > 0) {
                    respuesta.setError(false);
                    respuesta.setMensaje(Constantes.MSJ_EXITO_BAJA + "cliente.");
                } else {
                    respuesta.setMensaje(Constantes.MSJ_ERROR_BAJA + "cliente.");
                }

            } catch (Exception e) {
                respuesta.setMensaje("Error al eliminar cliente: " + e.getMessage());
            }
            conexionBD.close();
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
}
