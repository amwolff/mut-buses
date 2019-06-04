package storage;

import client.Vehicle;

import java.util.List;

/**
 * VehicleStore defines how fetched vehicles data can be stored and retrieved from the storage.
 */
public interface VehicleStore {
    void insert(Vehicle vehicle);

    void insert(List<Vehicle> vehicles);

    void clear(String route);

    List<Vehicle> retrieveAll();

    List<Vehicle> retrieve(String route);

    Vehicle retrieve(String route, String tripID);
}
