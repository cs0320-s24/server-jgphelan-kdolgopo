package edu.brown.cs.student.main.APIServer;
import static edu.brown.cs.student.main.APIServer.CodeFetcher.getCountyCode;
import static edu.brown.cs.student.main.APIServer.CodeFetcher.getStateCode;

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
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private static final String CENSUS_API_URL = "https://api.census.gov/data/2021/acs/acs1/subject/variables";

  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();

    try {
      String stateName = request.queryParams("state");
      String countyName = request.queryParams("county");

      // Ensure state and county names are not null
      if (stateName == null || countyName == null) {
        response.status(400); // Bad Request status code
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", "State and county names must be provided");
        return serialize(responseMap);
      }

      // Get state code
      String stateCode = CodeFetcher.getStateCode(stateName);
      if (stateCode == null) {
        response.status(400); // Bad Request status code
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", "Invalid state name");
        return serialize(responseMap);
      }

      // Get county code
      String countyCode = CodeFetcher.getCountyCode(stateCode, countyName);
      if (countyCode == null) {
        response.status(400); // Bad Request status code
        responseMap.put("result", "error_bad_request");
        responseMap.put("message", "Invalid county name");
        return serialize(responseMap);
      }


      String query = "county:" + countyCode + "&in=state:" + stateCode;
      Map<String, Object> broadbandData = fetchBroadbandData(query);

      response.status(200); // OK status code
      responseMap.put("result", "success");
      responseMap.put("datetime", LocalDateTime.now().toString());
      responseMap.put("state", stateName);
      responseMap.put("county", countyName);
      responseMap.put("broadband_data", broadbandData);

      return serialize(responseMap);
    } catch (Exception e) {
      e.printStackTrace();
      response.status(500); // Internal Server Error status code
      responseMap.put("result", "error_exception");
      responseMap.put("message", "An exception occurred");
      return serialize(responseMap);
    }
  }


  private Map<String, Object> fetchBroadbandData(String query)
      throws URISyntaxException, IOException, InterruptedException {
    // Build request to the Census API for broadband data
    URI uri = new URI(CENSUS_API_URL + "?get=NAME,S2802_C03_022E&for=" + query);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(uri)
        .GET()
        .build();
    HttpResponse<String> httpResponse = HttpClient.newHttpClient()
        .send(httpRequest, HttpResponse.BodyHandlers.ofString());

    // Check if response is successful
    if (httpResponse.statusCode() == 200) {
      // Parse JSON response
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<List<List<Object>>> adapter = moshi.adapter(
          Types.newParameterizedType(List.class, List.class, Object.class));
      List<List<Object>> responseData = adapter.fromJson(httpResponse.body());

      // Extract data from response
      if (responseData != null && responseData.size() > 1) {
        List<Object> dataRow = responseData.get(1); // Skip header row
        Map<String, Object> broadbandData = new HashMap<>();
        broadbandData.put("county", dataRow.get(0));
        broadbandData.put("percentage", dataRow.get(1));
        return broadbandData;
      } else {
        throw new RuntimeException("Invalid response format or empty data");
      }
    } else {
      throw new RuntimeException("Failed to fetch broadband data from Census API");
    }
  }


  private String serialize(Map<String, Object> responseMap) {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<Map<String, Object>> adapter = moshi.adapter(
          Types.newParameterizedType(Map.class, String.class, Object.class));
      return adapter.toJson(responseMap);
    } catch (Exception e) {
      e.printStackTrace();
      return "{\"error\": \"Failed to serialize responseMap; the response does not match the expected format\"}";
    }
  }

}