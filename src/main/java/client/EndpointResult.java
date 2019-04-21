package client;

import java.util.List;

class EndpointResult {
    private final List<Vehicle> result;

    EndpointResult(List<Vehicle> result) {
        this.result = result;
    }

    final List<Vehicle> getResult() {
        return result;
    }
}
