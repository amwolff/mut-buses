package daemon;

import client.APIClient;
import client.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.VehicleStore;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * SimpleFetcher holds basic Fetcher implementation.
 * It's designed to download large amounts of data while keeping low memory footprint of the api.um.warszawa.pl web
 * service.
 */
public final class SimpleFetcher implements Fetcher, Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleFetcher.class);
    private final APIClient client;
    private final VehicleStore store;
    private final Map<String, Integer> queriedRoutes;

    public SimpleFetcher(APIClient client, VehicleStore store, Map<String, Integer> queriedRoutes) {
        this.client = client;
        this.store = store;
        this.queriedRoutes = queriedRoutes;
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

    private static long calculateTotalDelay(long PartialTotal, int queriedRoutesSize) {
        return callEveryMs - Math.floorDiv(PartialTotal, (long) queriedRoutesSize);
    }

    /**
     * run of SimpleFetcher retrieves all available Vehicles from the queried
     * routes periodically. It fetches the data, inserts it into the database
     * (store) and waits calculated interval. Interval is calculated as follows:
     * 1. For each Vehicle:
     * 1.1 Calculate difference between current and the GPS time.
     * 1.2 Store the difference.
     * 2. Divide the sum of differences by the number of fetched of Vehicles.
     * 3. Sleep time equals GPS Refresh Duration minus the divided sum.
     */
    @Override
    public void run() {
        if (queriedRoutes.isEmpty()) {
            LOG.warn("Nothing to query... Exiting");
            return;
        }

        while (true) {
            long partialTimeDifferenceSum = 0;
            for (final Map.Entry<String, Integer> q : queriedRoutes.entrySet()) {
                final List<Vehicle> collectedVehicles;

                try {
                    collectedVehicles = client.getVehicles(q.getValue(), q.getKey());
                } catch (Throwable t) {
                    LOG.error("{}: client.getVehicles", q.getKey(), t);
                    continue;
                }

                if (collectedVehicles != null && !collectedVehicles.isEmpty()) {
                    final long partialTimeDifference = calculatePartialTimeDifference(collectedVehicles);
                    LOG.debug("{}: partialTimeDifference is {}", q.getKey(), partialTimeDifference);

                    partialTimeDifferenceSum += partialTimeDifference;

                    store.insert(collectedVehicles);
                    LOG.info("{}: inserted approx. {} item(s) into the database", q.getKey(), collectedVehicles.size());
                } else {
                    LOG.warn("{}: collectedVehicles is null or zero-length", q.getKey());
                    store.clear(q.getKey());
                }
            }

            final long calculatedSleepDuration = calculateTotalDelay(partialTimeDifferenceSum, queriedRoutes.size());
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
