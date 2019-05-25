package client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class EndpointResult {
    @Expose(serialize = false)
    @SerializedName("result")
    private List<Vehicle> result;

    EndpointResult(List<Vehicle> result) {
        this.result = result;
    }

    List<Vehicle> getResult() {
        return result;
    }
}
