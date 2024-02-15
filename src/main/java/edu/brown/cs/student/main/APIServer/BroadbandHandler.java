package edu.brown.cs.student.main.APIServer;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private static final String CENSUS_API_URL = "https://api.census.gov/data/2010/sf1";

  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();

    try {
      String stateName = request.queryParams("state");
      String countyName = request.queryParams("county");

      // Convert state and county names to numeric codes
      String stateCode = getStateCode(stateName);
      String countyCode = getCountyCode(stateCode, countyName);

      if (stateCode == null || countyCode == null) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", "Invalid state or county name");
        return serialize(responseMap);
      }

      String query = "county:" + countyCode + "&state:" + stateCode;

      String broadbandData = fetchBroadbandData(query);

      responseMap.put("result", "success");
      responseMap.put("datetime", LocalDateTime.now().toString());
      responseMap.put("state", stateName);
      responseMap.put("county", countyName);
      responseMap.put("broadband_data", broadbandData);

      return serialize(responseMap);
    } catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "error_exception");
      responseMap.put("message", "An exception occurred");
      return serialize(responseMap);
    }
  }

  private String getStateCode(String stateName) {
    return "06"; // or code
  }

  private String getCountyCode(String stateCode, String countyName) {

    return "001"; // or code
  }

  private String fetchBroadbandData(String query) throws URISyntaxException, IOException, InterruptedException {
    // Build request to the Census API for broadband data
    URI uri = new URI(CENSUS_API_URL + "?get=S2802_C03_022E&for=" + query);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(uri)
        .GET()
        .build();
    HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(httpRequest, HttpResponse.BodyHandlers.ofString());

    // Check if response is successful
    if (httpResponse.statusCode() == 200) {
      return httpResponse.body();
    } else {
      throw new RuntimeException("Failed to fetch broadband data from Census API");
    }
  }

  private String serialize(Map<String, Object> responseMap) {
    try {
      Moshi moshi = new Moshi.Builder().build();
      // Specify the generic types explicitly to match the required type
      JsonAdapter<Map<String, Object>> adapter = moshi.adapter(
          Types.newParameterizedType(Map.class, String.class, Object.class));
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

}
