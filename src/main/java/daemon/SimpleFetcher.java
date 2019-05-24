package daemon;

import client.APIClient;
import client.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.VehicleStore;

import java.util.Date;
import java.util.List;

public final class SimpleFetcher implements Fetcher, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleFetcher.class);
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
            if ((gpsTimeDiff > 0) && (gpsTimeDiff < callEveryMs)) {
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
    public void run() {
        while (true) {
            long partialTimeDifferenceSum = 0;
            for (final String q : queriedLines) {
                List<Vehicle> collectedVehicles = null;

                try {
                    collectedVehicles = client.getVehicles(1, q);
                } catch (Throwable t) {
                    LOG.error("{}: client.getVehicles", q, t);
                    continue;
                }

                if (collectedVehicles != null && !collectedVehicles.isEmpty()) {
                    final long partialTimeDifference = calculatePartialTimeDifference(collectedVehicles);
                    LOG.debug("{}: partialTimeDifference is {}", q, partialTimeDifference);

                    partialTimeDifferenceSum += partialTimeDifference;

                    store.insert(collectedVehicles);
                    LOG.info("{}: inserted {} item(s) into the database", q, collectedVehicles.size());
                } else {
                    LOG.warn("{}: collectedVehicles is null or zero-length", q);
                    // TODO: clear data for {q} key in the database
                }
            }

            final long calculatedSleepDuration = calculateTotalDelay(partialTimeDifferenceSum, queriedLines.size());
            LOG.info("Will now wait {}ms", calculatedSleepDuration);
            try {
                Thread.sleep(calculatedSleepDuration);
            } catch (InterruptedException e) {
                LOG.warn("Thread.sleep", e);
                LOG.info("Terminating fetch daemon");
                return;
            }
        }
    }
}
