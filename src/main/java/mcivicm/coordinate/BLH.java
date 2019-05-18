package mcivicm.coordinate;

/**
 * 经纬度和高程
 */
public class BLH {
    /**
     * 默认WGS84坐标
     */
    private NCS ncs;
    private double longitude;
    private double latitude;
    private double altitude;

    public BLH(NCS ncs, double longitude, double latitude, double altitude) {
        this.ncs = ncs;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public BLH(NCS ncs, double longitude, double latitude) {
        this(ncs, longitude, latitude, 0D);
    }

    public BLH(double longitude, double latitude, double altitude) {
        this(NCS.WGS84, longitude, latitude, altitude);
    }

    public BLH(double longitude, double latitude) {
        this(longitude, latitude, 0D);
    }

    public BLH(NCS ncs) {
        this(ncs, 0D, 0D, 0D);
    }

    public NCS getNcs() {
        return ncs;
    }

    public void setNcs(NCS ncs) {
        this.ncs = ncs;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "BLH{" +
                "ncs=" + ncs +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                '}';
    }
}
