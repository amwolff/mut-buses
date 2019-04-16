package apiumclient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Client {

    private final static Logger logger = LogManager.getLogger(Client.class.getName());

    private Client() {

    }

    public List<Vehicle> getVehicles(int lineNum) {
        return null;
    }

    public List<Vehicle> getVehicles(int lineNum, int brigadeNum) {
        return null;
    }

    private static URI appendUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);

        String newQuery = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = appendQuery;
        } else {
            newQuery += "&" + appendQuery;
        }

        return new URI(oldUri.getScheme(), oldUri.getAuthority(), oldUri.getPath(), newQuery, oldUri.getFragment());
    }

    private String downloadData(Map<String, String> requestParameters) throws IOException, URISyntaxException {
        URI targetURI = new URI("https://api.um.warszawa.pl/api/action/busestrams_get/");
        for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
            targetURI = Client.appendUri(targetURI.toString(), entry.getKey() + "=" + entry.getValue());
        }

        HttpURLConnection connection = null;
        try {
            final URL url = targetURI.toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Cache-Control", "no-cache");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.close();

            final InputStream stream = connection.getInputStream();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            final StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append("\n");
            }
            reader.close();

            return response.toString();
        } catch (IOException e) {
            throw new IOException("Error connecting to service", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static void main(String[] args) {
        Client c = new Client();

        logger.error("LOG IS CONFIGURE CORRECTLY!");

        Map<String, String> map = new TreeMap<>();
        map.put("sa", "f2e5503e-927d-4ad3-9500-4ab9e55deb59");
        map.put("apikey", "---");
        map.put("type", "1");
        map.put("line", "523");
        try {
            String str = c.downloadData(map);
            System.out.println(str);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
