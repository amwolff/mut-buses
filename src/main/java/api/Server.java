package api;

import spark.Route;

public class Server {
    private final Route healthEndpoint;
    private final Route routesEndpoint;
    private final Route vehiclesEndpoint;
    private final Route uiEndpoint;

    public Server(Route healthEndpoint, Route routesEndpoint, Route vehiclesEndpoint, Route uiEndpoint) {
        this.healthEndpoint = healthEndpoint;
        this.routesEndpoint = routesEndpoint;
        this.vehiclesEndpoint = vehiclesEndpoint;
        this.uiEndpoint = uiEndpoint;
    }

    public void initListenAndServe() { }
}
