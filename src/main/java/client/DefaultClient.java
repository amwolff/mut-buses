package client;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultClient implements Client {
    private Map<String, String> defaultParams = new HashMap<>();

    public DefaultClient(String apiKey) {
        defaultParams.put("resource_id", "f2e5503e-927d-4ad3-9500-4ab9e55deb59");
        defaultParams.put("apikey", apiKey);
    }

    @Override
    public List<Vehicle> getVehicles(Integer type, Integer line, Integer brigade) throws IOException {
        Map<String, String> params = new HashMap<>(defaultParams);
        params.put("type", type.toString());
        if (line != null) {
            params.put("line", line.toString());
        }
        if (brigade != null) {
            params.put("brigade", brigade.toString());
        }

        return new Gson().fromJson(
                new JsonReader(new InputStreamReader(Client.downloadData(params), StandardCharsets.UTF_8)),
                Vehicle.class
        );
    }

    @Override
    public List<Vehicle> getVehicles(Integer type, Integer lineNum) throws IOException {
        return getVehicles(type, lineNum, null);
    }

    @Override
    public List<Vehicle> getVehicles(Integer type) throws IOException {
        return getVehicles(type, null, null);
    }
}
