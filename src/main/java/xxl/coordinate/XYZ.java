package xxl.coordinate;

/**
 * 平面坐标
 */
public class XYZ {
    /**
     * 默认CGCS2000坐标
     */
    private NCS ncs;
    /**
     * 默认3三度带投影，更小更准确
     */
    private CodeType codeType;
    /**
     * 带号，不能经度对应不同带号
     */
    private int codeNum = 0;

    private double x;
    private double y;
    private double z;

    public XYZ(NCS ncs, CodeType codeType, int codeNum, double x, double y, double z) {
        this.ncs = ncs;
        this.codeType = codeType;
        this.codeNum = codeNum;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public XYZ(NCS ncs, CodeType codeType, int codeNum, double x, double y) {
        this(ncs, codeType, codeNum, x, y, 0D);
    }

    public XYZ(NCS ncs, CodeType codeType) {
        this(ncs, codeType, 0, 0D, 0D, 0D);
    }

    public XYZ(CodeType codeType) {
        this(NCS.WGS84, codeType);
    }


    public NCS getNcs() {
        return ncs;
    }

    public void setNcs(NCS ncs) {
        this.ncs = ncs;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    public int getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(int codeNum) {
        this.codeNum = codeNum;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "XYZ{" +
                "ncs=" + ncs +
                ", codeType=" + codeType +
                ", codeNum=" + codeNum +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
