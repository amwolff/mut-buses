package daemon;

import client.APIClient;
import client.Vehicle;
import storage.VehicleStore;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public final class SimpleFetcher implements Fetcher, Runnable {
    private final APIClient client;
    private final VehicleStore store;
    private final List<String> queriedLines;

    public SimpleFetcher(APIClient client, VehicleStore store, List<String> queriedLines) {
        this.client = client;
        this.store = store;
        this.queriedLines = queriedLines;
    }

    private static long calculatePartialTimeDifference(List<Vehicle> vehicleList) {
        final Date now = new Date();
        long gpsTimeDiffTotal = 0;
        long eligibleVehicles = 0;
        for (final Vehicle v : vehicleList) {
            final long gpsTimeDiff = now.getTime() - v.getTime().getTime();
            if (gpsTimeDiff < callEveryMs) {
                gpsTimeDiffTotal += gpsTimeDiff;
                eligibleVehicles++;
            }
        }
        if (eligibleVehicles > 0) {
            return Math.floorDiv(gpsTimeDiffTotal, eligibleVehicles);
        }
        return 0;
    }

    private static long calculateTotalDelay(long PartialTotal, int queriedLinesNumber) {
        return callEveryMs - Math.floorDiv(PartialTotal, (long) queriedLinesNumber);
    }

    // run of SimpleFetcher retrieves all available Vehicles from the queried
    // lines periodically. It fetches the data, inserts it into the database
    // (store) and waits calculated interval. Interval is calculated as follows:
    //  1. For each Vehicle:
    //      1.1 Calculate difference between current and the GPS time.
    //      1.2 Store the difference.
    //  2. Divide the sum of differences by the number of fetched of Vehicles.
    //  3. Sleep time equals GPS Refresh Duration minus the divided sum.
    @Override
    public void run() { // TODO: improve logging
        while (true) {
            long partialSleepDuration = 0;
            for (final String q : queriedLines) {
                List<Vehicle> collectedVehicles = null;

                try {
                    collectedVehicles = client.getVehicles(1, q);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (collectedVehicles != null && !collectedVehicles.isEmpty()) {
                    partialSleepDuration += calculatePartialTimeDifference(collectedVehicles);
                    store.insert(collectedVehicles);
                } else {
                    System.out.println("collectedVehicles is null or zero-length");
                }
            }

            final long totalSleepDuration = calculateTotalDelay(partialSleepDuration, queriedLines.size());
            System.out.printf("Will now wait %dms\n", totalSleepDuration);
            try {
                Thread.sleep(totalSleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
