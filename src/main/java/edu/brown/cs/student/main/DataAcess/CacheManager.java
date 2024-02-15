package edu.brown.cs.student.main.DataAcess;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.student.main.DataModels.ACSQueryKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The CacheManager class manages caching for various types of data. It leverages Google's Guava library
 * to create a configurable and efficient cache.
 *
 * @param <K> the type of keys used for caching.
 * @param <V> the type of values to be cached.
 */

public class CacheManager<K, V> {

  private LoadingCache<K, V> cache;


  /**
   * Creates a CacheManager with specified configuration and eviction strategy.
   *
   * @param config the configuration settings for the cache.
   * @param evictionStrategy the strategy for cache entry eviction.
   */
  public CacheManager(CacheConfiguration config, EvictionStrategy<K, V> evictionStrategy) {
    CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();

    // Config settings
    builder.maximumSize(config.getMaxSize())
        .expireAfterWrite(config.getExpireAfterWriteDuration(), config.getTimeUnit());

    // Eviction strategy
    evictionStrategy.configure((CacheBuilder<Object, Object>) builder);

    cache = builder.build(new CacheLoader<K, V>() {
      @Override
      public V load(K key) throws Exception {
        return fetchDataFromAPI(key);
      }
    });
  }

  /**
   * Fetches data from an API using the provided key.
   *
   * @param key the key used to fetch data.
   * @return the fetched data.
   * @throws IOException if an error occurs during data fetching.
   */
  private V fetchDataFromAPI(K key) throws IOException {
    try {
      String apiUrl = buildApiUrlForKey((ACSQueryKey) key);
      String apiResponse = makeApiRequest(apiUrl);
      return parseApiResponse(apiResponse);
    } catch (IOException e) {
      throw e;
    }
  }

  /**
   * Builds the API URL for a given ACSQueryKey.
   * This method constructs the URL for the American Community Survey (ACS) API based on the provided key.
   * It includes the necessary parameters such as year, dataset identifier, variables, county code, state code,
   * and an API key.
   *
   * @param key The ACSQueryKey object containing parameters for the ACS API query.
   * @return A string representing the fully constructed API URL.
   */
  private String buildApiUrlForKey(ACSQueryKey key) {
    String baseUrl = "https://api.census.gov/data/";

    // Specifying the year and dataset identifier
    String year = "2023"; // Example year, adjust as needed
    String dataset = "acs/acs5"; // Example dataset, adjust as needed

    //https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:*&in=state:06

    // Constructing the base URL
    String url = baseUrl + year + "/" + dataset;

    // Constructing the query parameters
    url += "/subject/?get=VARIABLES";

    url += "?get=NAME";

    // Adding geographic filters
    // The 'for' and 'in' parameters should use the county and state codes
    url += "&for=county:" + key.getCountyCode() + "&in=state:" + key.getStateCode();

    // Adding API Key
    String apiKey = "aa979b28dba65963c8a78da9cd8bec38a6b3d6a0";
    url += "&key=" + apiKey;

    return url;
  }



  /**
   * Makes an HTTP GET request to the specified API URL and returns the response.
   * This method opens a connection to the provided URL, sends a GET request, and collects
   * the response. If the response code indicates success (HTTP 200), the method returns
   * the response as a string. If the response code indicates failure, it throws an IOException.
   *
   * @param apiUrl The URL of the API to which the GET request is sent.
   * @return A string containing the response from the API.
   * @throws IOException If the HTTP request fails or if the response code is not HTTP_OK (200).
   */
  private String makeApiRequest(String apiUrl) throws IOException {
    URL url = new URL(apiUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    int responseCode = connection.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new IOException("HTTP GET request failed with response code " + responseCode);
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
      return reader.lines().collect(Collectors.joining());
    } finally {
      connection.disconnect();
    }
  }

  /**
   * Parses the JSON response from the API into a desired data type.
   * This method uses Gson to convert the JSON string into a list of objects of type V,
   * as specified by the TypeToken. It's designed to handle responses from the ACS API,
   * parsing them into a list of ACSQueryKey objects. Ensure the TypeToken accurately
   * reflects the expected structure of the JSON response.
   *
   * @param json The JSON string response from the API.
   * @return A list of objects of type V, parsed from the JSON string.
   */
  private V parseApiResponse(String json) {
    Gson gson = new Gson();
    Type type = new TypeToken<List<ACSQueryKey>>(){}.getType();
    return gson.fromJson(json, type);
  }

}
