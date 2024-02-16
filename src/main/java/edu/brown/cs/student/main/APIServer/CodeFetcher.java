package edu.brown.cs.student.main.APIServer;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

public class CodeFetcher {

  private static final String BASE_URL = "https://api.census.gov/data/2010/dec/sf1";
  private static final HttpClient httpClient = HttpClient.newHttpClient();
  private static Map<String, String> stateCodeCache = new HashMap<>();

  public static String getStateCode(String stateName) throws IOException, InterruptedException, URISyntaxException {
    if (stateCodeCache.containsKey(stateName)) {
      return stateCodeCache.get(stateName);
    }
    String apiKey = "aa979b28dba65963c8a78da9cd8bec38a6b3d6a0";
    String apiUrl = BASE_URL + "/states?get=NAME&key=" + apiKey;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(apiUrl))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    String[][] data = parseResponse(response.body());
    for (String[] row : data) {
      if (row[0].equalsIgnoreCase(stateName)) {
        String stateCode = row[1];
        stateCodeCache.put(stateName, stateCode);
        return stateCode;
      }
    }

    // If state code not found:
    return null;
  }

  public static String getCountyCode(String stateCode, String countyName) throws IOException, InterruptedException, URISyntaxException {
    String apiKey = "aa979b28dba65963c8a78da9cd8bec38a6b3d6a0";
    String apiUrl = BASE_URL + "/county?get=NAME&for=county:*&in=state:" + stateCode + "&key=" + apiKey;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(apiUrl))
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // Parse the response JSON to get the county code
    String[][] data = parseResponse(response.body());
    for (String[] row : data) {
      if (row[0].equalsIgnoreCase(countyName)) {
        return row[3]; // county code is in the fourth column
      }
    }

    // If county code not found, return null
    return null;
  }

  private static String[][] parseResponse(String response) {
    try {
      // Assuming response is a JSON array of arrays
      return new Gson().fromJson(response, String[][].class);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }


}
