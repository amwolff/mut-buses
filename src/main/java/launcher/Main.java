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
        final ImmutableInMemoryStore db = new ImmutableInMemoryStore();
        final List<String> qLns = new ArrayList<>();

        qLns.add("N01");
        qLns.add("N02");
        qLns.add("N03");
        qLns.add("N11");
        qLns.add("N12");
        qLns.add("N13");
        qLns.add("N14");
        qLns.add("N16");
        qLns.add("N21");
        qLns.add("N22");
        qLns.add("N24");
        qLns.add("N25");
        qLns.add("N31");

        final Thread fetcherThread = new Thread(new SimpleFetcher(defaultAPIClient, db, qLns));
        fetcherThread.start();

        final Server srv = new Server(
                getHealthHandler(),
                getRoutesHandler(qLns),
                getVehiclesAllHandler(db),
                getUIHandler());

        srv.initListenAndServe();

        Signal.handle(new Signal("INT"), signal -> {
            srv.shutdown();
            fetcherThread.interrupt();
        });

        try {
            fetcherThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
