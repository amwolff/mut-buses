package launcher;

import client.DefaultAPIClient;
import daemon.SimpleFetcher;
import storage.ImmutableInMemoryStore;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DefaultAPIClient defaultAPIClient = new DefaultAPIClient("");
        ImmutableInMemoryStore inMemoryVehicleStore = new ImmutableInMemoryStore();
        List<String> qLns = new ArrayList<>();
        qLns.add("220");
        SimpleFetcher simpleFetcher = new SimpleFetcher(defaultAPIClient, inMemoryVehicleStore, qLns);
        simpleFetcher.run();
    }
}
