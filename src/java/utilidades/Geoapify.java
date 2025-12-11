package utilidades;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Geoapify {
    // Tu API KEY
    private static final String API_KEY = "4dc655ca24d344bfbf2777d9127d9241"; 

    public static double[] obtenerLatLon(String cp) throws Exception {
        
        // --- CAMBIO CLAVE AQUÍ ---
        // En lugar de usar "?text=", usamos "?postcode=" y "&country="
        // Esto fuerza a la API a buscar exactamente el CP, sin adivinar.
        String urlString = "https://api.geoapify.com/v1/geocode/search?postcode=" 
                           + cp.trim() + "&country=Mexico&apiKey=" + API_KEY;

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new Exception("Error HTTP " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String linea;
        while ((linea = br.readLine()) != null) sb.append(linea);
        br.close();

        // Parseo
        JsonObject json = JsonParser.parseString(sb.toString()).getAsJsonObject();
        
        if (!json.has("features")) throw new Exception("Respuesta inválida de la API");
        
        JsonArray features = json.getAsJsonArray("features");
        
        // Si esto sigue saliendo 0, es que de verdad el CP no existe en Geoapify
        if (features.size() == 0) {
            throw new Exception("El CP " + cp + " no existe en los mapas de Geoapify.");
        }

        JsonObject props = features.get(0).getAsJsonObject().getAsJsonObject("properties");
        
        return new double[]{ 
            props.get("lat").getAsDouble(), 
            props.get("lon").getAsDouble() 
        };
    }
}