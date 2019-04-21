package client;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Vehicle {
    @SerializedName("Lat")
    private final float lat;
    @SerializedName("Lon")
    private final float lon;
    @SerializedName("Time")
    private final Date time;
    @SerializedName("Lines")
    private final int lines;
    @SerializedName("Brigade")
    private final int brigade;

    public Vehicle(float lat, float lon, Date time, int lines, int brigade) {
        this.lat = lat;
        this.lon = lon;
        this.lines = lines;
        this.brigade = brigade;
        this.time = time;
    }

    public Vehicle(Vehicle v) {
        this(v.lat, v.lon, v.time, v.lines, v.brigade);
    }

    public final float getLat() {
        return lat;
    }

    public final float getLon() {
        return lon;
    }

    public final Date getTime() {
        return time;
    }

    public final int getLines() {
        return lines;
    }

    public final int getBrigade() {
        return brigade;
    }
}
