package mcivicm.coordinate;

import org.osgeo.proj4j.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class CT {
    private static final CoordinateTransformFactory ctf = new CoordinateTransformFactory();
    private static final CRSFactory crsf = new CRSFactory();
    private static final String[] wgs84 = new String[]{
            "datum=WGS84",
            "k=1",
            "lat_0=0",
            //lon_0=*
            "no_defs",
            "proj=tmerc",
            "units=m",
            //x_0=*
            "y_0=0"
    };
    private static final String[] beijing54 = new String[]{
            "ellps=krass",
            "k=1",
            "lat_0=0",
            //lon_0=*
            "no_defs",
            "proj=tmerc",
            "towgs84=15.8,-154.4,-82.3,0,0,0,0",
            "units=m",
            //x_0=*
            "y_0=0"
    };
    private static final String[] xian80 = new String[]{
            "a=6378140",
            "b=6356755.288157528",
            "k=1",
            "lat_0=0",
            //lon_0=*
            "no_defs",
            "proj=tmerc",
            "units=m",
            //x_0=*
            "y_0=0"
    };
    private static final String[] cgcs2000 = new String[]{
            "ellps=GRS80",
            "k=1",
            "lat_0=0",
            //lon_0=*
            "no_defs",
            "proj=tmerc",
            "units=m",
            //x_0=*
            "y_0=0"
    };

    private static List<String> get(NCS ncs) {
        List<String> params = new ArrayList<>();
        switch (ncs) {
            case WGS84:
                params.addAll(Arrays.asList(wgs84));
                break;
            case BEIJING54:
                params.addAll(Arrays.asList(beijing54));
                break;
            case XIAN80:
                params.addAll(Arrays.asList(xian80));
                break;
            case CGCS2000:
                params.addAll(Arrays.asList(cgcs2000));
                break;
        }
        return params;
    }

    private static int[] stepCorrection(CodeType ct) {
        int step = ct == CodeType.THREE ? 3 : 6;
        int addition = ct == CodeType.THREE ? 0 : -3;
        return new int[]{step, addition};
    }

    /**
     * BLH->XYZ
     *
     * @param blh
     * @param xyz
     * @return
     */
    public static XYZ blh2xyz(BLH blh, XYZ xyz) {
        //参数列表
        List<String> params = get(xyz.getNcs());
        int[] step = stepCorrection(xyz.getCodeType());
        for (int i = 1; i * step[0] <= 180; i++) {
            int center = step[0] * i + step[1];
            if (Math.abs(center - blh.getLongitude()) < step[0] / 2f) {
                xyz.setCodeNum(i);
                params.add("lon_0=" + center);
                params.add("x_0=" + i + "500000");
                break;
            }
        }
        CoordinateReferenceSystem src = crsf.createFromName(blh.getNcs().getCode());
        CoordinateReferenceSystem dst = crsf.createFromParameters("ncs", params.toArray(new String[0]));
        CoordinateTransform ct = ctf.createTransform(src, dst);
        ProjCoordinate t = ct.transform(new ProjCoordinate(blh.getLongitude(), blh.getLatitude(), blh.getAltitude()), new ProjCoordinate());
        xyz.setX(t.x);
        xyz.setY(t.y);
        xyz.setZ(t.z);
        return xyz;
    }

    /**
     * XYZ->BLH
     *
     * @param xyz
     * @param blh
     * @return
     */
    public static BLH xyz2blh(XYZ xyz, BLH blh) {
        List<String> params = get(xyz.getNcs());
        int[] step = stepCorrection(xyz.getCodeType());
        params.add("lon_0=" + (step[0] * xyz.getCodeNum() + step[1]));
        params.add("x_0=" + xyz.getCodeNum() + "500000");//加个带号更好
        CoordinateReferenceSystem src = crsf.createFromParameters("ncs", params.toArray(new String[0]));
        CoordinateReferenceSystem dst = crsf.createFromName(blh.getNcs().getCode());
        CoordinateTransform ct = ctf.createTransform(src, dst);
        ProjCoordinate t = ct.transform(new ProjCoordinate(xyz.getX(), xyz.getY(), xyz.getZ()), new ProjCoordinate());
        blh.setLongitude(t.x);
        blh.setLatitude(t.y);
        blh.setAltitude(t.z);
        return blh;
    }

    /**
     * 删除距离不合格的坐标点
     *
     * @param src
     * @param f
     * @param min
     * @param max
     * @param <T>
     * @return
     */
    public static <T> List<T> deleteDuplicate(List<T> src, Function<T, IXYZ> f, double min, double max) {
        Objects.requireNonNull(src);
        Objects.requireNonNull(f);
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("radius can not be negative.");
        }
        if (max < min) {
            throw new IllegalArgumentException("max can not be less than min.");
        }
        if (src.size() < 2) {
            return src;
        }
        List<T> list = new ArrayList<>();
        src.stream().reduce(new BinaryOperator<T>() {
            @Override
            public T apply(T t, T t2) {
                IXYZ o = f.apply(t);
                IXYZ n = f.apply(t2);
                double dist =
                        Math.sqrt(
                                Math.pow(n.getX() - o.getX(), 2D) +
                                        Math.pow(n.getY() - o.getY(), 2D) +
                                        Math.pow(n.getZ() - o.getZ(), 2D)
                        );
                if (dist < min || dist > max) {
                    return t;
                } else {
                    list.add(t2);
                    return t2;
                }
            }
        });
        return list;
    }
}
