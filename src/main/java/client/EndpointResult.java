package client;

import java.util.List;

class EndpointResult {
    private List<Vehicle> result;

    EndpointResult(List<Vehicle> result) {
        this.result = result;
    }

    List<Vehicle> getResult() {
        return result;
    }
}
