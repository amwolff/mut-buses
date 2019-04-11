package apiumclient;

public class Vehicle {

    private final float lan;
    private final float lon;
    // TODO(zxcv22z): investigate data type
    // private <Date> Time
    private final int lines;
    private final int brigade;

    public Vehicle(float lan, float lon, int lines, int brigade) {
        this.lan = lan;
        this.lon = lon;
        this.lines = lines;
        this.brigade = brigade;
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

}
