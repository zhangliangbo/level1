package mcivicm.coordinate;

/**
 * national coordinate system
 * 国家坐标系统
 */
public enum NCS {
    WGS84("epsg:4326"),
    BEIJING54("epsg:4214"),
    XIAN80("epsg:4610"),
    CGCS2000("epsg:3889");

    private String code;

    NCS(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }}
