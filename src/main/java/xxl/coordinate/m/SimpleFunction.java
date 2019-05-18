package xxl.coordinate.m;

import xxl.coordinate.IXYZ;

import java.util.function.Function;

public class SimpleFunction implements Function<SimpleXYZ, IXYZ> {

    @Override
    public IXYZ apply(SimpleXYZ simpleXYZ) {
        return simpleXYZ;
    }

}
