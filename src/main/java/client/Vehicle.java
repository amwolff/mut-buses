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
    private final String lines;
    @SerializedName("Brigade")
    private final int brigade;

    public Vehicle(float lat, float lon, Date time, String lines, int brigade) {
        this.lat = lat;
        this.lon = lon;
        this.lines = lines;
        this.brigade = brigade;
        this.time = time;
    }

    public Vehicle(Vehicle v) {
        this(v.lat, v.lon, v.time, v.lines, v.brigade);
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public Date getTime() {
        return time;
    }

    public String getLines() {
        return lines;
    }

    public int getBrigade() {
        return brigade;
    }

}
