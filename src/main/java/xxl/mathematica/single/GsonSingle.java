package xxl.mathematica.single;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSingle {

  public static Gson instance() {
    return Holder.gson;
  }

  private static class Holder {
    private static Gson gson = new GsonBuilder().create();
  }

}
