package storage;

import client.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Math.*;

public class ImmutableStoreWithAzimuth implements VehicleStore {
    private final Map<String, List<Vehicle>> backend;
    private final ReadWriteLock arrayMtx;

    public ImmutableStoreWithAzimuth(Map<String, Integer> queriedLines) {
        backend = new HashMap<>();
        queriedLines.forEach((k, v) -> backend.put(k, new ArrayList<>()));

        arrayMtx = new ReentrantReadWriteLock();
    }

    private static boolean shallowCompareVehicles(Vehicle v1, Vehicle v2) {
        return v1.getLines().equals(v2.getLines()) && v1.getBrigade().equals(v2.getBrigade());
    }

    static List<Vehicle> removeDuplicates(List<Vehicle> duplicated) {
        final List<Vehicle> deduplicated = new ArrayList<>();
        outerLoop:
        for (final Vehicle v1 : duplicated) {
            for (int i = 0; i < deduplicated.size(); i++) {
                final Vehicle v2 = deduplicated.get(i);
                if (shallowCompareVehicles(v1, v2)) {
                    if (v1.getTime().getTime() > v2.getTime().getTime()) {
                        deduplicated.set(i, v1);
                    }
                    continue outerLoop;
                }
            }
            deduplicated.add(v1);
        }
        return deduplicated;
    }

    static double calculateAzimuth(double lat1, double lon1, double lat2, double lon2) {
        // Δλ = λ₂ - λ₁
        // θ = atan2 [(sin Δλ ⋅ cos φ₂), (cos φ₁ ⋅ sin φ₂ − sin φ₁ ⋅ cos φ₂ ⋅ cos Δλ)]
        final double d = toRadians(lon2 - lon1);
        final double radLat2 = toRadians(lat2);
        final double radLat1 = toRadians(lat1);
        return toDegrees(atan2(sin(d) * cos(radLat2), (cos(radLat1) * sin(radLat2)) - (sin(radLat1) * cos(radLat2) * cos(d))));
    }

    @Override
    public void insert(Vehicle vehicle) {
        // implement me!
    }

    @Override
    public void insert(List<Vehicle> vehicles) {
        final List<Vehicle> tantamount = new ArrayList<>(removeDuplicates(vehicles));
        final String getPutKey = tantamount.get(0).getLines();

        final List<Vehicle> previousInsert = backend.get(getPutKey);
        final List<Vehicle> remaining = new ArrayList<>(tantamount);

        final List<Vehicle> withAzimuth = new ArrayList<>();
        tantamount.forEach(v -> {
            arrayMtx.readLock().lock();
            previousInsert.stream().filter(p -> shallowCompareVehicles(v, p)).forEach(p -> {
                if (!(p.getLat() == v.getLat() && p.getLon() == v.getLon())) {
                    withAzimuth.add(new Vehicle(v, calculateAzimuth(p.getLat(), p.getLon(), v.getLat(), v.getLon())));
                } else {
                    withAzimuth.add(new Vehicle(v, p.getAzimuth()));
                }
                remaining.remove(v);
            });
            arrayMtx.readLock().unlock();
        });

        withAzimuth.addAll(remaining); // add the rest from the vehicle list

        arrayMtx.writeLock().lock();
        previousInsert.clear();
        previousInsert.addAll(withAzimuth);
        arrayMtx.writeLock().unlock();
    }

    @Override
    public void clear(String line) {
        final List<Vehicle> vehicles = backend.get(line);
        arrayMtx.writeLock().lock();
        vehicles.clear();
        arrayMtx.writeLock().unlock();
    }

    @Override
    public List<Vehicle> retrieveAll() {
        final List<Vehicle> ret = new ArrayList<>();
        arrayMtx.readLock().lock();
        backend.values().forEach(ret::addAll);
        arrayMtx.readLock().unlock();
        return ret;
    }

    @Override
    public List<Vehicle> retrieve(String line) {
        // implement me!
        return null;
    }

    @Override
    public Vehicle retrieve(String line, String brigade) {
        // implement me!
        return null;
    }
}
