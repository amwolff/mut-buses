package storage;

import client.Vehicle;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryVehicleStore implements VehicleStore {
    private final Map<InMemoryVehicleStoreKey, Vehicle> inMemDb = new ConcurrentHashMap<>();

    @Override
    public void insert(Vehicle vehicle) {
        inMemDb.put(new InMemoryVehicleStoreKey(vehicle.getLines(), vehicle.getBrigade()), vehicle);
    }

    @Override
    public void insert(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            insert(vehicle);
        }
    }

    @Override
    public List<Vehicle> retrieveAll() {
        return new LinkedList<>(inMemDb.values());
    }

    @Override
    public List<Vehicle> retrieve(String line) {
        List<Vehicle> ret = new LinkedList<>();
        inMemDb.forEach((inMemoryVehicleStoreKey, vehicle) -> {
            if (inMemoryVehicleStoreKey.getLine().equals(line)) {
                ret.add(vehicle);
            }
        });
        return ret;
    }

    @Override
    public Vehicle retrieve(String line, Integer brigade) {
        return inMemDb.get(new InMemoryVehicleStoreKey(line, brigade));
    }
}
