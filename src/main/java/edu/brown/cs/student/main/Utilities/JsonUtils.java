package edu.brown.cs.student.main.Utilities;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.util.Map;

/**
 * Utility class for parsing JSON strings into a Map.
 */
public class JsonUtils {

  /**
   * Parses a JSON string into a Map<String, Object>.
   *
   * @param jsonString The JSON string to parse.
   * @return A Map representing the JSON data, where keys are strings and values are objects.
   * @throws IOException If an I/O error occurs while reading the JSON string.
   */
  public static Map<String, Object> parseStringToMap(String jsonString) throws IOException {
    // Initialize Moshi JSON adapter
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(
        Types.newParameterizedType(Map.class, String.class, Object.class));

    // Parse JSON string into Map
    return adapter.fromJson(jsonString);
  }
}
