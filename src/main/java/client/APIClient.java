package client;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface APIClient {
    String baseURL = "https://api.um.warszawa.pl/api/action/busestrams_get/";

    private static String appendParameter(String url, String paramKey, String paramValue) {
        if (url == null || url.trim().isEmpty()) {
            return url;
        }
        if (url.endsWith("/")) {
            return url + "?" + paramKey + "=" + paramValue;
        }
        return url + "&" + paramKey + "=" + paramValue;
    }

    static InputStream downloadData(Map<String, String> requestParameters) throws IOException {
        String targetURL = baseURL;
        for (final Map.Entry<String, String> entry : requestParameters.entrySet()) {
            targetURL = appendParameter(targetURL, entry.getKey(), entry.getValue());
        }

        final HttpsURLConnection connection = (HttpsURLConnection) new URL(targetURL).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Cache-Control", "no-cache");

        return connection.getInputStream();
    }

    List<Vehicle> getVehicles(Integer type, String line, Integer brigade) throws IOException;

    List<Vehicle> getVehicles(Integer type, String line) throws IOException;

    List<Vehicle> getVehicles(Integer type) throws IOException;
}
