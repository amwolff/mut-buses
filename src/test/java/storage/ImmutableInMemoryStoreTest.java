package storage;

import client.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ImmutableInMemoryStoreTest {

    private List<Vehicle> firstLineVehicles;
    private List<Vehicle> secLineVehicles;
    private ImmutableInMemoryStore immutableInMemoryStore = new ImmutableInMemoryStore();

    @Test
    public void insertAndRetrieveMethodTest() {
        List<Vehicle> fromDb = immutableInMemoryStore.retrieve("220");

        assertFalse(firstLineVehicles.isEmpty());
        assertFalse(fromDb.isEmpty());

        assertEquals(fromDb.get(0).getBrigade(), firstLineVehicles.get(0).getBrigade());
        assertEquals(fromDb.get(0).getLines(), firstLineVehicles.get(0).getLines());
        assertEquals(fromDb.get(0).getTime(), firstLineVehicles.get(0).getTime());
        assertEquals(fromDb.get(0).getLat(), firstLineVehicles.get(0).getLat());
        assertEquals(fromDb.get(0).getLon(), firstLineVehicles.get(0).getLon());

        assertEquals(fromDb.get(1).getBrigade(), firstLineVehicles.get(1).getBrigade());
        assertEquals(fromDb.get(1).getLines(), firstLineVehicles.get(1).getLines());
        assertEquals(fromDb.get(1).getTime(), firstLineVehicles.get(1).getTime());
        assertEquals(fromDb.get(1).getLat(), firstLineVehicles.get(1).getLat());
        assertEquals(fromDb.get(1).getLon(), firstLineVehicles.get(1).getLon());
    }

    @Test
    public void updateInsertAndRetrieveMethodTest() {
        immutableInMemoryStore.insert(firstLineVehicles);
        immutableInMemoryStore.insert(firstLineVehicles);
        immutableInMemoryStore.insert(firstLineVehicles);
        List<Vehicle> fromDb = immutableInMemoryStore.retrieve("220");
        List<Vehicle> fromDbSec = immutableInMemoryStore.retrieve("122");
        assertEquals(2, fromDb.size());
        assertEquals(1, fromDbSec.size());
    }

    @Test
    public void retrieveAllMethodTest() {
        List<Vehicle> listOfVehicles = immutableInMemoryStore.retrieveAll();
        assertEquals(3, listOfVehicles.size());
        insertAndRetrieveMethodTest();
    }

    @Test
    public void retrieveByLineAndBrigade() {
        Vehicle vehicle = immutableInMemoryStore.retrieve("220", 1);
        assert vehicle != null;
        assertEquals(vehicle.getBrigade(), 1);
    }

    @BeforeEach
    private void initVehicles() {
        Date date = new Time(4);
        Vehicle firstVeh = new Vehicle(0f, 0f, date, "220", 1, 0.1f);
        Vehicle secondVeh = new Vehicle(1f, 1f, date, "220", 2, 0.1f);
        Vehicle thirdVeh = new Vehicle(2f, 2f, date, "122", 1, 0.1f);
        firstLineVehicles = new CopyOnWriteArrayList<>();
        secLineVehicles = new CopyOnWriteArrayList<>();
        firstLineVehicles.add(firstVeh);
        firstLineVehicles.add(secondVeh);
        secLineVehicles.add(thirdVeh);
        immutableInMemoryStore.insert(firstLineVehicles);
        immutableInMemoryStore.insert(secLineVehicles);
    }
}
