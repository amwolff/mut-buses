package client;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Vehicle {
    @Expose
    @SerializedName(value = "latitude", alternate = "Lat")
    private final float lat;
    @Expose
    @SerializedName(value = "longitude", alternate = "Lon")
    private final float lon;
    @Expose
    @SerializedName(value = "time", alternate = "Time")
    private final Date time;
    @Expose
    @SerializedName(value = "route", alternate = "Lines")
    private final String lines;
    @Expose
    @SerializedName(value = "trip_id", alternate = "Brigade")
    private final String brigade;
    @Expose(deserialize = false)
    @SerializedName("azimuth")
    private final double azimuth;

    public Vehicle(float lat, float lon, Date time, String lines, String brigade, double azimuth) {
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.lines = lines;
        this.brigade = brigade;
        this.azimuth = azimuth;
    }

    public Vehicle(Vehicle v) {
        this(v.lat, v.lon, v.time, v.lines, v.brigade, v.azimuth);
    }

    public Vehicle(Vehicle v, double azimuth) {
        this(v.lat, v.lon, v.time, v.lines, v.brigade, azimuth);
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

    public String getBrigade() {
        return brigade;
    }

    public double getAzimuth() {
        return azimuth;
    }
}
