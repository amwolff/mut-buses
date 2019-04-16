package apiumclient;

import java.util.Date;

public class Vehicle {

    private final float lan;
    private final float lon;
    private final int lines;
    private final int brigade;
    //TODO(zxcv22z): investigate data type
    private final Date date;

    public Vehicle(float lan, float lon, Date date, int lines, int brigade) {
        this.lan = lan;
        this.lon = lon;
        this.lines = lines;
        this.brigade = brigade;
        this.date = date;
    }

    public float getLan() {
        return lan;
    }

    public float getLon() {
        return lon;
    }

    public int getLines() {
        return lines;
    }

    public int getBrigade() {
        return brigade;
    }

    public Date getDate() {
        return date;
    }

}
