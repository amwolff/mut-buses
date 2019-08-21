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
        queriedRoutes.put("L-6", 1);
        queriedRoutes.put("10", 2);
        queriedRoutes.put("20", 2);
        queriedRoutes.put("23", 2);
        queriedRoutes.put("24", 2);
        queriedRoutes.put("26", 2);
        queriedRoutes.put("28", 2);
        queriedRoutes.put("109", 1);
        queriedRoutes.put("112", 1);
        queriedRoutes.put("122", 1);
        queriedRoutes.put("149", 1);
        queriedRoutes.put("154", 1);
        queriedRoutes.put("171", 1);
        queriedRoutes.put("184", 1);
        queriedRoutes.put("190", 1);
        queriedRoutes.put("220", 1);
        queriedRoutes.put("523", 1);
        queriedRoutes.put("N01", 1);
        queriedRoutes.put("N43", 1);
        queriedRoutes.put("N45", 1);
        queriedRoutes.put("N95", 1);
    }
}
