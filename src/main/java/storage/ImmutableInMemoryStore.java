package storage;

import client.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ImmutableInMemoryStore implements VehicleStore {
    private final Map<String, CopyOnWriteArrayList<Vehicle>> inMemDb = new ConcurrentHashMap<>();

    @Override
    public void insert(Vehicle vehicle) { // shouldn't be used really
        final CopyOnWriteArrayList<Vehicle> br = new CopyOnWriteArrayList<>();
        br.add(vehicle);
        inMemDb.put(vehicle.getLines(), br);
    }

    @Override
    public void insert(List<Vehicle> vehicles) {
        inMemDb.put(vehicles.get(0).getLines(), new CopyOnWriteArrayList<>(vehicles));
    }

    @Override
    public List<Vehicle> retrieveAll() {
        final List<Vehicle> ret = new ArrayList<>();
        inMemDb.values().forEach(ret::addAll);
        return ret;
    }

    @Override
    public List<Vehicle> retrieve(String line) {
        return inMemDb.get(line);
    }

    @Override
    public Vehicle retrieve(String line, Integer brigade) {
        final List<Vehicle> vehicles = inMemDb.get(line);
        for (final Vehicle vehicle : vehicles) {
            if (vehicle.getBrigade() == brigade) {
                return vehicle;
            }
        }
        return null;
    }
}
