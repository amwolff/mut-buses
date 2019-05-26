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

public class Main {
    private static final DefaultAPIClient defaultAPIClient = new DefaultAPIClient("17726468-47b2-466b-8ec1-4c99276dc9fa");
    private static final ImmutableStoreWithAzimuth store = new ImmutableStoreWithAzimuth();
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
        queriedLines.put("N01", 1);
        queriedLines.put("N02", 1);
        queriedLines.put("N03", 1);
        queriedLines.put("N11", 1);
        queriedLines.put("N12", 1);
        queriedLines.put("N13", 1);
        queriedLines.put("N14", 1);
        queriedLines.put("N16", 1);
        queriedLines.put("N21", 1);
        queriedLines.put("N22", 1);
        queriedLines.put("N24", 1);
        queriedLines.put("N25", 1);
        queriedLines.put("N31", 1);
        queriedLines.put("N32", 1);
        queriedLines.put("N33", 1);
        queriedLines.put("N34", 1);
        queriedLines.put("N35", 1);
        queriedLines.put("N36", 1);
        queriedLines.put("N37", 1);
        queriedLines.put("N38", 1);
        queriedLines.put("N41", 1);
        queriedLines.put("N42", 1);
        queriedLines.put("N43", 1);
        queriedLines.put("N44", 1);
        queriedLines.put("N45", 1);
        queriedLines.put("N46", 1);
        queriedLines.put("N50", 1);
        queriedLines.put("N52", 1);
        queriedLines.put("N56", 1);
        queriedLines.put("N58", 1);
        queriedLines.put("N61", 1);
        queriedLines.put("N62", 1);
        queriedLines.put("N63", 1);
        queriedLines.put("N64", 1);
        queriedLines.put("N66", 1);
        queriedLines.put("N71", 1);
        queriedLines.put("N72", 1);
        queriedLines.put("N81", 1);
        queriedLines.put("N83", 1);
        queriedLines.put("N85", 1);
        queriedLines.put("N88", 1);
        queriedLines.put("N91", 1);
        queriedLines.put("N95", 1);
    }
}
