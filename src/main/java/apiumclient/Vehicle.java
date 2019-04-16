package apiumclient;

import java.util.Date;

public class Vehicle {
    private final float lat;
    private final float lon;
    private final Date time;
    private final int lines;
    private final int brigade;

    public Vehicle(float lat, float lon, Date time, int lines, int brigade) {
        this.lat = lat;
        this.lon = lon;
        this.lines = lines;
        this.brigade = brigade;
        this.time = time;
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

    public int getLines() {
        return lines;
    }

    public int getBrigade() {
        return brigade;
    }
}
