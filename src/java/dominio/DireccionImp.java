package dominio;

import dto.ColoniaDTO;
import dto.DatosCPDTO;
import java.util.ArrayList;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class DireccionImp {

    public static DatosCPDTO obtenerDatosPorCP(String codigoPostal) {
        DatosCPDTO respuesta = new DatosCPDTO();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                DatosCPDTO datos = conexionBD.selectOne("direccion.obtener-datos-por-cp", codigoPostal);

                if (datos != null) {
                    datos.setError(false);
                    datos.setMensaje("Datos encontrados.");
                    respuesta = datos;
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontraron datos para el CP " + codigoPostal);
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al consultar CP: " + e.getMessage());
            }
            conexionBD.close();
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No se pudo conectar a la base de datos.");
        }

        return respuesta;
    }


    public static List<ColoniaDTO> obtenerColoniasPorCP(String codigoPostal) {

        SqlSession conexionBD = MyBatisUtil.getSession();
        List<ColoniaDTO> lista = null;

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("direccion.obtener-colonias-por-cp", codigoPostal);

                if (lista == null || lista.isEmpty()) {

                    ColoniaDTO c = new ColoniaDTO();
                    c.setError(true);
                    c.setMensaje("No existen colonias registradas para el CP " + codigoPostal);
                    List<ColoniaDTO> listaError = new ArrayList<>();
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
    
    public static DatosCPDTO obtenerMunicipioPorId(int idMunicipio) {
        DatosCPDTO respuesta = new DatosCPDTO();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                DatosCPDTO datos = conexionBD.selectOne("direccion.obtener-municipio", idMunicipio);

                if (datos != null) {
                    datos.setError(false);
                    datos.setMensaje("Municipio encontrado.");
                    respuesta = datos;
                } else {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró un municipio con ID " + idMunicipio);
                }

            } catch (Exception e) {
                respuesta.setError(true);
                respuesta.setMensaje("Error al consultar municipio: " + e.getMessage());
            }
            conexionBD.close();

        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No se pudo conectar a la base de datos.");
        }

        return respuesta;
    }

}
