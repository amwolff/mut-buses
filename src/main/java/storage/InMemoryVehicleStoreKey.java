package storage;

final class InMemoryVehicleStoreKey {
    private final String line;
    private final Integer brigade;

    InMemoryVehicleStoreKey(String line, Integer brigade) {
        this.line = line;
        this.brigade = brigade;
    }

    String getLine() {
        return line;
    }

    Integer getBrigade() {
        return brigade;
    }
}
