package edu.brown.cs.student.main.Utilities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {

  public static Map<String, Object> parseStringToMap(String jsonString) throws IOException {
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(
        Types.newParameterizedType(Map.class, String.class, Object.class));
    return adapter.fromJson(jsonString);
  }
}
