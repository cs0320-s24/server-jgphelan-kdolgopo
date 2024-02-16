package edu.brown.cs.student.main.ServerTests;

import edu.brown.cs.student.main.APIServer.BroadbandHandler;
import edu.brown.cs.student.main.APIServer.CSVHandler;
import edu.brown.cs.student.main.DataAcess.CacheConfiguration;
import edu.brown.cs.student.main.DataAcess.EvictionStrategy;
import edu.brown.cs.student.main.main.Server;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import spark.Service;
public class ServerFuzzTest {

  private static final String BASE_URL = "http://localhost:4570"; // Adjust the port if necessary
  private static final int TEST_ITERATIONS = 10;
  private static final Random random = new Random();
  private static Service http;

  @BeforeAll
  public static void setup() {
    http = Service.ignite().port(4570);

    // Initialize handlers and configurations
    CSVHandler csvHandler = new CSVHandler();
    BroadbandHandler broadbandHandler = new BroadbandHandler();
    CacheConfiguration cacheConfig = new CacheConfiguration(100, 10, TimeUnit.MINUTES);
    EvictionStrategy<?, ?> evictionStrategy = new EvictionStrategy.SizeBasedEvictionStrategy<>();

    // Configure routes
    http.get("/loadcsv", (req, res) -> csvHandler.loadCSV(req, res));
    http.get("/viewcsv", (req, res) -> csvHandler.viewCSV(req, res));
    http.get("/searchcsv", (req, res) -> csvHandler.searchCSV(req, res));
    http.get("/broadband", broadbandHandler::handle);

    // Start the server
    http.awaitInitialization();
  }

  @Test
  public void fuzzTestServer() throws IOException {
    for (int i = 0; i < TEST_ITERATIONS; i++) {
      String endpoint = getRandomEndpoint();
      String query = getRandomQueryForEndpoint(endpoint);
      URL url = new URL(BASE_URL + endpoint + query);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      try {
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
        // Should return 10 200's
      } finally {
        connection.disconnect();
      }
    }
  }

    private String getRandomEndpoint() {
        // Return a random endpoint like "loadcsv", "viewcsv", "searchcsv", "broadband"
        String[] endpoints = {"loadcsv", "viewcsv", "searchcsv", "broadband"};
        return "/" + endpoints[random.nextInt(endpoints.length)];
    }

    private String getRandomQueryForEndpoint(String endpoint) {
        // Return a random query string based on the endpoint
        switch (endpoint) {
            case "/loadcsv":
              return "?filepath=randomfile" + random.nextInt(100) + ".csv";
            case "/viewcsv":
                // Logic for viewcsv endpoint
              return ""; // Placeholder
            case "/searchcsv":
              // Randomly decide the search method (by index, header, or all columns)
              int searchType = random.nextInt(3);
              switch (searchType) {
                case 0: // Search by column index
                  return "?index=" + random.nextInt(10) + "&value=" + "sampleValue";
                case 1: // Search by column header
                  return "?header=" + "sampleHeader" + "&value=" + "sampleValue";
                case 2: // Search across all columns
                  return "?value=" + "sampleValue";
                default:
                  return "";
              }
          case "/broadband":
                return "?state=RI&county=Providence"; // Example query
            default:
                return "";
        }
    }
}
