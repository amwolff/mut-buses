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

        get("/healthz", healthEndpoint);
        get("/favicon.ico", (request, response) -> "");

        path("/api", () -> {
            get("/routes", routesEndpoint, gson::toJson);
            path("/vehicles", () -> {
                get("/all", vehiclesEndpoint, gson::toJson);
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
