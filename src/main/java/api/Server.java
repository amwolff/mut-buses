package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Route;

import static spark.Spark.*;

public class Server {
    private final Route healthEndpoint;
    private final Route routesEndpoint;
    private final Route vehiclesEndpoint;
    private final Gson gson;

    public Server(Route healthEndpoint, Route routesEndpoint, Route vehiclesEndpoint) {
        this.healthEndpoint = healthEndpoint;
        this.routesEndpoint = routesEndpoint;
        this.vehiclesEndpoint = vehiclesEndpoint;
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public void initListenAndServe() {
        port(8080);

        staticFiles.location("/public");
        staticFiles.expireTime(Long.MAX_VALUE);

        get("/healthz", healthEndpoint);
        get("/routes", routesEndpoint, gson::toJson);
        get("/vehicles/all", vehiclesEndpoint, gson::toJson);

        // TODO: add handlers for specific lines' vehicles

        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
    }

    public void shutdown() {
        stop();
    }
}
