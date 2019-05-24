package api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class InternalRoute {
    @Expose
    @SerializedName("route")
    private final String route;

    InternalRoute(String route) {
        this.route = route;
    }

    public String getRoute() {
        return route;
    }
}
