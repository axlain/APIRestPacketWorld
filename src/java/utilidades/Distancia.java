package utilidades;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Distancia {
    private static final String URL_API = "http://sublimas.com.mx:8080/calculadora/api/envios/distancia/";
    public static double obtenerDistancia(String cpOrigen, String cpDestino) {
        try {
            String urlStr = URL_API + cpOrigen + "," + cpDestino;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error HTTP: " + conn.getResponseCode());
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder jsonResponse = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                jsonResponse.append(line);
            }

            in.close();
            conn.disconnect();

            JsonObject json = JsonParser.parseString(jsonResponse.toString()).getAsJsonObject();
            if (json.get("error").getAsBoolean()) {
                throw new RuntimeException("API devolvió error: " + json.get("mensaje").getAsString());
            }

            return json.get("distanciaKM").getAsDouble();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo obtener la distancia: " + e.getMessage());
        }
    }
}
