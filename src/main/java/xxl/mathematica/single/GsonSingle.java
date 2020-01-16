package xxl.mathematica.single;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class GsonSingle {

    public static Gson instance() {
        return Holder.gson;
    }

    public static Gson oneLevelInstance() {
        return OneLevelHolder.gson;
    }

    static class Holder {
        static Gson gson = new GsonBuilder().create();
    }

    static class OneLevelHolder {
        static ExclusionStrategy es = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return !f.getDeclaredClass().isPrimitive() &&
                        !f.getDeclaredClass().equals(String.class) &&
                        !f.getDeclaredClass().isAssignableFrom(Date.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return !clazz.isPrimitive();
            }
        };
        static Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(es)
                .addDeserializationExclusionStrategy(es)
                .create();
    }

}
