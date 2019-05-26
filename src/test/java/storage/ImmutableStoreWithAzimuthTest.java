package storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImmutableStoreWithAzimuthTest {
    @Test
    void calculateAzimuth() {
        final double a = ImmutableStoreWithAzimuth.calculateAzimuth(52.175243, 20.918766, 52.17513, 20.919823);
        assertEquals(99.88845000697633, a);
    }
}
