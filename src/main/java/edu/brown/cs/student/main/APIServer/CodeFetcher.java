package edu.brown.cs.student.main.APIServer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for fetching state and county codes from the Census API.
 */
public class CodeFetcher {

  private static final String BASE_URL = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
  private static final String COUNTY_URL = "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:";
  private static final HttpClient httpClient = HttpClient.newHttpClient();
  private static Map<String, String> stateCodeCache = new HashMap<>();

  /**
   * Retrieves the state code for the given state name.
   *
   * @param stateName The name of the state.
   * @return The state code corresponding to the provided state name.
   * @throws IOException        If an I/O error occurs during the HTTP request.
   * @throws InterruptedException If the HTTP request is interrupted.
   * @throws URISyntaxException If the provided URI syntax is invalid.
   */
  public static String getStateCode(String stateName) throws IOException, InterruptedException, URISyntaxException {
    // Check if the state code is already cached
    if (stateCodeCache.containsKey(stateName)) {
      return stateCodeCache.get(stateName);
    }

    // Build the API URL for fetching state data
    String apiUrl = BASE_URL;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(apiUrl))
        .build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // Parse the response to extract state codes
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

  /**
   * Retrieves the county code for the given state code and county name.
   *
   * @param stateCode  The code of the state.
   * @param countyName The name of the county.
   * @return The county code corresponding to the provided state code and county name.
   * @throws IOException        If an I/O error occurs during the HTTP request.
   * @throws InterruptedException If the HTTP request is interrupted.
   * @throws URISyntaxException If the provided URI syntax is invalid.
   */
  public static String getCountyCode(String stateCode, String countyName) throws IOException, InterruptedException, URISyntaxException {
    // Build the API URL for fetching county data
    String apiUrl = COUNTY_URL + stateCode;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(apiUrl))
        .build();

    // Send the HTTP request and receive the response
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

  /**
   * Parses a JSON response string into a 2D array of strings.
   *
   * @param response The JSON response string to parse.
   * @return A 2D array of strings representing the parsed response data.
   */
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
