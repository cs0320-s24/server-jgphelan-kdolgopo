package edu.brown.cs.student.main.APIServer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class CodeFetcher {

  String apiKey = "aa979b28dba65963c8a78da9cd8bec38a6b3d6a0";
  private static final String BASE_URL = "https://api.census.gov/data/2010/dec/sf1";
  private static final HttpClient httpClient = HttpClient.newHttpClient();
  private static Map<String, String> stateCodeCache = new HashMap<>();

  //public static String getStateCode(String stateName) throws IOException, InterruptedException, URISyntaxException {

  //}

}
