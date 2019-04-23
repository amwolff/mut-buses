package database;

import client.Vehicle;

import java.util.List;

public interface VehicleStore {
    void insert(Vehicle vehicle);

    List<Vehicle> retrieve(Integer type);

    List<Vehicle> retrieve(Integer type, String line);

    Vehicle retrieve(Integer type, String line, Integer brigade);
}
