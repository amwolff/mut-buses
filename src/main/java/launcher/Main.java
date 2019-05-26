package launcher;

import api.Server;
import client.DefaultAPIClient;
import daemon.SimpleFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.ImmutableInMemoryStore;
import sun.misc.Signal;

import java.util.HashMap;
import java.util.Map;

import static api.Handlers.*;

public class Main {
    private static final DefaultAPIClient defaultAPIClient = new DefaultAPIClient("17726468-47b2-466b-8ec1-4c99276dc9fa");
    private static final ImmutableInMemoryStore store = new ImmutableInMemoryStore();
    private static final Map<String, Integer> queriedLines = new HashMap<>();
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        basicInit();

        final Thread fetcherThread = new Thread(new SimpleFetcher(defaultAPIClient, store, queriedLines));
        fetcherThread.start();

        final Server srv = new Server(getHealthHandler(), getRoutesHandler(queriedLines), getVehiclesAllHandler(store));
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
        queriedLines.put("523", 1);
        queriedLines.put("220", 1);
        queriedLines.put("122", 1);
        queriedLines.put("10", 2);
    }
}
