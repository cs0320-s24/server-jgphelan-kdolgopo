package edu.brown.cs.student.main.ServerTests;
import edu.brown.cs.student.main.Utilities.JsonUtils;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.APIServer.BroadbandHandler;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;
import com.squareup.moshi.Moshi;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BroadbandHandlerTest {

  private BroadbandHandler broadbandHandler;

  @Before
  public void setUp() {
    broadbandHandler = new BroadbandHandler();
  }

  // Inside your test class
  private Map<String, Object> parseStringToMap(String jsonString) throws IOException {
    return JsonUtils.parseStringToMap(jsonString);
  }

  @Test
  public void testHandle_Successful() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("California", "Los Angeles County, California");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    // Call the handle method
    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);
    System.out.println(result);
    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);

    // Verify the fields in the response
    assertEquals("success", resultMap.get("result"));
    assertNotNull(resultMap.get("datetime"));
    assertEquals("California", resultMap.get("state"));
    assertEquals("Los Angeles County, California", resultMap.get("county"));

    // Deserialize the broadband_data field to a Map
    Map<String, Object> broadbandData = parseStringToMap(resultMap.get("broadband_data").toString());
    assertNotNull(broadbandData);
    assertEquals("89.9", broadbandData.get("percentage"));
    assertEquals("Los Angeles County, California", broadbandData.get("county"));
  }


  @Test
  public void testHandle_InvalidState() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("InvalidState", "Los Angeles");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);
    System.out.println(result);
    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state name", resultMap.get("message"));
  }

  @Test
  public void testHandle_InvalidCounty() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("California", "InvalidCounty");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);
    System.out.println(result);
    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid county name", resultMap.get("message"));
  }

  @Test
  public void testHandle_InvalidStateAndCounty() throws IOException, InterruptedException, URISyntaxException {
    Request request = createRequest("InvalidState", "InvalidCounty");
    Response response = new Response() {
      @Override
      public void status(int statusCode) {
        // Do nothing for testing purposes
      }
    };

    Object result = broadbandHandler.handle(request, response);
    assertNotNull(result);
    System.out.println(result);
    // Deserialize the response to a Map
    Map<String, Object> resultMap = parseStringToMap(result.toString());
    assertNotNull(resultMap);
    assertEquals("error_bad_request", resultMap.get("result"));
    assertEquals("Invalid state name", resultMap.get("message"));
  }


  private Request createRequest(final String stateQueryParam, final String countyQueryParam) {
    return new Request() {
      @Override
      public String queryParams(String queryParam) {
        if (queryParam.equals("state")) {
          return stateQueryParam;
        } else if (queryParam.equals("county")) {
          return countyQueryParam;
        } else {
          return null;
        }
      }
    };
  }
}
