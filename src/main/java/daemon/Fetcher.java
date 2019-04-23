package daemon;

import database.VehicleStore;

public interface Fetcher {
    void Run(VehicleStore store);
}
