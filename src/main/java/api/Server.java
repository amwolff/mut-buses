package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Route;

import static spark.Spark.*;

public class Server {
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
        port(8080);

        staticFiles.location("/public");
        staticFiles.expireTime(31536000);

        notFound((request, response) -> {
            response.redirect("/");
            return null;
        });

        get("/healthz", healthHandler);
        get("/favicon.ico", (request, response) -> "");

        path("/api", () -> {
            get("/routes", routesHandler, gson::toJson);
            path("/vehicles", () -> {
                get("/all", vehiclesAllHandler, gson::toJson);
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
        stop();
    }
}
