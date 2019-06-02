package storage;

import client.Vehicle;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImmutableStoreWithAzimuthTest {

    @Test
    void removeDuplicates() {
        final List<Vehicle> duplicates = new ArrayList<>();

        final Vehicle toDel0 = new Vehicle(0, 0, new Date("January 1, 1970"), "1", "1", 0);
        final Vehicle toDel1 = new Vehicle(0, 0, new Date("January 1, 1971"), "1", "1", 0);

        duplicates.add(toDel0);
        duplicates.add(toDel1);
        duplicates.add(new Vehicle(0, 0, new Date(), "1", "1", 0));
        duplicates.add(new Vehicle(0, 0, new Date("January 1, 1970"), "1", "2", 0));
        duplicates.add(new Vehicle(0, 0, new Date("January 1, 1970"), "1", "3", 0));

        final List<Vehicle> dedupeControlSample = new ArrayList<>(duplicates);
        dedupeControlSample.remove(toDel0);
        dedupeControlSample.remove(toDel1);

        assertEquals(dedupeControlSample, ImmutableStoreWithAzimuth.removeDuplicates(dedupeControlSample));

        assertEquals(dedupeControlSample, ImmutableStoreWithAzimuth.removeDuplicates(duplicates));

        assertEquals(dedupeControlSample,
                ImmutableStoreWithAzimuth.removeDuplicates(
                        ImmutableStoreWithAzimuth.removeDuplicates(
                                ImmutableStoreWithAzimuth.removeDuplicates(
                                        ImmutableStoreWithAzimuth.removeDuplicates(
                                                ImmutableStoreWithAzimuth.removeDuplicates(duplicates))))));

        final List<Vehicle> empty = new ArrayList<>();
        assertEquals(empty, ImmutableStoreWithAzimuth.removeDuplicates(empty));
    }

    @Test
    void calculateAzimuth() {
        final double a = ImmutableStoreWithAzimuth.calculateAzimuth(52.175243, 20.918766, 52.17513, 20.919823);
        assertEquals(99.88845000697633, a);
    }

}
