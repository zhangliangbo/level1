package xxl.mathematica.single;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

public class GsonSingle {

    public static Gson instance() {
        return Holder.gson;
    }

    public static Gson oneLevelInstance() {
        return OneLevelHolder.gson;
    }

    static class Holder {
        static Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue()) {
                    return new JsonPrimitive(src.longValue());
                }
                return new JsonPrimitive(src);
            }
        }).create();
    }

    static class OneLevelHolder {
        static ExclusionStrategy serialEs = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return !f.getDeclaredClass().isPrimitive() &&
                        !f.getDeclaredClass().equals(String.class) &&
                        !f.getDeclaredClass().isAssignableFrom(Date.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
        static ExclusionStrategy deSerialEs = new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return !f.getDeclaredClass().isPrimitive() &&
                        !f.getDeclaredClass().equals(String.class) &&
                        !f.getDeclaredClass().isAssignableFrom(Date.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
        static Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(serialEs)
                .addDeserializationExclusionStrategy(deSerialEs)
                .create();
    }

}
