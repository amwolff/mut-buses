package api;

import client.DefaultAPIClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import static spark.Spark.*;

public class Server {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAPIClient.class);

    private static final int PORT_VALUE = 8080;

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
        port(PORT_VALUE);
        LOG.info("Port set to {}", PORT_VALUE);

        staticFiles.location("/public");
        staticFiles.expireTime(Long.MAX_VALUE);

        get("/healthz", healthEndpoint);
        LOG.info("initialization of {} endopint", healthEndpoint);

        get("/routes", routesEndpoint, gson::toJson);
        LOG.info("initialization of {} endopint", healthEndpoint);

        get("/vehicles/all", vehiclesEndpoint, gson::toJson);
        LOG.info("initialization of {} endopint", vehiclesEndpoint);

        // TODO: add handlers for specific lines' vehicles

        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
    }

    public void shutdown() {
        LOG.warn("server will be turned off!");
        stop();
    }
}
