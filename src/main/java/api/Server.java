package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import static spark.Spark.*;

public class Server {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private static final int PORT_VALUE = 8080;

    private static final String PUBLIC_PATH = "/public";
    private static final String HEALTHZ_PATH = "/healthz";
    private static final String FAVICON_PATH = "/favicon.ico";
    private static final String API_PATH = "/api";
    private static final String ROUTES_PATH = "/routes";
    private static final String VEHICLES_PATH = "/vehicles";
    private static final String ALL_PATH = "/all";

    private final Route healthHandler;
    private final Route routesHandler;
    private final Route vehiclesAllHandler;
    private final Gson gson;

    public Server(Route healthHandler, Route routesHandler, Route vehiclesAllHandler) {
        this.healthHandler = healthHandler;
        this.routesHandler = routesHandler;
        this.vehiclesAllHandler = vehiclesAllHandler;
        gson = new GsonBuilder().setDateFormat("MM-dd HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();
    }

    public void initListenAndServe() {
        port(PORT_VALUE);
        LOG.info("Port set to {}", PORT_VALUE);

        staticFiles.location(PUBLIC_PATH);
        staticFiles.expireTime(31536000);

        notFound((request, response) -> {
            response.redirect("/");
            return null;
        });

        get(HEALTHZ_PATH, healthHandler);
        LOG.info("initialization of {} endopint", healthHandler);

        get(FAVICON_PATH, (request, response) -> "");

        path(API_PATH, () -> {
            get(ROUTES_PATH, routesHandler, gson::toJson);
            LOG.info("initialization of {} endopint", routesHandler);

            path(VEHICLES_PATH, () -> {
                get(ALL_PATH, vehiclesAllHandler, gson::toJson);
                LOG.info("initialization of {} endopint", vehiclesAllHandler);

                // TODO: add handlers for specific lines' vehicles
            });
            after("/*", (request, response) -> {
                response.header("Cache-Control", "no-cache, no-store, must-revalidate");
                response.header("Content-Type", "application/json");
            });
        });

        after((request, response) -> {
            response.header("Content-Encoding", "gzip");
        });
    }

    public void shutdown() {
        LOG.warn("server will be turned off!");
        stop();
    }
}
