package launcher;

import api.Server;
import client.DefaultAPIClient;
import daemon.SimpleFetcher;
import storage.ImmutableInMemoryStore;
import sun.misc.Signal;

import java.util.ArrayList;
import java.util.List;

import static api.Handlers.*;

public class Main { // it's only a sketch
    public static void main(String[] args) {
        final DefaultAPIClient defaultAPIClient = new DefaultAPIClient("17726468-47b2-466b-8ec1-4c99276dc9fa");
        final ImmutableInMemoryStore store = new ImmutableInMemoryStore();
        final List<String> queriedLines = new ArrayList<>();
        queriedLines.add("523");
        queriedLines.add("220");
        queriedLines.add("122");

        final Thread fetcherThread = new Thread(new SimpleFetcher(defaultAPIClient, store, queriedLines));
        fetcherThread.start();

        final Server srv = new Server(getHealthHandler(), getRoutesHandler(queriedLines), getVehiclesAllHandler(store));
        srv.initListenAndServe();

        Signal.handle(new Signal("INT"), signal -> {
            fetcherThread.interrupt();
            srv.shutdown();
        });

        try {
            fetcherThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
