package edu.brown.cs.student.main.main;

import edu.brown.cs.student.main.APIServer.BroadbandHandler;
import java.util.concurrent.TimeUnit;
import spark.Service;
import edu.brown.cs.student.main.APIServer.CSVHandler;
import edu.brown.cs.student.main.DataAcess.CacheManager;
import edu.brown.cs.student.main.DataAcess.CacheConfiguration;
import edu.brown.cs.student.main.DataAcess.EvictionStrategy;

public class Server {
  private final Service http;
  private final CacheManager<?, ?> cacheManager;

  public Server(int port, CSVHandler csvHandler, BroadbandHandler broadbandHandler, CacheConfiguration cacheConfig, EvictionStrategy<?, ?> evictionStrategy) {
    this.http = Service.ignite();
    this.http.port(port);
    this.cacheManager = new CacheManager<>(cacheConfig, evictionStrategy);
    setupRoutes(csvHandler, broadbandHandler);
  }

  private void setupRoutes(CSVHandler csvHandler, BroadbandHandler broadbandHandler) {
    // Setup route for loading CSV files
    http.get("/loadcsv", (req, res) -> csvHandler.loadCSV(req, res));

    // Setup route for viewing CSV files
    http.get("/viewcsv", (req, res) -> csvHandler.viewCSV(req, res));

    // Setup route for searching in CSV files
    http.get("/searchcsv", (req, res) -> csvHandler.searchCSV(req, res));

    // Setup route for retrieving broadband data
    http.get("/broadband", (req, res) -> broadbandHandler.handle(req, res));
  }

  public void start() {
    http.init();
  }

  public void stop() {
    http.stop();
  }

  public static void main(String[] args) {
    // Initialize handlers and cache configuration
    CSVHandler csvHandler = new CSVHandler();
    BroadbandHandler broadbandHandler = new BroadbandHandler();
    CacheConfiguration cacheConfig = new CacheConfiguration(100, 10, TimeUnit.MINUTES);
    EvictionStrategy<?, ?> evictionStrategy = new EvictionStrategy.SizeBasedEvictionStrategy<>(100);

    // Create and start the server
    Server server = new Server(4567, csvHandler, broadbandHandler, cacheConfig, evictionStrategy);
    server.start();

    System.out.println("Server Started:");
  }
}
