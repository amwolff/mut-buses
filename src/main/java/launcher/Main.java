package launcher;

import api.Server;
import client.DefaultAPIClient;
import daemon.SimpleFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.ImmutableInMemoryStore;
import sun.misc.Signal;

import java.util.ArrayList;
import java.util.List;

import static api.Handlers.*;

public class Main {
    private static final DefaultAPIClient defaultAPIClient = new DefaultAPIClient("17726468-47b2-466b-8ec1-4c99276dc9fa");
    private static final ImmutableInMemoryStore store = new ImmutableInMemoryStore();
    private static final List<String> queriedLines = new ArrayList<>();
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
        //type = 1
        queriedLines.add("523");
        queriedLines.add("220");
        queriedLines.add("122");
        queriedLines.add("154");
        queriedLines.add("320");
        queriedLines.add("N45");
        queriedLines.add("N95");
        //tramwaje type = 2
        queriedLines.add("20");
    }
}
