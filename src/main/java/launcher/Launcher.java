package launcher;

import api.Server;
import client.DefaultAPIClient;
import daemon.SimpleFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.ImmutableStoreWithAzimuth;
import sun.misc.Signal;

import java.util.HashMap;
import java.util.Map;

import static api.Handlers.*;

/**
 * Launcher allocates required resources and starts the application.
 */
public class Launcher {
    private static final DefaultAPIClient defaultAPIClient = new DefaultAPIClient("17726468-47b2-466b-8ec1-4c99276dc9fa");
    private static final Map<String, Integer> queriedRoutes = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {
        basicInit();

        final ImmutableStoreWithAzimuth store = new ImmutableStoreWithAzimuth(queriedRoutes);

        final Thread fetcherThread = new Thread(new SimpleFetcher(defaultAPIClient, store, queriedRoutes));
        fetcherThread.start();

        final Server srv = new Server(getHealthHandler(), getRoutesHandler(queriedRoutes), getVehiclesAllHandler(store));
        srv.initListenAndServe();

        Signal.handle(new Signal("INT"), signal -> {
            LOG.info("Received termination signal (shutting down)");
            fetcherThread.interrupt();
            srv.shutdown();
        });

        try {
            fetcherThread.join();
        } catch (InterruptedException e) {
            LOG.error("fetcherThread.join", e);
        }
    }

    private static void basicInit() {
        // TODO: move to config file
        queriedRoutes.put("523", 1);
    }
}
