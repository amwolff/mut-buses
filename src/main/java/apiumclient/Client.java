package apiumclient;

import java.util.List;

public interface Client {
    String baseURL = "https://api.um.warszawa.pl/api/action/busestrams_get/?resource_id=f2e5503e-927d-4ad3-9500-4ab9e55deb59";

    List<Vehicle> getVehicles(Integer lineNum);

    List<Vehicle> getVehicles(Integer lineNum, Integer brigadeNum);
}
