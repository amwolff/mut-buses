package storage;

import client.Vehicle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryVehicleStore implements VehicleStore { // TODO: this implementation needs a vacuum
    private final Map<String, Vehicle> inMemDb = new ConcurrentHashMap<>();

    @Override
    public void insert(Vehicle vehicle) {
        inMemDb.put(vehicle.getLines() + vehicle.getBrigade(), vehicle);
    }

    @Override
    public void insert(List<Vehicle> vehicles) {
        vehicles.forEach(this::insert);
    }

    @Override
    public void clear(String line) {
        // Implement me!
    }

    @Override
    public List<Vehicle> retrieveAll() {
        return new ArrayList<>(inMemDb.values());
    }

    @Override
    public List<Vehicle> retrieve(String line) {
        final List<Vehicle> ret = new LinkedList<>();
        inMemDb.forEach((hash, vehicle) -> {
            if (hash.startsWith(line)) {
                ret.add(vehicle);
            }
        });
        return ret;
    }

    @Override
    public Vehicle retrieve(String line, Integer brigade) {
        return inMemDb.get(line + brigade);
    }
}
