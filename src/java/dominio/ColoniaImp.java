package dominio;

import pojo.Colonia;
import utilidades.Geoapify;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import java.util.List;
import utilidades.Constantes;
import utilidades.Distancia;

public class ColoniaImp {

    public static double calcularDistancia(String cpOrigen, String cpDestino) {

        SqlSession conexionBD = MyBatisUtil.getSession();
        double distancia;

        if (conexionBD != null) {
            try {
                Colonia origen = obtenerOActualizarColonia(conexionBD, cpOrigen);
                Colonia destino = obtenerOActualizarColonia(conexionBD, cpDestino);

                distancia = Distancia.calcular(
                        origen.getLatitud(),
                        origen.getLongitud(),
                        destino.getLatitud(),
                        destino.getLongitud()
                );

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al calcular distancia: " + e.getMessage());
            }
            conexionBD.close();
        } else {
            throw new RuntimeException(Constantes.MSJ_ERROR_BD);
        }

        return distancia;
    }

    private static Colonia obtenerOActualizarColonia(SqlSession conexion, String cp) throws Exception {

        List<Colonia> lista = conexion.selectList("colonia.obtener-coordenadas-por-cp",cp);

        if (lista.isEmpty()) {
            throw new Exception("El codigo postal " + cp + " no existe en la base de datos.");
        }

        Colonia colonia = lista.get(0);

        // Si las coordenadas son nulas se consulta la API
        if (colonia.getLatitud() == null || colonia.getLongitud() == null) {

            double[] coordenadas = Geoapify.obtenerLatLon(cp);

            colonia.setLatitud(coordenadas[0]);
            colonia.setLongitud(coordenadas[1]);
            colonia.setCodigoPostal(cp);

            conexion.update("colonia.actualizar-coordenadas",colonia);
            conexion.commit();
        }

        return colonia;
    }
}