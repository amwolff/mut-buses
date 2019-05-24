package api;

import spark.Route;
import storage.VehicleStore;

import java.util.ArrayList;
import java.util.List;

public final class Handlers {
    private Handlers() {
    }

    public static Route getHealthHandler() {
        return (request, response) -> "OK";
    }

    public static Route getUIHandler() {
        return (request, response) -> "Implement me!";
    }

    public static Route getRoutesHandler(List<String> availableRoutes) {
        final List<InternalRoute> routeList = new ArrayList<>();
        availableRoutes.forEach(r -> {
            routeList.add(new InternalRoute(r));
        });

        return (request, response) -> routeList;
    }

    public static Route getVehiclesAllHandler(VehicleStore store) {
        return (request, response) -> store.retrieveAll();
    }
}
