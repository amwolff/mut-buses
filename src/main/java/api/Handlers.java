package api;

import spark.Route;
import storage.VehicleStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Handlers provide functions for interfacing with API endpoints.
 */
public final class Handlers {
    private Handlers() {
    }

    public static Route getHealthHandler() {
        return (request, response) -> "OK";
    }

    public static Route getRoutesHandler(Map<String, Integer> queriedRoutes) {
        final List<InternalRoute> routeList = new ArrayList<>();
        queriedRoutes.forEach((r, t) -> routeList.add(new InternalRoute(r)));
        routeList.sort(Comparator.comparing(InternalRoute::getRoute));

        return (request, response) -> routeList;
    }

    public static Route getVehiclesAllHandler(VehicleStore store) {
        return (request, response) -> store.retrieveAll();
    }
}
