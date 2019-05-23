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

    public static long calculatePartialDelay(List<Vehicle> vehicleList) {
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

    public static long calculateTotalDelay(long PartialTotal, int queriedLinesNumber) {
        return callEveryMs - Math.floorDiv(PartialTotal, (long) queriedLinesNumber);
    }

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

                if (collectedVehicles != null) {
                    partialSleepDuration += calculatePartialDelay(collectedVehicles);
                    store.insert(collectedVehicles);
                } else {
                    System.out.println("collectedVehicles is null");
                }
            }

            final long totalSleepDuration = calculateTotalDelay(partialSleepDuration, queriedLines.size());
            System.out.printf("Will now wait %dms\n", totalSleepDuration);
            try {
                Thread.sleep(totalSleepDuration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
