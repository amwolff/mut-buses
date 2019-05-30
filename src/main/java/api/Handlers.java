package api;

import spark.Route;
import storage.VehicleStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class Handlers {
    private Handlers() {
    }

    public static Route getHealthHandler() {
        return (request, response) -> "OK";
    }

    public static Route getRoutesHandler(Map<String, Integer> queriedRoutes) {
        final List<InternalRoute> routeList = new ArrayList<>();
        queriedRoutes.forEach((r, t) -> routeList.add(new InternalRoute(r)));

        return (request, response) -> routeList;
    }

    public static Route getVehiclesAllHandler(VehicleStore store) {
        return (request, response) -> store.retrieveAll();
    }
}
