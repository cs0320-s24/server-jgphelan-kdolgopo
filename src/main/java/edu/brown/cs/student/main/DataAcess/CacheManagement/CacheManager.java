package edu.brown.cs.student.main.DataAcess.CacheManagement;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.DataModels.ACSQueryKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

public class CacheManager<K, V> {
  private LoadingCache<K, V> cache;

  public CacheManager(CacheConfiguration config, EvictionStrategy<K, V> evictionStrategy) {
    CacheBuilder<K, V> builder = (CacheBuilder<K, V>) CacheBuilder.newBuilder();

    // config settings
    builder.maximumSize(config.getMaxSize())
        .expireAfterWrite(config.getExpireAfterWriteDuration(), config.getTimeUnit());

    // eviction strategy
    evictionStrategy.configure(builder);

    cache = builder.build(new CacheLoader<K, V>() {
      @Override
      public V load(K key) throws Exception {
        // Assume K is a type that contains necessary information for an ACS API request
        // V is the type of data we want to cache, e.g., ACS response data

        // First, build the API URL using the key
        String apiUrl = buildApiUrlForKey((ACSQueryKey) key);

        // Then, make the API request
        String apiResponse = makeApiRequest(apiUrl);

        // Parse the API response into the desired format (V)
        V parsedResponse = parseApiResponse(apiResponse);

        // Return the parsed data for caching
        return parsedResponse;
      }
    });
  }

  private String buildApiUrlForKey(ACSQueryKey key) {
    String baseUrl = "https://api.census.gov/data/";
    String year = key.getYear();
    String dataset = key.getDataset();
    String url = baseUrl + year + "/" + dataset;

    // Add query parameters for variables and geographies
    String variables = String.join(",", key.getVariables());
    url += "?get=" + variables;

    Map<String, String> geoFilters = key.getGeoFilters();
    for (Map.Entry<String, String> entry : geoFilters.entrySet()) {
      url += "&" + entry.getKey() + "=" + entry.getValue();
    }

    String apiKey = key.getApiKey();
    if (apiKey != null && !apiKey.isEmpty()) {
      url += "&key=" + apiKey;
    }

    return url;
  }

  private String makeApiRequest(String apiUrl) throws IOException, IOException {
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

  ///Implementation to parse

  private V parseApiResponse(String response) {
    return null;
  }


}
