package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultClient implements Client {
    private final Map<String, String> defaultParams = new HashMap<>();

    public DefaultClient(String apiKey) {
        defaultParams.put("resource_id", "f2e5503e-927d-4ad3-9500-4ab9e55deb59");
        defaultParams.put("apikey", apiKey);
    }

    @Override
    public final List<Vehicle> getVehicles(Integer type, Integer line, Integer brigade) throws IOException {
        Map<String, String> params = new HashMap<>(defaultParams);
        params.put("type", type.toString());
        if (line != null) {
            params.put("line", line.toString());
        }
        if (brigade != null) {
            params.put("brigade", brigade.toString());
        }

        try (InputStream inputStream = Client.downloadData(params)) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
            EndpointResult vehiclesResult = gson.fromJson(
                    new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)),
                    EndpointResult.class
            );
            return vehiclesResult.getResult();
        } catch (IOException exc) {
            throw new IOException("Problem accessing api.um.warszawa.pl services", exc);
        }
    }

    @Override
    public final List<Vehicle> getVehicles(Integer type, Integer lineNum) throws IOException {
        return getVehicles(type, lineNum, null);
    }

    @Override
    public final List<Vehicle> getVehicles(Integer type) throws IOException {
        return getVehicles(type, null, null);
    }
}
