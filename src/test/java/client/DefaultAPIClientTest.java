package client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAPIClientTest {

    private static String apiKeyForTests = "";

    @BeforeEach
    void setUp() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/secret.txt"))) {
            apiKeyForTests = br.readLine();
        }
        if (apiKeyForTests.isEmpty()) {
            fail("API secret not provided!");
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getVehicles() throws IOException {
        DefaultAPIClient client = new DefaultAPIClient(apiKeyForTests);
        List<Vehicle> vehicles = client.getVehicles(1, "523");

        vehicles.forEach(vehicle -> {
            assertNotEquals(0, vehicle.getLat());
            assertNotEquals(0, vehicle.getLon());
            assertNotNull(vehicle.getTime());
            assertFalse(vehicle.getLines().trim().isEmpty());
            assertNotEquals(0, vehicle.getBrigade());
        });
    }
}
