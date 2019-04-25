package daemon;

import client.APIClient;
import client.Vehicle;
import storage.VehicleStore;

import java.io.IOException;
import java.util.List;

public class SimpleFetcher implements Fetcher {
    private final APIClient client;
    private final VehicleStore store;

    public SimpleFetcher(APIClient client, VehicleStore store) {
        this.client = client;
        this.store = store;
    }

    @Override
    public void Run() throws IOException {
        for (String queriedLine : queriedLines) {
            List<Vehicle> data = client.getVehicles(1, queriedLine);
            for (Vehicle v : data) {
                // Period timeDiff = Period.between(LocalDate.now(), v.getTime().toInstant().atZone(ZoneId.of("Europe/Warsaw")).toLocalDate());
            }
        }
    }
}
