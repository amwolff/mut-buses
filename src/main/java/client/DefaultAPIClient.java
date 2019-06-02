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

public final class DefaultAPIClient implements APIClient {
    private final Map<String, String> defaultParams;
    private final Gson gson;

    public DefaultAPIClient(String apiKey) {
        defaultParams = new HashMap<>();
        defaultParams.put("resource_id", "f2e5503e-927d-4ad3-9500-4ab9e55deb59");
        defaultParams.put("apikey", apiKey);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    public List<Vehicle> getVehicles(Integer type, String line, String brigade) throws IOException {
        final Map<String, String> params = new HashMap<>(defaultParams);
        params.put("type", type.toString());
        if (line != null) {
            params.put("line", line);
        }
        if (brigade != null) {
            params.put("brigade", brigade);
        }

        try (InputStream inputStream = APIClient.downloadData(params)) {
            final EndpointResult endpointResult = gson.fromJson(
                    new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)), EndpointResult.class);

            return endpointResult.getResult();
        } catch (IOException exc) {
            throw new IOException("Problem accessing api.um.warszawa.pl services", exc);
        }
    }

    @Override
    public List<Vehicle> getVehicles(Integer type, String lineNum) throws IOException {
        return getVehicles(type, lineNum, null);
    }

    @Override
    public List<Vehicle> getVehicles(Integer type) throws IOException {
        return getVehicles(type, null, null);
    }
}
