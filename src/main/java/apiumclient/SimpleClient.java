package apiumclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SimpleClient implements Client {

    private final static Logger logger = LogManager.getLogger(SimpleClient.class.getName());
    private Map<String, String> defaultParams = new TreeMap<>();

    private SimpleClient(String apiKey) {
        defaultParams.put("apikey", apiKey);
    }

    private static String appendParameter(String url, String paramKey, String paramValue) {
        if (url == null || url.trim().isEmpty()) {
            return url;
        }
        if (url.endsWith("/")) {
            return url + "?" + paramKey + "=" + paramValue;
        }
        return url + "&" + paramKey + "=" + paramValue;
    }

    private static InputStream downloadData(Map<String, String> requestParameters) throws IOException {
        String targetURL = baseURL;
        for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
            targetURL = SimpleClient.appendParameter(targetURL, entry.getKey(), entry.getValue());
        }

        HttpsURLConnection connection = null;
        try {
            connection = (HttpsURLConnection) new URL(targetURL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Cache-Control", "no-cache");

            return connection.getInputStream();
        } catch (IOException e) {
            throw new IOException("Error connecting to the service", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    public List<Vehicle> getVehicles(Integer lineNum) {
        try {
            Map<String, String> params = new TreeMap<>(defaultParams);
            params.put("type", "1");
            params.put("line", lineNum.toString());

            JsonReader reader = new JsonReader(new InputStreamReader(SimpleClient.downloadData(params), StandardCharsets.UTF_8));
            reader.beginArray();

            Gson gson = new GsonBuilder().create();
            List<Vehicle> vehicles = new LinkedList<>();
            while (reader.hasNext()) {
                vehicles.add(gson.fromJson(reader, Vehicle.class));
            }

            return vehicles;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Vehicle> getVehicles(Integer lineNum, Integer brigadeNum) {
        return null;
    }
}
