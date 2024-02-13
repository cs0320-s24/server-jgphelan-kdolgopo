package edu.brown.cs.student.main.DataAcess.CacheManagement;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class CacheManager<K, V> {
  private LoadingCache<K, V> cache;

  public CacheManager(long maxSize, long expireAfterWriteDuration, TimeUnit timeUnit) {
    cache =
        CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(expireAfterWriteDuration, timeUnit)
            .build(
                new CacheLoader<K, V>() {
                  @Override
                  public V load(K key) throws Exception {
                    String apiUrl = buildApiUrlForKey(key);
                    String response = makeApiRequest(apiUrl);
                    return parseApiResponse(response);
                  }
                });
  }

  private String buildApiUrlForKey(K key) {
    // Need to see how the key work
    return null;
  }

  private String makeApiRequest(String apiUrl) throws IOException {
    HttpURLConnection connection = null;
    StringBuilder response = new StringBuilder();

    try {
      URL url = new URL(apiUrl);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(10000); // 10 seconds timeout
      connection.setReadTimeout(10000);

      int responseCode = connection.getResponseCode();
      BufferedReader reader =
          new BufferedReader(
              new InputStreamReader(
                  responseCode >= 200 && responseCode < 300
                      ? connection.getInputStream()
                      : connection.getErrorStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }

    return response.toString();
  }

  private V parseApiResponse(String response) {
    //need to discuss this
    return null;
  }
}
