package dominio;

import pojo.Colonia;
import utilidades.Geoapify;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import java.util.List;
import utilidades.Distancia;

public class ColoniaImp {

    public static double calcularDistancia(String cp1, String cp2) {
        SqlSession conexion = MyBatisUtil.getSession();

        if (conexion != null) {
            try {
                // Paso A: Obtener los objetos. Si la BD tiene nulos, este método va a la API y los llena.
                Colonia c1 = obtenerOActualizar(conexion, cp1);
                Colonia c2 = obtenerOActualizar(conexion, cp2);

                // Paso B: Imprimir en consola para verificar (Míralo en Output de Netbeans)
                System.out.println("CP " + cp1 + ": " + c1.getLatitud() + ", " + c1.getLongitud());
                System.out.println("CP " + cp2 + ": " + c2.getLatitud() + ", " + c2.getLongitud());

                // Paso C: Calcular. Ahora estamos seguros de que NO son null.
                return Distancia.calcular(
                    c1.getLatitud(), 
                    c1.getLongitud(), 
                    c2.getLatitud(), 
                    c2.getLongitud()
                );

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error: " + e.getMessage());
            } finally {
                conexion.close();
            }
        }
        throw new RuntimeException("Sin conexión a BD");
    }

    // ESTE ES EL MÉTODO MÁGICO QUE ARREGLA TU PROBLEMA
    private static Colonia obtenerOActualizar(SqlSession conexion, String cp) throws Exception {
        // 1. Buscamos en la BD
        List<Colonia> lista = conexion.selectList("colonia.obtenerCoordenadasPorCP", cp);
        
        if (lista.isEmpty()) {
            throw new Exception("El CP " + cp + " no existe en la tabla colonia.");
        }
        
        Colonia col = lista.get(0);

        // 2. DETECTAMOS SI ESTÁ VACÍO (NULL)
        // Como tú dices: "ningún cp tiene latitud ni longitud", entonces entrará siempre aquí.
        if (col.getLatitud() == null || col.getLongitud() == null) {
            
            System.out.println("--> El CP " + cp + " tiene coordenadas NULL. Buscando en API...");
            
            // 3. Vamos a la API
            try {
                double[] coordenadas = Geoapify.obtenerLatLon(cp);
                
                // 4. Actualizamos el objeto en memoria (para usarlo ya)
                col.setLatitud(coordenadas[0]);
                col.setLongitud(coordenadas[1]);
                col.setCodigoPostal(cp); // Aseguramos que el CP esté puesto para el update

                // 5. Guardamos en la BD para que la próxima vez no sea null
                int filasAfectadas = conexion.update("colonia.actualizarCoordenadas", col);
                conexion.commit(); // IMPORTANTE: Guardar cambios
                
                System.out.println("--> Base de datos actualizada para " + cp);

            } catch (Exception apiError) {
                System.out.println("Error API: " + apiError.getMessage());
                throw new Exception("No se pudieron obtener coordenadas para el CP " + cp);
            }
        }
        
        return col;
    }
}