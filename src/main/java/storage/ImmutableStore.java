package storage;

import client.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ImmutableStore implements VehicleStore {
    private final Map<String, CopyOnWriteArrayList<Vehicle>> backend = new ConcurrentHashMap<>();

    @Override
    public void insert(Vehicle vehicle) {
        final CopyOnWriteArrayList<Vehicle> replacement = new CopyOnWriteArrayList<>();
        replacement.add(vehicle);
        backend.put(vehicle.getLines(), replacement);
    }

    @Override
    public void insert(List<Vehicle> vehicles) {
        backend.put(vehicles.get(0).getLines(), new CopyOnWriteArrayList<>(vehicles));
    }

    @Override
    public void clear(String route) {
        backend.getOrDefault(route, new CopyOnWriteArrayList<>()).clear();
    }

    @Override
    public List<Vehicle> retrieveAll() {
        final List<Vehicle> ret = new ArrayList<>();
        backend.values().forEach(ret::addAll);
        return ret;
    }

    @Override
    public List<Vehicle> retrieve(String route) {
        return new ArrayList<>(backend.get(route));
    }

    @Override
    public Vehicle retrieve(String route, String tripID) {
        final List<Vehicle> vehicles = backend.get(route);
        for (final Vehicle vehicle : vehicles) {
            if (vehicle.getBrigade().equals(tripID)) {
                return vehicle;
            }
        }
        return null;
    }
}
