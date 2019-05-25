package storage;

import client.Vehicle;

import java.util.List;

public interface VehicleStore {
    void insert(Vehicle vehicle);

    void insert(List<Vehicle> vehicles);

    void clear(String line);

    List<Vehicle> retrieveAll();

    List<Vehicle> retrieve(String line);

    Vehicle retrieve(String line, Integer brigade);
}
