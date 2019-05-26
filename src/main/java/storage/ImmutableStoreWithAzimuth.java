package storage;

import client.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.*;

public final class ImmutableStoreWithAzimuth implements VehicleStore {
    private final Map<String, CopyOnWriteArrayList<Vehicle>> backend = new ConcurrentHashMap<>();

    static double calculateAzimuth(double lat1, double lon1, double lat2, double lon2) {
        // Δλ = λ₂ - λ₁
        // θ = atan2 [(sin Δλ ⋅ cos φ₂), (cos φ₁ ⋅ sin φ₂ − sin φ₁ ⋅ cos φ₂ ⋅ cos Δλ)]
        final double d = toRadians(lon2 - lon1);
        final double radLat2 = toRadians(lat2);
        final double radLat1 = toRadians(lat1);
        return toDegrees(atan2(sin(d) * cos(radLat2), (cos(radLat1) * sin(radLat2)) - (sin(radLat1) * cos(radLat2) * cos(d))));
    }

    private static boolean shallowCompareVehicles(Vehicle v1, Vehicle v2) {
        return v1.getLines().equals(v2.getLines()) && v1.getBrigade() == v2.getBrigade();
    }

    @Override
    public void insert(Vehicle vehicle) {
        final CopyOnWriteArrayList<Vehicle> replacement = new CopyOnWriteArrayList<>();
        replacement.add(vehicle);
        backend.put(vehicle.getLines(), replacement);
    }

    @Override
    public void insert(List<Vehicle> vehicles) {
        final List<Vehicle> remaining = new ArrayList<>(vehicles);
        final String getPutKey = remaining.get(0).getLines();
        final List<Vehicle> previousInsert = backend.getOrDefault(getPutKey, new CopyOnWriteArrayList<>());
        final List<Vehicle> withAzimuth = new ArrayList<>();
        vehicles.forEach(v -> previousInsert.forEach(p -> {
            if (shallowCompareVehicles(v, p)) {
                if (!(p.getLat() == v.getLat() && p.getLon() == v.getLon())) {
                    withAzimuth.add(new Vehicle(v, calculateAzimuth(p.getLat(), p.getLon(), v.getLat(), v.getLon())));
                } else {
                    withAzimuth.add(new Vehicle(v, p.getAzimuth()));
                }
                remaining.remove(v);
            }
        }));
        withAzimuth.addAll(remaining); // add the rest from the vehicle list
        backend.put(getPutKey, new CopyOnWriteArrayList<>(withAzimuth));
    }

    @Override
    public void clear(String line) {
        backend.getOrDefault(line, new CopyOnWriteArrayList<>()).clear();
    }

    @Override
    public List<Vehicle> retrieveAll() {
        final List<Vehicle> ret = new ArrayList<>();
        backend.values().forEach(ret::addAll);
        return ret;
    }

    @Override
    public List<Vehicle> retrieve(String line) {
        return new ArrayList<>(backend.get(line));
    }

    @Override
    public Vehicle retrieve(String line, Integer brigade) {
        final List<Vehicle> vehicles = backend.get(line);
        for (final Vehicle vehicle : vehicles) {
            if (vehicle.getBrigade() == brigade) {
                return vehicle;
            }
        }
        return null;
    }
}
