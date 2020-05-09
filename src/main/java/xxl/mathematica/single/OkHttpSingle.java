package xxl.mathematica.single;

import okhttp3.OkHttpClient;

public class OkHttpSingle {

    public static OkHttpClient instance() {
        return Holder.client;
    }

    private static class Holder {
        private static OkHttpClient client = new OkHttpClient.Builder().build();
    }
}
